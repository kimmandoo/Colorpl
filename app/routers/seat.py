from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from app.schemas.seat import SeatCreateRequest, SeatResponse
from app.crud.seat import create_seats_bulk, get_seats_by_show_detail
from app.database import get_db

router = APIRouter()

@router.post("/seats", response_model=List[SeatResponse])
def create_seats(seat_request: SeatCreateRequest, db: Session = Depends(get_db)):
    seats = create_seats_bulk(db, seat_request.seats)
    return seats

@router.get("/seats/{show_detail_id}", response_model=List[SeatResponse])
def get_seats(show_detail_id: int, db: Session = Depends(get_db)):
    seats = get_seats_by_show_detail(db, show_detail_id)
    if not seats:
        raise HTTPException(status_code=404, detail="Seats not found")
    return seats
