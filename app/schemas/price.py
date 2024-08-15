from pydantic import BaseModel
from typing import List, Optional
from utils.enum import SeatGrade

class PriceBySeatClassCreate(BaseModel):
    show_detail_id: int
    price_by_seat_class_price: int
    price_by_seat_class_seat_class: SeatGrade

    class Config:
        from_attributes = True

class PriceBySeatClassResponse(PriceBySeatClassCreate):
    pass
