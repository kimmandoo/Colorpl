from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime
from utils.enum import Category

class ReservationActivity(BaseModel):
    reserve_id: int
    nickname: str
    create_date: datetime
    reserve_amount: str
    reserve_date: datetime
    is_refunded: bool
    show_detail_category: Category

    class Config:
        from_attributes = True

class ReservationDetail(BaseModel):
    reserve_id: int
    nickname: str
    profile_image: Optional[str] = None
    create_date: datetime
    update_date: Optional[datetime] = None
    reserve_date: datetime
    reserve_amount: str
    reserve_comment: Optional[str] = None
    is_refunded: bool
    seat_col: int
    seat_row: int
    show_detail: dict

    class Config:
        from_attributes = True

class ReservationUpdate(BaseModel):
    reserve_amount: Optional[str] = None
    reserve_comment: Optional[str] = None
    is_refunded: Optional[bool] = None

class ReservationSearch(BaseModel):
    nickname: Optional[str] = None
    email: Optional[str] = None
    reserve_id: Optional[int] = None
    show_detail_category: Optional[Category] = None
    is_refunded: Optional[bool] = None
