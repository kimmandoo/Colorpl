from pydantic import BaseModel
from typing import List
from datetime import datetime

class ShowScheduleCreate(BaseModel):
    show_schedule_date_time: datetime
    show_detail_id: int

    class Config:
        from_attributes = True

class ShowScheduleResponse(BaseModel):
    show_schedule_id: int
    show_schedule_date_time: datetime
    show_detail_id: int

    class Config:
        from_attributes = True
