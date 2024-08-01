from pydantic import BaseModel
from datetime import datetime
from typing import List, Optional


class ShowDateSchema(BaseModel):
    SHOW_DATE_ID: int
    SHOW_ID: int
    SHOW_DATE: datetime
    SHOW_TIME: str

    class Config:
        orm_mode = True

class ShowSchema(BaseModel):
    SHOW_ID: int
    SHOW_NAME: str
    SHOW_START_DATE: datetime
    SHOW_END_DATE: datetime
    SHOW_CAST: str
    SHOW_RUNTIME: str
    SHOW_RATING: str
    SHOW_POSTER_IMAGE_PATH: str
    SHOW_AREA: str
    SHOW_STATE: str
    SHOW_SCHEDULE: str
    show_dates: List[ShowDateSchema] = []

    class Config:
        orm_mode = True


class ShowCreateSchema(BaseModel):
    HALL_ID: int
    SHOW_NAME: str
    SHOW_START_DATE: datetime
    SHOW_END_DATE: datetime
    SHOW_CAST: str
    SHOW_RUNTIME: str
    SHOW_RATING: str
    SHOW_POSTER_IMAGE_PATH: str
    SHOW_AREA: str
    SHOW_STATE: str
    SHOW_SCHEDULE: str

    class Config:
        orm_mode = True

class ShowUpdateSchema(BaseModel):
    HALL_ID: Optional[int] = None
    SHOW_NAME: Optional[str] = None
    SHOW_START_DATE: Optional[datetime] = None
    SHOW_END_DATE: Optional[datetime] = None
    SHOW_CAST: Optional[str] = None
    SHOW_RUNTIME: Optional[str] = None
    SHOW_RATING: Optional[str] = None
    SHOW_POSTER_IMAGE_PATH: Optional[str] = None
    SHOW_AREA: Optional[str] = None
    SHOW_STATE: Optional[str] = None
    SHOW_SCHEDULE: Optional[str] = None

    class Config:
        orm_mode = True


class SeatGradePriceSchema(BaseModel):
    SEAT_PRICE: int
    FIELD1: str
    FIELD2: Optional[int] = None

    class Config:
        orm_mode = True

class SeatGradePriceUpdateSchema(BaseModel):
    SEAT_PRICE: Optional[int] = None
    FIELD1: Optional[str] = None
    FIELD2: Optional[int] = None

    class Config:
        orm_mode = True
