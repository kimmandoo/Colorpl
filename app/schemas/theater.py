from pydantic import BaseModel, Field
from typing import List, Optional
from datetime import datetime

class HallInfo(BaseModel):
    hall_id: int
    hall_name: str
    hall_api_id: str

class TheaterInfo(BaseModel):
    theater_id: int
    theater_name: Optional[str] = None
    theater_address: Optional[str] = None
    theater_latitude: Optional[float] = None
    theater_longitude: Optional[float] = None
    halls: List[HallInfo] = []

    class Config:
        from_attributes = True

class ShowInfo(BaseModel):
    show_detail_id: int
    show_detail_name: str
    show_detail_poster_image_path: str
    show_schedule_date_time: datetime
    show_runtime: int
    
    class Config:
        from_attributes = True

class HallCreate(BaseModel):
    hall_name: str = Field(..., example="신한카드홀")

class HallResponse(BaseModel):
    hall_id: int
    hall_name: str
    hall_api_id: str

    class Config:
        from_attributes = True

class TheaterWithHallsCreate(BaseModel):
    theater_name: str = Field(..., example="블루스퀘어")
    theater_address: str = Field(..., example="서울특별시 용산구 이태원로 294")
    halls: List[HallCreate] = Field(..., example=[{"hall_name": "신한카드홀"}, {"hall_name": "Mastercard Hall"}])

class TheaterWithHallsResponse(BaseModel):
    theater_id: int
    theater_name: str
    theater_address: str
    theater_latitude: float
    theater_longitude: float
    theater_api_id: str
    halls: List[HallResponse]

    class Config:
        from_attributes = True

class TheaterWithHallsUpdate(BaseModel):
    theater_name: Optional[str]
    theater_address: Optional[str]
    halls: Optional[List[HallCreate]]

class TheaterSearchRequest(BaseModel):
    region: Optional[str] = Field(None, example="서울특별시 강남구")
    theater_name: Optional[str] = Field(None, example="CGV 강남")
    hall_name: Optional[str] = Field(None, example="1관")
    show_detail_name: Optional[str] = Field(None, example="인셉션")