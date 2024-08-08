from fastapi import APIRouter, Depends, HTTPException, UploadFile, File
from sqlalchemy.orm import Session
from app.database import get_db
from app.schemas.schedule import ScheduleCreate, ScheduleUpdateDTO, ScheduleSearch, ScheduleActivity
from app.crud.schedule import get_schedules, update_schedule, create_schedule
import shutil
from typing import List

router = APIRouter()

@router.get("/schedules", response_model=List[ScheduleActivity])
def read_schedules(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return get_schedules(db, skip=skip, limit=limit)

@router.post("/schedules/search", response_model=List[ScheduleActivity])
def search_schedules(search: ScheduleSearch, skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return get_schedules(db, search=search, skip=skip, limit=limit)

@router.patch("/schedule/{schedule_id}", response_model=ScheduleActivity)
def update_schedule_endpoint(schedule_id: int, schedule_update: ScheduleUpdateDTO, db: Session = Depends(get_db), file: UploadFile = File(None)):
    filename = None
    if file:
        filename = f"images/{file.filename}"
        with open(filename, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)

    db_schedule = update_schedule(db, schedule_id, schedule_update, filename)
    if db_schedule is None:
        raise HTTPException(status_code=404, detail="Schedule not found")

    return db_schedule

@router.post("/schedule", response_model=ScheduleActivity)
def create_schedule_endpoint(schedule: ScheduleCreate, db: Session = Depends(get_db), file: UploadFile = File(...)):
    filename = f"images/{file.filename}"
    with open(filename, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    return create_schedule(db, schedule, filename=filename)
