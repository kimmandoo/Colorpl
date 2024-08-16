from collections import defaultdict
from sqlalchemy.orm import Session
from models import Schedule, Member, Review, Reservation, ReservationDetail, ShowSchedule, ShowDetail, Hall
from schemas.schedule import ScheduleActivity, ScheduleDetail, ScheduleUpdateDTO, ScheduleSearch
from sqlalchemy import func
from typing import Optional, List

def get_schedules(db: Session, search: Optional[ScheduleSearch] = None, skip: int = 0, limit: int = 10) -> List[ScheduleActivity]:
    # dtype이 C인 경우의 기본 쿼리
    query = db.query(
        Schedule.schedule_id,
        Schedule.dtype,
        Member.member_id,
        Member.nickname,
        Member.email,
        Schedule.schedule_date_time.label("schedule_date_time"),
        Schedule.schedule_name.label("schedule_name"),
        Schedule.schedule_category.label("schedule_category"),
        Schedule.schedule_location.label("schedule_location"),
        Schedule.schedule_image.label("schedule_image"),
        Schedule.schedule_seat.label("schedule_seat")
    ).join(Member, Schedule.member_id == Member.member_id)

    # dtype이 R인 경우를 위한 조인 추가
    reservation_query = db.query(
        Schedule.schedule_id,
        Schedule.dtype,
        Member.member_id,
        Member.nickname,
        Member.email,
        ShowSchedule.show_schedule_date_time.label("schedule_date_time"),
        Reservation.reserve_comment.label("schedule_name"),
        ShowDetail.show_detail_category.label("schedule_category"),
        Hall.hall_name.label("schedule_location"),
        ShowDetail.show_detail_poster_image_path.label("schedule_image"),
        func.concat(ReservationDetail.seat_row, '행 ', ReservationDetail.seat_col, '번').label("schedule_seat")
    ).join(Member, Schedule.member_id == Member.member_id) \
     .join(Reservation, Schedule.reserve_id == Reservation.reserve_id) \
     .join(ReservationDetail, Reservation.reserve_id == ReservationDetail.reserve_id) \
     .join(ShowSchedule, ReservationDetail.show_schedule_id == ShowSchedule.show_schedule_id) \
     .join(ShowDetail, ShowSchedule.show_detail_id == ShowDetail.show_detail_id) \
     .join(Hall, ShowDetail.hall_id == Hall.hall_id)

    # dtype에 따라 데이터를 선택
    query = query.filter(Schedule.dtype == 'C').union_all(
        reservation_query.filter(Schedule.dtype == 'R')
    )

    if search:
        query = apply_schedule_search_filters(query, search)

    # 전체 스케줄 결과를 가져옴
    schedules = query.offset(skip).limit(limit).all()

    # 그룹화된 결과를 저장할 딕셔너리
    grouped_schedules = defaultdict(list)

    # schedule_id로 그룹화
    for schedule in schedules:
        grouped_schedules[schedule.schedule_id].append(schedule)

    schedule_activities = []
    for schedule_id, grouped_schedule in grouped_schedules.items():
        # 그룹화된 스케줄의 공통 정보를 사용
        base_schedule = grouped_schedule[0]
        seats = ", ".join([s.schedule_seat for s in grouped_schedule])  # 모든 좌석을 병합

        review = db.query(Review).filter(Review.schedule_id == schedule_id).first()
        review_id = review.review_id if review else None

        schedule_activities.append(
            ScheduleActivity(
                member_id=base_schedule.member_id,
                schedule_id=base_schedule.schedule_id,
                nickname=base_schedule.nickname,
                email=base_schedule.email,
                dtype=base_schedule.dtype,
                schedule_date_time=base_schedule.schedule_date_time,
                schedule_name=base_schedule.schedule_name,
                schedule_category=base_schedule.schedule_category,
                schedule_location=base_schedule.schedule_location,
                schedule_seat=seats,  # 병합된 좌석 정보를 사용
                schedule_image=base_schedule.schedule_image,
                review_written=bool(review),
                review_id=review_id
            )
        )

    return schedule_activities

