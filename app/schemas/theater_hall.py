from pydantic import BaseModel, Field
from typing import List, Optional

class HallCreate(BaseModel):
    hall_api_id: str = Field(..., alias="HALL_API_NUM")
    hall_name: str = Field(..., alias="HALL_NAME")

class HallResponse(HallCreate):
    hall_id: int
    theater_id: int

    class Config:
        arbitrary_types_allowed = True
        populate_by_name = True

class TheaterCreate(BaseModel):
    theater_api_id: str = Field(..., alias="THEATER_API_ID")
    theater_name: str = Field(..., alias="THEATER_NAME")
    theater_address: str = Field(..., alias="THEATER_ADDRESS")
    theater_latitude: Optional[float] = Field(None, alias="THEATER_LATITUDE")
    theater_longitude: Optional[float] = Field(None, alias="THEATER_LONGITUDE")
    halls: List[HallCreate] = Field(..., alias="mt13s")

class TheaterUpdate(TheaterCreate):
    theater_id: int

class TheaterResponse(BaseModel):
    theater_id: int
    theater_api_id: str
    theater_name: str
    theater_address: str
    theater_latitude: Optional[float] = None
    theater_longitude: Optional[float] = None
    halls: List[HallResponse]

    class Config:
        arbitrary_types_allowed = True
        populate_by_name = True

class TheaterSearchRequest(BaseModel):
    theater_name: str
    skip: Optional[int] = 0
    limit: Optional[int] = 10
    