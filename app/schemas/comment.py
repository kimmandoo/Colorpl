from pydantic import BaseModel
from datetime import datetime
from typing import Optional

class CommentActivity(BaseModel):
    comment_id: int
    review_id: int
    member_id: int
    nickname: str
    create_date: datetime
    content: str
    is_inappropriate: bool

    class Config:
        from_attributes = True

class CommentBase(BaseModel):
    content: Optional[str] = None

class CommentUpdate(CommentBase):
    pass

class CommentDetail(CommentBase):
    comment_id: int
    review_id: Optional[int]
    member_id: Optional[int]
    nickname: str
    email: str
    create_date: datetime
    update_date: datetime
    review: Optional[str] = None
    is_inappropriate: Optional[bool] = None

    class Config:
        from_attributes = True

class CommentSearch(BaseModel):
    member_id: Optional[int] = None
    review_id: Optional[int] = None
    nickname: Optional[str] = None
    email: Optional[str] = None
    content: Optional[str] = None
    create_date_from: Optional[datetime] = None
    create_date_to: Optional[datetime] = None
    is_inappropriate: Optional[bool] = None
    skip: Optional[int] = 0
    limit: Optional[int] = 10

    class Config:
        from_attributes = True
