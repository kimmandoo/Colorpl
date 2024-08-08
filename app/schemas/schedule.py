from pydantic import BaseModel, validator
from typing import Optional, List
from datetime import datetime

class ScheduleActivity(BaseModel):
    schedule_id: int
    nickname: Optional[str] = None
    email: Optional[str] = None
    dtype: Optional[str] = None
    schedule_date_time: Optional[datetime] = None
    schedule_name: Optional[str] = None
    schedule_category: Optional[int] = None
    review_written: bool

    class Config:
        from_attributes = True

class ScheduleCreate(BaseModel):
    schedule_name: str
    schedule_date_time: datetime
    schedule_category: int
    schedule_seat: str
    schedule_image: Optional[str]

class ScheduleUpdateDTO(BaseModel):
    schedule_name: Optional[str]
    schedule_date_time: Optional[datetime]
    schedule_category: Optional[int]
    schedule_seat: Optional[str]

class ScheduleSearch(BaseModel):
    nickname: Optional[str] = None
    email: Optional[str] = None
    schedule_name: Optional[str] = None
    schedule_category: Optional[int] = None
