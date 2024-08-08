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
    categories: Optional[List[str]] = []
    schedules_count: Optional[int] = 0
    reviews_count: Optional[int] = 0
    comments_count: Optional[int] = 0
    reservations_count: Optional[int] = 0
    type: Optional[str] = None

    class Config:
        from_attributes = True

class MemberSearch(BaseModel):
    nickname: Optional[str] = None
    email: Optional[str] = None
    create_date_from: Optional[datetime] = None
    create_date_to: Optional[datetime] = None
    category: Optional[str] = None
