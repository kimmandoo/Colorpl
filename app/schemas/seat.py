from pydantic import BaseModel
from typing import List
from app.utils.enum import SeatGrade

class SeatCreate(BaseModel):
    seat_col: int
    seat_row: int
    seat_class: SeatGrade
    show_detail_id: int

    class Config:
        from_attributes = True

class SeatResponse(SeatCreate):
    seat_id: int

    class Config:
        from_attributes = True

class SeatCreateRequest(BaseModel):
    seats: List[SeatCreate]

    class Config:
        from_attributes = True
