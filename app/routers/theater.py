from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.database import get_db
from app.crud.theater import get_theater_info, get_current_shows_in_hall
from app.schemas.theater import TheaterInfo, ShowInfo
from typing import List
from datetime import datetime

router = APIRouter()

@router.get("/theaters", response_model=list[TheaterInfo])
def read_theater_info(db: Session = Depends(get_db)):
    return get_theater_info(db)

@router.get("/halls/{hall_id}/shows", response_model=List[ShowInfo])
def read_current_shows_in_hall(hall_id: int, start_time: datetime, end_time: datetime, db: Session = Depends(get_db)):
    return get_current_shows_in_hall(db, hall_id, start_time, end_time)