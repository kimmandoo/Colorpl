from sqlalchemy.orm import Session
from app.models import Schedule, Member, Review, ReservationDetail
from app.schemas.schedule import ScheduleActivity, ScheduleDetail, ScheduleCreate, ScheduleUpdateDTO, ScheduleSearch
from sqlalchemy import func
from typing import Optional, List

def get_schedules(db: Session, search: ScheduleSearch = None, skip: int = 0, limit: int = 10) -> List[ScheduleActivity]:
    query = db.query(
        Schedule.schedule_id,
        Schedule.schedule_date_time,
        Schedule.schedule_name,
        Schedule.schedule_category,
        Schedule.dtype,
        Member.nickname,
        Member.email,
        Schedule.reserve_detail_id
    ).join(Member, Schedule.member_id == Member.member_id)

    if search:
        query = apply_schedule_search_filters(query, search)

    schedules = query.offset(skip).limit(limit).all()

    return [
        ScheduleActivity(
            schedule_id=schedule.schedule_id,
            nickname=schedule.nickname,
            email=schedule.email,
            dtype=schedule.dtype,
            schedule_date_time=schedule.schedule_date_time,
            schedule_name=schedule.schedule_name,
            schedule_category=schedule.schedule_category,
            review_written=has_review(db, schedule.schedule_id),
            is_reserved=schedule.reserve_detail_id is not None
        )
        for schedule in schedules
    ]

def get_schedule_by_id(db: Session, schedule_id: int):
    query = db.query(
        Schedule.schedule_id,
        Schedule.schedule_date_time,
        Schedule.schedule_name,
        Schedule.schedule_category,
        Schedule.dtype,
        Member.nickname,
        Member.email,
        Schedule.schedule_latitude,
        Schedule.schedule_longitude,
        Schedule.schedule_location,
        Schedule.schedule_seat,
        Schedule.schedule_image
    ).join(Member, Schedule.member_id == Member.member_id).filter(Schedule.schedule_id == schedule_id).first()

    if query:
        review_written = has_review(db, schedule_id)
        return {
            "schedule_id": query.schedule_id,
            "nickname": query.nickname,
            "email": query.email,
            "dtype": query.dtype,
            "schedule_date_time": query.schedule_date_time,
            "schedule_name": query.schedule_name,
            "schedule_category": query.schedule_category,
            "review_written": review_written,
            "schedule_latitude": query.schedule_latitude,
            "schedule_longitude": query.schedule_longitude,
            "schedule_location": query.schedule_location,
            "schedule_seat": query.schedule_seat,
            "schedule_image": query.schedule_image,
        }
    return None

def get_schedule_detail(db: Session, schedule_id: int) -> Optional[ScheduleDetail]:
    schedule = db.query(Schedule).filter(Schedule.schedule_id == schedule_id).first()
    if not schedule:
        return None
    
    return ScheduleDetail(
        schedule_id=schedule.schedule_id,
        nickname=schedule.member.nickname,
        email=schedule.member.email,
        dtype=schedule.dtype,
        schedule_date_time=schedule.schedule_date_time,
        schedule_name=schedule.schedule_name,
        schedule_category=schedule.schedule_category,
        schedule_latitude=schedule.schedule_latitude,
        schedule_longitude=schedule.schedule_longitude,
        schedule_location=schedule.schedule_location,
        schedule_seat=schedule.schedule_seat,
        schedule_image=schedule.schedule_image,
        review_written=has_review(db, schedule.schedule_id),
        is_reserved=schedule.reserve_detail_id is not None
    )

def apply_schedule_search_filters(query, search: ScheduleSearch):
    search_data = search.dict(exclude_unset=True)
    
    if "nickname" in search_data:
        query = query.filter(Member.nickname.ilike(f'%{search_data["nickname"]}%'))
    
    if "email" in search_data:
        query = query.filter(Member.email.ilike(f'%{search_data["email"]}%'))
    
    if "schedule_name" in search_data:
        query = query.filter(Schedule.schedule_name.ilike(f'%{search_data["schedule_name"]}%'))
    
    if "schedule_category" in search_data:
        query = query.filter(Schedule.schedule_category == search_data["schedule_category"])
    
    if "review_written" in search_data:
        if search_data["review_written"]:
            query = query.filter(Review.schedule_id == Schedule.schedule_id)
        else:
            query = query.outerjoin(Review).filter(Review.schedule_id.is_(None))
    
    if "is_reserved" in search_data:
        if search_data["is_reserved"]:
            query = query.filter(Schedule.reserve_detail_id.isnot(None))
        else:
            query = query.filter(Schedule.reserve_detail_id.is_(None))
    
    return query


def has_review(db: Session, schedule_id: int) -> bool:
    review_count = db.query(func.count(Review.review_id)).filter(Review.schedule_id == schedule_id).scalar()
    return review_count > 0

def update_schedule(db: Session, schedule_id: int, schedule_update: ScheduleUpdateDTO, filename: Optional[str] = None):
    schedule = db.query(Schedule).filter(Schedule.schedule_id == schedule_id).first()
    if not schedule:
        return None
    
    update_data = schedule_update.dict(exclude_unset=True)  # 지정된 필드만 포함
    for key, value in update_data.items():
        setattr(schedule, key, value)
    
    if filename:
        schedule.schedule_image = filename
    
    db.commit()
    db.refresh(schedule)
    
    return schedule

def create_schedule(db: Session, schedule: ScheduleCreate, filename: str) -> ScheduleDetail:
    db_schedule = Schedule(
        **schedule.dict(),
        dtype='RESERVATION' if schedule.reserve_detail_id else 'CUSTOM',
        schedule_image=filename
    )
    db.add(db_schedule)
    db.commit()
    db.refresh(db_schedule)
    return get_schedule_detail(db, db_schedule.schedule_id)
