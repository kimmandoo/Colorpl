from pydantic import BaseModel, Field
from typing import List, Optional
from utils.enum import Category, Region, ShowState

class ShowDetailCreate(BaseModel):
    show_detail_api_id: str = Field(..., alias="SHOW_API_ID")
    show_detail_area: Region = Field(..., alias="SHOW_AREA")
    show_detail_cast: Optional[str] = Field(None, alias="SHOW_CAST")
    show_detail_category: Category = Field(..., alias="SHOW_GENRE")
    show_detail_name: str = Field(..., alias="SHOW_NAME")
    show_detail_poster_image_path: Optional[str] = Field(None, alias="SHOW_POSTER")
    show_detail_runtime: Optional[str] = Field(None, alias="SHOW_RUNTIME")
    show_detail_state: ShowState = Field(..., alias="SHOW_STATE")
    hall_id: int = Field(..., alias="hall_id")

    class Config:
        use_enum_values = True
        populate_by_name = True

class ShowDetailResponse(BaseModel):
    show_detail_id: int
    show_detail_api_id: str
    show_detail_area: Optional[Region] = None
    show_detail_cast: Optional[str] = None
    show_detail_category: Optional[Category] = None
    show_detail_name: str
    show_detail_poster_image_path: Optional[str] = None
    show_detail_runtime: Optional[str] = None
    show_detail_state: Optional[ShowState] = None
    hall_id: int

    class Config:
        from_attributes = True
        use_enum_values = True

class ShowDetailSearch(BaseModel):
    show_detail_api_id: Optional[str] = None
    show_detail_name: Optional[str] = None
    show_detail_area: Optional[Region] = None
    show_detail_state: Optional[ShowState] = None
    show_detail_category: Optional[Category] = None
    skip: Optional[int] = 0
    limit: Optional[int] = 10

    class Config:
        use_enum_values = True
        populate_by_name = True



