from datetime import datetime
import shutil
from fastapi import APIRouter, Depends, Form, HTTPException, UploadFile, File
from sqlalchemy.orm import Session
from app.schemas.schedule import ScheduleActivity, ScheduleDetail, ScheduleCreate, ScheduleUpdateDTO, ScheduleSearch
from app.crud.schedule import get_schedules, get_schedule_detail, has_review, update_schedule, create_schedule, get_schedule_by_id
from app.database import get_db
from typing import List, Optional
from app.utils.enum import Category
from app.utils.image import delete_image_file, update_image_file, save_image_file
from app.models import Schedule

router = APIRouter()

@router.get("/schedules", response_model=List[ScheduleActivity])
def read_schedules(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return get_schedules(db, skip=skip, limit=limit)

@router.post("/schedules/search", response_model=List[ScheduleActivity])
def search_schedules(search: ScheduleSearch, skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return get_schedules(db, search=search, skip=skip, limit=limit)

@router.get("/schedules/{schedule_id}", response_model=ScheduleDetail)
def read_schedule_detail(schedule_id: int, db: Session = Depends(get_db)):
    schedule = get_schedule_detail(db, schedule_id)
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

    update_data = schedule_update.dict(exclude_unset=True)
    for key, value in update_data.items():
        setattr(schedule, key, value)

    db.commit()
    db.refresh(schedule)

    # Review and reservation status checks
    review_written = has_review(db, schedule.schedule_id)
    is_reserved = schedule.reserve_detail_id is not None

    return {
        "schedule_id": schedule.schedule_id,
        "nickname": schedule.member.nickname,
        "email": schedule.member.email,
        "dtype": schedule.dtype,
        "schedule_date_time": schedule.schedule_date_time,
        "schedule_name": schedule.schedule_name,
        "schedule_category": schedule.schedule_category,
        "schedule_latitude": schedule.schedule_latitude,
        "schedule_longitude": schedule.schedule_longitude,
        "schedule_location": schedule.schedule_location,
        "schedule_seat": schedule.schedule_seat,
        "schedule_image": schedule.schedule_image,
        "review_written": review_written,
        "is_reserved": is_reserved,
    }

@router.post("/schedules", response_model=ScheduleDetail)
def create_schedule_endpoint(schedule: ScheduleCreate, db: Session = Depends(get_db), file: UploadFile = File(...)):
    filename = f"images/{file.filename}"
    with open(filename, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    return create_schedule(db, schedule, filename=filename)

@router.patch("/schedules/{schedule_id}/image", response_model=dict)
def update_schedule_image(
    schedule_id: int,
    file: UploadFile = File(...),
    db: Session = Depends(get_db)
):
    schedule = db.query(Schedule).filter(Schedule.schedule_id == schedule_id).first()
    if not schedule:
        raise HTTPException(status_code=404, detail="Schedule not found")

    # 기존 이미지가 있으면 삭제
    if schedule.schedule_image:
        delete_image_file(schedule.schedule_image)

    # 새 이미지 저장
    filename = save_image_file(file)
    schedule.schedule_image = filename

    db.commit()
    db.refresh(schedule)

    return {
        "schedule_id": schedule.schedule_id,
        "schedule_image": schedule.schedule_image,
    }