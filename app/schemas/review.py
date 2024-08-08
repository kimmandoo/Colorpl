from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class CommentDetail(BaseModel):
    comment_id: int
    content: str

    class Config:
        from_attributes = True

class ReviewDetail(BaseModel):
    review_id: int
    review_filename: Optional[str]
    nickname: Optional[str]
    email: Optional[str]
    create_date: Optional[datetime]
    update_date: Optional[datetime]
    schedule_name: Optional[str]
    schedule_category: Optional[int]
    review_content: Optional[str]
    review_emotion: Optional[int]
    is_spoiler: Optional[bool]
    comments: List[CommentDetail] = []

    class Config:
        from_attributes = True

class ReviewUpdateDTO(BaseModel):
    review_filename: Optional[str]
    review_content: Optional[str]
    review_emotion: Optional[int]
    is_spoiler: Optional[bool]

    class Config:
        from_attributes = True

class ReviewSearch(BaseModel):
    nickname: Optional[str] = None
    email: Optional[str] = None
    schedule_name: Optional[str] = None
    is_spoiler: Optional[bool] = None
    schedule_category: Optional[int] = None

    class Config:
        from_attributes = True

class ReviewActivity(BaseModel):
    review_id: int
    nickname: str
    create_date: datetime
    schedule_name: str
    is_spoiler: bool
    comments_count: int
    schedule_category: int

    class Config:
        from_attributes = True