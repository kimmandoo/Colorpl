from datetime import datetime
import shutil
from fastapi import APIRouter, Depends, Form, HTTPException, UploadFile, File
from sqlalchemy.orm import Session
from schemas.schedule import ScheduleActivity, ScheduleDetail, ScheduleCreate, ScheduleUpdateDTO, ScheduleSearch
from crud.schedule import get_schedules, has_review, update_schedule, get_schedule_by_id
from database import get_db
from typing import List, Optional
from utils.enum import Category
# from utils.image import delete_image_file, update_image_file, save_image_file
from models import Hall, Reservation, ReservationDetail, Review, Schedule, ShowDetail, ShowSchedule

router = APIRouter()

@router.get("/schedules", response_model=List[ScheduleActivity])
def read_schedules(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return get_schedules(db, skip=skip, limit=limit)

@router.post("/schedules/search", response_model=List[ScheduleActivity])
def search_schedules(search: ScheduleSearch, skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return get_schedules(db, search=search, skip=skip, limit=limit)

@router.get("/schedules/{schedule_id}", response_model=ScheduleDetail)
def read_schedule_detail(schedule_id: int, db: Session = Depends(get_db)):
    schedule = get_schedule_by_id(db, schedule_id)
    if not schedule:
        raise HTTPException(status_code=404, detail="Schedule not found")
    return schedule

@router.patch("/schedules/{schedule_id}", response_model=ScheduleActivity)
def update_schedule_endpoint(
    schedule_id: int,
    schedule_update: ScheduleUpdateDTO,
    db: Session = Depends(get_db)
):
    schedule = db.query(Schedule).filter(Schedule.schedule_id == schedule_id).first()
    if not schedule:
        raise HTTPException(status_code=404, detail="Schedule not found")

    # 전달된 필드만 업데이트
    update_data = schedule_update.dict(exclude_unset=True)
    for key, value in update_data.items():
        setattr(schedule, key, value)

    if schedule.dtype == "C":
        # Custom 스케줄의 경우
        db.commit()
        db.refresh(schedule)

        review = db.query(Review).filter(Review.schedule_id == schedule.schedule_id).first()
        review_id = review.review_id if review else None

        return {
            "schedule_id": schedule.schedule_id,
            "member_id": schedule.member_id,
            "nickname": schedule.member.nickname,
            "email": schedule.member.email,
            "dtype": schedule.dtype,
            "schedule_date_time": schedule.schedule_date_time,
            "schedule_name": schedule.schedule_name,
            "schedule_category": schedule.schedule_category,
            "schedule_location": schedule.schedule_location,
            "schedule_seat": schedule.schedule_seat,
            "schedule_image": schedule.schedule_image,
            "review_written": bool(review),
            "review_id": review_id
        }

    else:
        # Reservation 스케줄의 경우
        reservation = db.query(Reservation).filter(Reservation.reserve_id == schedule.reserve_id).first()
        reservation_detail = db.query(ReservationDetail).filter(ReservationDetail.reserve_id == schedule.reserve_id).first()
        if not reservation or not reservation_detail:
            raise HTTPException(status_code=404, detail="Reservation or Reservation detail not found")

        # 조인하여 필요한 정보를 가져옴
        show_schedule = db.query(ShowSchedule).filter(ShowSchedule.show_schedule_id == reservation_detail.show_schedule_id).first()
        show_detail = db.query(ShowDetail).filter(ShowDetail.show_detail_id == show_schedule.show_schedule_id).first()
        hall = db.query(Hall).filter(Hall.hall_id == show_detail.hall_id).first()

        db.commit()  # 변경사항 커밋
        db.refresh(schedule)  # 새로고침하여 최신 데이터 반영

        review = db.query(Review).filter(Review.schedule_id == schedule.schedule_id).first()
        review_id = review.review_id if review else None

        return {
            "schedule_id": schedule.schedule_id,
            "member_id": schedule.member_id,
            "nickname": schedule.member.nickname,
            "email": schedule.member.email,
            "dtype": schedule.dtype,
            "schedule_date_time": show_schedule.show_schedule_date_time,
            "schedule_name": schedule.schedule_name,  # reservation.reserve_comment 대신 schedule.schedule_name 사용
            "schedule_category": show_detail.show_detail_category,
            "schedule_location": hall.hall_name,
            "schedule_seat": f"{reservation_detail.seat_row}행 {reservation_detail.seat_col}번",
            "schedule_image": show_detail.show_detail_poster_image_path,
            "review_written": bool(review),
            "review_id": review_id
        }
