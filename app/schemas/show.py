from pydantic import BaseModel
from typing import Optional

class ShowSummary(BaseModel):
    show_detail_id: int
    show_detail_name: str
    show_detail_cast: Optional[str] = None
    show_detail_area: Optional[str] = None
    show_detail_category: Optional[str] = None
    show_detail_state: Optional[str] = None
    theater_name: str
    hall_name: str
    schedule_days: Optional[str] = None

    class Config:
        from_attributes = True
