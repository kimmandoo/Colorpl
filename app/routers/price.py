from typing import List
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.schemas.price import PriceBySeatClassCreate, PriceBySeatClassResponse
from app.crud.price import create_price_by_seat_class, get_price_by_seat_class
from app.database import get_db

router = APIRouter()

@router.post("/price", response_model=PriceBySeatClassResponse)
def create_price_by_seat_class_route(price_data: PriceBySeatClassCreate, db: Session = Depends(get_db)):
    return create_price_by_seat_class(db, price_data)

@router.get("/price/{show_detail_id}", response_model=List[PriceBySeatClassResponse])
def get_price_by_seat_class_route(show_detail_id: int, db: Session = Depends(get_db)):
    prices = get_price_by_seat_class(db, show_detail_id)
    if not prices:
        raise HTTPException(status_code=404, detail="Prices not found")
    return prices
