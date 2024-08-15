from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from schemas.show_schedule import ShowScheduleCreate, ShowScheduleResponse
from crud.show_schedule import create_show_schedule, get_schedules_by_hall
from database import get_db

router = APIRouter()

@router.post("/schedules", response_model=ShowScheduleResponse)
def create_schedule(schedule: ShowScheduleCreate, db: Session = Depends(get_db)):
    try:
        db_schedule = create_show_schedule(db, schedule)
        return db_schedule
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"Error creating schedule: {str(e)}")

@router.get("/schedules/hall/{hall_id}", response_model=List[ShowScheduleResponse])
def get_schedules(hall_id: int, db: Session = Depends(get_db)):
    schedules = get_schedules_by_hall(db, hall_id)
    if not schedules:
        raise HTTPException(status_code=404, detail="Schedules not found")
    return schedules
