from pydantic import BaseModel, validator
from typing import Optional, List
from datetime import datetime

class MemberDetail(BaseModel):
    member_id: int
    email: Optional[str] = None
    nickname: Optional[str] = None
    profile: Optional[str] = None
    type: Optional[str] = None
    create_date: Optional[datetime] = None
    update_date: Optional[datetime] = None

    class Config:
        from_attributes = True

class MemberUpdateDTO(BaseModel):
    email: Optional[str] = None
    nickname: Optional[str] = None
    profile: Optional[str] = None
    type: Optional[str] = None

    @validator('type')
    def validate_type(cls, value):
        if value not in ('ADMIN', 'USER'):
            raise ValueError('Invalid type')
        return value

class MemberActivity(BaseModel):
    member_id: int
    nickname: Optional[str] = None
    email: Optional[str] = None
    create_date: Optional[datetime] = None
    categories: List[str] = []
    schedules_count: int = 0 
    reviews_count: int = 0   
    comments_count: int = 0 
    reservations_count: int = 0
    type: Optional[str] = None

    class Config:
        from_attributes = True

class MemberSearch(BaseModel):
    member_id: Optional[int] = None
    nickname: Optional[str] = None
    email: Optional[str] = None
    create_date_from: Optional[datetime] = None
    create_date_to: Optional[datetime] = None
    category: Optional[List[str]] = None
