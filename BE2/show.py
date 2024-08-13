# from pydantic import BaseModel
# from typing import Dict, Optional, List
# from datetime import date, time, datetime

# class ShowSummary(BaseModel):
#     show_detail_id: int
#     show_detail_name: str
#     show_detail_cast: Optional[str] = None
#     show_detail_area: Optional[str] = None
#     show_detail_category: Optional[str] = None
#     show_detail_state: Optional[str] = None
#     theater_name: str
#     hall_name: str
#     schedule_days: Optional[str] = None

#     class Config:
#         from_attributes = True


# class ShowDetailCreate(BaseModel):
#     hall_id: int
#     show_detail_name: str
#     show_detail_cast: Optional[str] = None
#     show_detail_area: Optional[str] = None
#     show_detail_category: str
#     show_detail_state: str
#     show_detail_runtime: str

# class ShowDetailUpdate(BaseModel):
#     hall_id: Optional[int] = None
#     show_detail_name: Optional[str] = None
#     show_detail_cast: Optional[str] = None
#     show_detail_area: Optional[str] = None
#     show_detail_category: Optional[str] = None
#     show_detail_state: Optional[str] = None
#     show_detail_poster_image_path: Optional[str] = None
#     show_detail_runtime: Optional[str] = None

# class ShowDetailResponse(BaseModel):
#     show_detail_id: int
#     hall_id: int
#     show_detail_name: str
#     show_detail_cast: Optional[str]
#     show_detail_area: Optional[str]
#     show_detail_category: str
#     show_detail_state: str
#     show_detail_poster_image_path: Optional[str]

# class ShowScheduleCreate(BaseModel):
#     show_schedule_date_time: datetime
#     show_detail_id: int

# class ShowScheduleUpdate(BaseModel):
#     show_schedule_date_time: Optional[datetime] = None

# class PriceBySeatClassCreate(BaseModel):
#     show_detail_id: int
#     price_by_seat_class_seat_class: str
#     price_by_seat_class_price: int

# class PriceBySeatClassUpdate(BaseModel):
#     seat_class: Optional[str] = None
#     price: Optional[int] = None

# class SeatCreate(BaseModel):
#     show_detail_id: int
#     seat_row: int
#     seat_col: int
#     seat_class: str

# class SeatUpdate(BaseModel):
#     seat_row: Optional[int] = None
#     seat_col: Optional[int] = None
#     seat_class: Optional[str] = None