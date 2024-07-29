# Buff
from pydantic import BaseModel
from typing import Optional
from datetime import datetime

# Member
class MemberBase(BaseModel):
    email: str
    nickname: str

class Member(MemberBase):
    member_id: int
    created_date: datetime
    modified_date: datetime
    is_deleted: bool

    class Config:
        from_attributes = True

# Review
class ReviewBase(BaseModel):
    schedule_id: int
    member_id: int
    content: str
    is_spoiler: bool

class ReviewUpdate(BaseModel):
    is_spoiler: Optional[bool] = None

class Review(ReviewBase):
    review_id: int
    created_date: datetime
    modified_date: datetime
    is_deleted: bool

    class Config:
        from_attributes = True

# Comment
class CommentBase(BaseModel):
    review_id: int
    member_id: int
    content: str

class CommentUpdate(BaseModel):
    content: Optional[str] = None

class Comment(CommentBase):
    comment_id: int
    created_date: datetime
    modified_date: datetime
    is_deleted: bool

    class Config:
        from_attributes = True

# Category
class CategoryBase(BaseModel):
    category_name: str

class Category(CategoryBase):
    category_id: int

    class Config:
        from_attributes = True

class UserCategoryBase(BaseModel):
    member_id: int
    category_id: int

class UserCategory(UserCategoryBase):
    pass

    class Config:
        from_attributes = True

# Log
class ManagementLogBase(BaseModel):
    management_category: int
    # 1. 사용자, 2. 리뷰, 3. 댓글
    member_id: int
    management_action: int
    # 1. 삭제, 2. 수정
    management_by: str
    management_reason: Optional[str] = None

class ManagementLog(ManagementLogBase):
    id: int
    managed_at: datetime

    class Config:
        from_attributes = True