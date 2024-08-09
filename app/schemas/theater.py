from pydantic import BaseModel
from typing import List
from datetime import datetime

class HallInfo(BaseModel):
    hall_id: int
    hall_name: str
    hall_api_id: str

class TheaterInfo(BaseModel):
    theater_id: int
    theater_name: str
    theater_address: str
    theater_latitude: float
    theater_longitude: float
    halls: List[HallInfo]

class ShowInfo(BaseModel):
    show_detail_id: int
    show_detail_name: str
    show_detail_poster_image_path: str
    show_schedule_date_time: datetime
