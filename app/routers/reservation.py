from fastapi import APIRouter, Depends,HTTPException
from sqlalchemy.orm import Session
from app.crud import reservation as crud_reservation
from app.schemas.reservation import ReservationActivity, ReservationDetail, ReservationUpdate, ReservationSearch
from app.database import get_db
from typing import List

router = APIRouter()

@router.get("/reservations/activity", response_model=List[ReservationActivity])
def get_reservations_activity(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return crud_reservation.get_reservations_activity(db, skip=skip, limit=limit)

router.get("/reservations/{reservation_id}", response_model=ReservationDetail)
def get_reservation_by_id(reservation_id: int, db: Session = Depends(get_db)):
    reservation = crud_reservation.get_reservation_by_id(db, reservation_id)
    if not reservation:
        raise HTTPException(status_code=404, detail="Reservation not found")
    return reservation

@router.patch("/reservations/{reservation_id}", response_model=ReservationDetail)
def update_reservation(reservation_id: int, reservation_update: ReservationUpdate, db: Session = Depends(get_db)):
    reservation = crud_reservation.update_reservation(db, reservation_id, reservation_update)
    if not reservation:
        raise HTTPException(status_code=404, detail="Reservation not found")
    return reservation

@router.post("/reservations/search", response_model=List[ReservationActivity])
def search_reservations(search: ReservationSearch, skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return crud_reservation.search_reservations(db, search, skip=skip, limit=limit)