from pydantic import BaseModel
from typing import Optional, List
from datetime import date, datetime
from utils.enum import Category

class CommentDetail(BaseModel):
    comment_id: int
    content: str

    class Config:
        from_attributes = True

class ReviewDetail(BaseModel):
    review_id: int
    schedule_id: Optional[int]
    member_id: Optional[int]
    review_filename: Optional[str]
    nickname: Optional[str]
    email: Optional[str]
    create_date: Optional[datetime]
    update_date: Optional[datetime]
    schedule_name: Optional[str]
    schedule_category: Optional[Category]
    review_content: Optional[str]
    review_emotion: int = 0  # 기본값 0 설정
    is_spoiler: bool
    comments: List[CommentDetail] = []
    emphathy_num: int  # 추가된 필드

    class Config:
        from_attributes = True

class ReviewUpdateDTO(BaseModel):
    review_filename: Optional[str] = None
    review_content: Optional[str] = None
    review_emotion: Optional[int] = None
    is_spoiler: bool

    class Config:
        from_attributes = True

class ReviewSearch(BaseModel):
    member_id: Optional[int] = None
    schedule_id: Optional[int] = None
    nickname: Optional[str] = None
    create_date_from: Optional[date] = None 
    create_date_to: Optional[date] = None
    schedule_name: Optional[str] = None
    is_spoiler: Optional[bool] = None
    schedule_category: Optional[Category] = None
    skip: Optional[int] = 0
    limit: Optional[int] = 10

    class Config:
        from_attributes = True

class ReviewActivity(BaseModel):
    review_id: int
    schedule_id: int
    member_id: int
    nickname: str
    create_date: datetime
    schedule_name: str
    is_spoiler: bool
    comments_count: int
    schedule_category: Category
    review_emotion: int = 0  # 기본값 0 설정
    emphathy_num: int  # 추가된 필드

    class Config:
        from_attributes = True