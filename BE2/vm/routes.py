from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from . import crud, schemas, models
from .database import get_db

router = APIRouter()

@router.get("/show_details", response_model=List[schemas.ShowDetail])
async def read_show_details(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    return crud.get_show_details(db, skip=skip, limit=limit)

@router.get("/show_details/{show_detail_id}", response_model=schemas.ShowDetail)
async def read_show_detail(show_detail_id: int, db: Session = Depends(get_db)):
    db_show_detail = crud.get_show_detail(db, show_detail_id=show_detail_id)
    if db_show_detail is None:
        raise HTTPException(status_code=404, detail="ShowDetail not found")
    return db_show_detail

@router.post("/show_details", response_model=schemas.ShowDetail)
async def create_show_detail(show_detail: schemas.ShowDetailCreate, db: Session = Depends(get_db)):
    return crud.create_show_detail(db=db, show_detail=show_detail)

@router.get("/show_schedules", response_model=List[schemas.ShowSchedule])
async def read_show_schedules(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    return crud.get_show_schedules(db, skip=skip, limit=limit)

@router.post("/show_schedules", response_model=schemas.ShowSchedule)
async def create_show_schedule(show_schedule: schemas.ShowScheduleCreate, db: Session = Depends(get_db)):
    return crud.create_show_schedule(db=db, show_schedule=show_schedule)

@router.get("/theaters", response_model=List[schemas.Theater])
async def read_theaters(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    return crud.get_theaters(db, skip=skip, limit=limit)

@router.post("/theaters", response_model=schemas.Theater)
async def create_theater(theater: schemas.TheaterCreate, db: Session = Depends(get_db)):
    return crud.create_theater(db=db, theater=theater)

@router.get("/seats", response_model=List[schemas.Seat])
async def read_seats(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    return crud.get_seats(db, skip=skip, limit=limit)

@router.post("/seats", response_model=schemas.Seat)
async def create_seat(seat: schemas.SeatCreate, db: Session = Depends(get_db)):
    return crud.create_seat(db=db, seat=seat)

@router.get("/prices_by_seat_class", response_model=List[schemas.PriceBySeatClass])
async def read_prices_by_seat_class(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    return crud.get_prices_by_seat_class(db, skip=skip, limit=limit)

@router.post("/prices_by_seat_class", response_model=schemas.PriceBySeatClass)
async def create_price_by_seat_class(price_by_seat_class: schemas.PriceBySeatClassCreate, db: Session = Depends(get_db)):
    return crud.create_price_by_seat_class(db=db, price_by_seat_class=price_by_seat_class)
