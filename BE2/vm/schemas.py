from pydantic import BaseModel
from typing import List, Optional
from datetime import datetime

class ShowDetailBase(BaseModel):
    show_detail_api_id: str
    show_detail_area: str
    show_detail_cast: str
    show_detail_category: Optional[int] = None
    show_hall: str
    show_detail_name: str
    show_detail_poster_image_path: Optional[str] = None
    show_detail_runtime: str
    show_detail_state: str
    theater_id: Optional[int] = None

class ShowDetailCreate(ShowDetailBase):
    pass

class ShowDetail(ShowDetailBase):
    show_detail_id: int

    class Config:
        from_attributes = True

class ShowScheduleBase(BaseModel):
    show_schedule_date_time: datetime
    show_detail_id: int

class ShowScheduleCreate(ShowScheduleBase):
    pass

class ShowSchedule(ShowScheduleBase):
    show_schedule_id: int

    class Config:
        from_attributes = True

class TheaterBase(BaseModel):
    theater_address: str
    theater_api_id: str
    theater_latitude: float
    theater_longitude: float
    theater_name: str

class TheaterCreate(TheaterBase):
    pass

class Theater(TheaterBase):
    theater_id: int
    show_details: List[ShowDetail] = []

    class Config:
        from_attributes = True

class SeatBase(BaseModel):
    seat_col: int
    seat_row: int
    seat_class: str
    show_detail_id: int

class SeatCreate(SeatBase):
    pass

class Seat(SeatBase):
    seat_id: int

    class Config:
        from_attributes = True

class PriceBySeatClassBase(BaseModel):
    show_detail_id: int
    price_by_seat_class_price: int
    price_by_seat_class_seat_class: str

class PriceBySeatClassCreate(PriceBySeatClassBase):
    pass

class PriceBySeatClass(PriceBySeatClassBase):
    class Config:
        from_attributes = True
