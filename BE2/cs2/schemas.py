from pydantic import BaseModel
from datetime import datetime
from typing import List, Optional

class Category(BaseModel):
    id: int
    name: str

    class Config:
        from_attributes = True

class Member(BaseModel):
    member_id: int
    email: str
    nickname: str
    profile: Optional[str]
    categories: List[Category] = []

    class Config:
        from_attributes = True

class TicketBase(BaseModel):
    ticket_name: str
    theater: str
    seat: str
    category: str

class Ticket(TicketBase):
    ticket_id: int
    member_id: int
    filepath: Optional[str]
    created_date: datetime
    modified_date: datetime
    owner: Member

    class Config:
        from_attributes = True


class Comment(BaseModel):
    comment_id: int
    review_id: int
    member_id: int
    # writer: str
    commentContent: str
    owner: Member

    class Config:
        from_attributes = True

class Review(BaseModel):
    review_id: int
    ticket_id: int
    member_id: int
    content: str
    spoiler: bool
    emotion: str
    owner: Member

    class Config:
        from_attributes = True

class MemberActivity(BaseModel):
    member: Member
    recent_tickets: List[Ticket] = []
    recent_reviews: List[Review] = []
    recent_comments: List[Comment] = []

    class Config:
        from_attributes = True

class ReviewActivity(BaseModel):
    review: Review
    ticket: Ticket
    owner: Member
    recent_comments: List[Comment] = []

    class Config:
        from_attributes = True

class CommentActivity(BaseModel):
    comment: Comment
    review: Review
    owner: Member

    class Config:
        from_attributes = True

class TicketActivity(BaseModel):
    ticket: Ticket
    owner: Member
    recent_reviews: List[Review] = []

    class Config:
        from_attributes = True
