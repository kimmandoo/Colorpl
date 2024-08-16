from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime
from utils.enum import Category

class ReservationActivity(BaseModel):
    reserve_id: int
    member_id: Optional[int]
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
    member_id: int
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
    show_detail_name: str
    hall_name: str

    class Config:
        from_attributes = True

class ReservationUpdate(BaseModel):
    reserve_amount: Optional[str] = None
    reserve_comment: Optional[str] = None
    is_refunded: Optional[bool] = None

class ReservationSearch(BaseModel):
    member_id: Optional[int] = None  # member_id를 검색에 사용
    nickname: Optional[str] = None
    email: Optional[str] = None
    reserve_id: Optional[int] = None
    show_detail_category: Optional[Category] = None
    is_refunded: Optional[bool] = None
    skip: Optional[int] = 0
    limit: Optional[int] = 10
