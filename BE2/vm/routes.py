from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from .database import SessionLocal, get_db
from .models import Show, SeatGradePrice, SeatInfo
from .schemas import ShowSchema, ShowCreateSchema, ShowUpdateSchema, SeatGradePriceSchema, SeatGradePriceUpdateSchema
from typing import List

router = APIRouter()


def generate_seat_arrangement(rows: int, pattern: str) -> List[List[int]]:

    seat_arrangement = []
    pattern_length = len(pattern)
    
    for i in range(rows):
        row_pattern = []
        for j in range(pattern_length):
            row_pattern.append(int(pattern[j]))
        seat_arrangement.append(row_pattern)
    
    return seat_arrangement


@router.get("/shows/{show_id}", response_model=ShowSchema)
def read_show(show_id: int, db: Session = Depends(get_db)):
    db_show = db.query(Show).filter(Show.SHOW_ID == show_id).first()
    if db_show is None:
        raise HTTPException(status_code=404, detail="Show not found")
    return db_show

@router.get("/shows/", response_model=List[ShowSchema])
def read_shows(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    shows = db.query(Show).offset(skip).limit(limit).all()
    return shows

@router.post("/shows/", response_model=ShowSchema)
def create_show(show: ShowCreateSchema, db: Session = Depends(get_db)):
    db_show = Show(**show.dict())
    db.add(db_show)
    db.commit()
    db.refresh(db_show)
    return db_show

@router.put("/shows/{show_id}", response_model=ShowSchema)
def update_show(show_id: int, show: ShowUpdateSchema, db: Session = Depends(get_db)):
    db_show = db.query(Show).filter(Show.SHOW_ID == show_id).first()
    if db_show is None:
        raise HTTPException(status_code=404, detail="Show not found")
    for var, value in vars(show).items():
        if value is not None:
            setattr(db_show, var, value)
    db.commit()
    db.refresh(db_show)
    return db_show

@router.delete("/shows/{show_id}")
def delete_show(show_id: int, db: Session = Depends(get_db)):
    db_show = db.query(Show).filter(Show.SHOW_ID == show_id).first()
    if db_show is None:
        raise HTTPException(status_code=404, detail="Show not found")
    db.delete(db_show)
    db.commit()
    return {"ok": True}


@router.get("/seats/{show_id}", response_model=List[SeatGradePriceSchema])
def read_seats(show_id: int, db: Session = Depends(get_db)):
    seats = db.query(SeatGradePrice).filter(SeatGradePrice.SHOW_ID == show_id).all()
    if not seats:
        raise HTTPException(status_code=404, detail="Seats not found")
    return seats

@router.put("/seats/{seat_id}", response_model=SeatGradePriceSchema)
def update_seat(seat_id: int, seat: SeatGradePriceUpdateSchema, db: Session = Depends(get_db)):
    db_seat = db.query(SeatGradePrice).filter(SeatGradePrice.SHOW_ID == seat_id).first()
    if db_seat is None:
        raise HTTPException(status_code=404, detail="Seat not found")
    for var, value in vars(seat).items():
        if value is not None:
            setattr(db_seat, var, value)
    db.commit()
    db.refresh(db_seat)
    return db_seat

@router.delete("/seats/{seat_id}")
def delete_seat(seat_id: int, db: Session = Depends(get_db)):
    db_seat = db.query(SeatGradePrice).filter(SeatGradePrice.SHOW_ID == seat_id).first()
    if db_seat is None:
        raise HTTPException(status_code=404, detail="Seat not found")
    db.delete(db_seat)
    db.commit()
    return {"ok": True}


@router.put("/seat-grades/{seat_id}", response_model=SeatGradePriceSchema)
def update_seat_grade(seat_id: int, seat_grade: SeatGradePriceUpdateSchema, db: Session = Depends(get_db)):
    db_seat_grade = db.query(SeatGradePrice).filter(SeatGradePrice.SHOW_ID == seat_id).first()
    if db_seat_grade is None:
        raise HTTPException(status_code=404, detail="Seat grade not found")
    for var, value in vars(seat_grade).items():
        if value is not None:
            setattr(db_seat_grade, var, value)
    db.commit()
    db.refresh(db_seat_grade)
    return db_seat_grade


@router.get("/seats/arrangement/{rows}", response_model=List[List[int]])
def get_seat_arrangement(rows: int, pattern: str = "33332221112223333"):
    seat_arrangement = generate_seat_arrangement(rows, pattern)
    return seat_arrangement
