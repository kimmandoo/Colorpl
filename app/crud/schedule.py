from sqlalchemy.orm import Session
from app.models import Schedule, Member, Review
from app.schemas.schedule import ScheduleCreate, ScheduleUpdateDTO, ScheduleSearch
from sqlalchemy import func
from typing import Optional

def get_schedules(db: Session, search: ScheduleSearch = None, skip: int = 0, limit: int = 10):
    query = db.query(
        Schedule.schedule_id,
        Schedule.schedule_date_time,
        Schedule.schedule_name,
        Schedule.schedule_category,
        Schedule.dtype,
        Member.nickname,
        Member.email
    ).join(Member, Schedule.member_id == Member.member_id)

    if search:
        query = apply_schedule_search_filters(query, search)

    schedules = query.offset(skip).limit(limit).all()

    schedule_list = []
    for schedule in schedules:
        review_written = has_review(db, schedule.schedule_id)
        schedule_list.append({
            "schedule_id": schedule.schedule_id,
            "nickname": schedule.nickname,
            "email": schedule.email,
            "dtype": schedule.dtype,
            "schedule_date_time": schedule.schedule_date_time,
            "schedule_name": schedule.schedule_name,
            "schedule_category": schedule.schedule_category,
            "review_written": review_written,
        })

    return schedule_list

def apply_schedule_search_filters(query, search: ScheduleSearch):
    search_data = search.dict(exclude_unset=True)
    if "nickname" in search_data:
        query = query.filter(Member.nickname.ilike(f'%{search_data["nickname"]}%'))
    if "email" in search_data:
        query = query.filter(Member.email.ilike(f'%{search_data["email"]}%'))
    if "schedule_name" in search_data:
        query = query.filter(Schedule.schedule_name.ilike(f'%{search_data["schedule_name"]}'))
    if "schedule_category" in search_data:
        query = query.filter(Schedule.schedule_category == search_data["schedule_category"])
    return query

def has_review(db: Session, schedule_id: int) -> bool:
    review_count = db.query(func.count(Review.review_id)).filter(Review.schedule_id == schedule_id).scalar()
    return review_count > 0

def update_schedule(db: Session, schedule_id: int, schedule_update: ScheduleUpdateDTO, filename: Optional[str] = None):
    schedule = db.query(Schedule).filter(Schedule.schedule_id == schedule_id).first()
    if not schedule:
        return None
    update_data = schedule_update.dict(exclude_unset=True)  # Only update fields that are provided
    for key, value in update_data.items():
        setattr(schedule, key, value)
    if filename:
        schedule.schedule_image = filename
    db.commit()
    db.refresh(schedule)

    review_written = has_review(db, schedule.schedule_id)
    return {
        "schedule_id": schedule.schedule_id,
        "nickname": schedule.member.nickname,
        "email": schedule.member.email,
        "dtype": schedule.dtype,
        "schedule_date_time": schedule.schedule_date_time,
        "schedule_name": schedule.schedule_name,
        "schedule_category": schedule.schedule_category,
        "review_written": review_written,
    }


def create_schedule(db: Session, schedule: ScheduleCreate, filename: str):
    db_schedule = Schedule(
        **schedule.dict(),
        dtype='RESERVATION' if schedule.reserve_detail_id else 'CUSTOM',
        schedule_image=filename
    )
    db.add(db_schedule)
    db.commit()
    db.refresh(db_schedule)
    return db_schedule