def get_schedule_by_id(db: Session, schedule_id: int) -> Optional[ScheduleDetail]:
    schedule = db.query(Schedule).filter(Schedule.schedule_id == schedule_id).first()

    if not schedule:
        return None
    
    if schedule.dtype == 'C':
        # dtype이 C인 경우
        schedule_detail = db.query(
            Schedule.schedule_id,
            Schedule.schedule_date_time,
            Schedule.schedule_name,
            Schedule.schedule_category,
            Schedule.dtype,
            Member.member_id,
            Member.nickname,
            Member.email,
            Schedule.schedule_location,
            Schedule.schedule_seat,
            Schedule.schedule_image
        ).join(Member, Schedule.member_id == Member.member_id).filter(Schedule.schedule_id == schedule_id).first()
    else:
        # dtype이 R인 경우
        schedule_detail = db.query(
            Schedule.schedule_id,
            ShowSchedule.show_schedule_date_time.label("schedule_date_time"),
            Reservation.reserve_comment.label("schedule_name"),
            ShowDetail.show_detail_category.label("schedule_category"),
            Hall.hall_name.label("schedule_location"),
            ShowDetail.show_detail_poster_image_path.label("schedule_image"),
            func.concat(ReservationDetail.seat_row, '행 ', ReservationDetail.seat_col, '번').label("schedule_seat"),
            Schedule.dtype,
            Member.member_id,
            Member.nickname,
            Member.email
        ).join(Member, Schedule.member_id == Member.member_id) \
         .join(Reservation, Schedule.reserve_id == Reservation.reserve_id) \
         .join(ReservationDetail, Reservation.reserve_id == ReservationDetail.reserve_id) \
         .join(ShowSchedule, ReservationDetail.show_schedule_id == ShowSchedule.show_schedule_id) \
         .join(ShowDetail, ShowSchedule.show_detail_id == ShowDetail.show_detail_id) \
         .join(Hall, ShowDetail.hall_id == Hall.hall_id).filter(Schedule.schedule_id == schedule_id).first()

    if schedule_detail:
        review_written = has_review(db, schedule_id)
        return ScheduleDetail(
            member_id=schedule_detail.member_id,
            schedule_id=schedule_detail.schedule_id,
            nickname=schedule_detail.nickname,
            email=schedule_detail.email,
            dtype=schedule_detail.dtype,
            schedule_date_time=schedule_detail.schedule_date_time,
            schedule_name=schedule_detail.schedule_name,
            schedule_category=schedule_detail.schedule_category,
            review_written=review_written,
            schedule_location=schedule_detail.schedule_location,
            schedule_seat=schedule_detail.schedule_seat,
            schedule_image=schedule_detail.schedule_image
        )
    return None

def apply_schedule_search_filters(query, search: ScheduleSearch):
    search_data = search.dict(exclude_unset=True)
    
    if "member_id" in search_data:
        query = query.filter(Member.member_id == search_data["member_id"])
    
    if "nickname" in search_data:
        query = query.filter(Member.nickname.ilike(f'%{search_data["nickname"]}%'))
    
    if "email" in search_data:
        query = query.filter(Member.email.ilike(f'%{search_data["email"]}%'))
    
    if "schedule_name" in search_data:
        # dtype에 따라 스케줄 이름 필터링이 다를 경우
        if "dtype" in search_data and search_data["dtype"] == "R":
            query = query.filter(Reservation.reserve_comment.ilike(f'%{search_data["schedule_name"]}%'))
        else:
            query = query.filter(Schedule.schedule_name.ilike(f'%{search_data["schedule_name"]}%'))
    
    if "schedule_category" in search_data:
        # dtype에 따라 카테고리 필터링이 다를 경우
        if "dtype" in search_data and search_data["dtype"] == "R":
            query = query.filter(ShowDetail.show_detail_category == search_data["schedule_category"])
        else:
            query = query.filter(Schedule.schedule_category == search_data["schedule_category"])
    
    if "review_written" in search_data:
        if search_data["review_written"]:
            query = query.join(Review, Review.schedule_id == Schedule.schedule_id).filter(Review.review_id.isnot(None))
        else:
            query = query.outerjoin(Review, Review.schedule_id == Schedule.schedule_id).filter(Review.review_id.is_(None))

    if "dtype" in search_data:
        query = query.filter(Schedule.dtype == search_data["dtype"])

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
