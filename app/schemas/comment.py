from pydantic import BaseModel
from datetime import datetime
from typing import Optional

class CommentActivity(BaseModel):
    comment_id: int
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
    nickname: str
    email: str
    create_date: datetime
    update_date: datetime
    review: Optional[str] = None

    class Config:
        from_attributes = True

class CommentSearch(BaseModel):
    nickname: Optional[str] = None
    email: Optional[str] = None
    create_date_from: Optional[datetime] = None
    create_date_to: Optional[datetime] = None
    review_id: Optional[int] = None
    is_inappropriate: Optional[bool] = None
