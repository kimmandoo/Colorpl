from sqlalchemy import Column, Integer, String, DateTime, Boolean, ForeignKey, Table
from sqlalchemy.orm import relationship, declared_attr
from cs2.database import Base
import datetime

class BaseEntity:
    created_date = Column(DateTime, default=datetime.datetime.utcnow)
    modified_date = Column(DateTime, default=datetime.datetime.utcnow, onupdate=datetime.datetime.utcnow)
    is_deleted = Column(Boolean, default=False)

    @declared_attr
    def __tablename__(cls):
        return cls.__name__.lower()

user_category = Table(
    'user_category', Base.metadata,
    Column('user_id', Integer, ForeignKey('members.member_id'), primary_key=True),
    Column('category_id', Integer, ForeignKey('categories.id'), primary_key=True)
)

class Member(Base, BaseEntity):
    __tablename__ = "members"

    member_id = Column(Integer, primary_key=True, index=True)
    email = Column(String(255), unique=True, index=True)
    nickname = Column(String(50), unique=True, index=True)
    profile = Column(String(255))
    reviews = relationship("Review", back_populates="owner")
    comments = relationship("Comment", back_populates="owner")
    tickets = relationship("Ticket", back_populates="owner")
    categories = relationship("Category", secondary=user_category, back_populates="members")

class Ticket(Base, BaseEntity):
    __tablename__ = "tickets"

    ticket_id = Column(Integer, primary_key=True, index=True)
    filepath = Column(String(255))
    ticket_name = Column(String(100))
    theater = Column(String(100))
    seat = Column(String(10))
    category = Column(String(50))
    member_id = Column(Integer, ForeignKey("members.member_id"))
    reviews = relationship("Review", back_populates="ticket")

    owner = relationship("Member", back_populates="tickets")

class Review(Base, BaseEntity):
    __tablename__ = "reviews"

    review_id = Column(Integer, primary_key=True, index=True)
    ticket_id = Column(Integer, ForeignKey("tickets.ticket_id"))
    content = Column(String(1000))
    spoiler = Column(Boolean)
    emotion = Column(String(50))
    member_id = Column(Integer, ForeignKey("members.member_id"))
    comments = relationship("Comment", back_populates="review")

    owner = relationship("Member", back_populates="reviews")
    ticket = relationship("Ticket", back_populates="reviews")

class Comment(Base, BaseEntity):
    __tablename__ = "comments"

    comment_id = Column(Integer, primary_key=True, index=True)
    review_id = Column(Integer, ForeignKey("reviews.review_id"))
    member_id = Column(Integer, ForeignKey("members.member_id"))
    # writer = Column(String(50))
    commentContent = Column(String(1000))

    owner = relationship("Member", back_populates="comments")
    review = relationship("Review", back_populates="comments")

class Category(Base):
    __tablename__ = "categories"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(50), index=True)
    members = relationship("Member", secondary=user_category, back_populates="categories")
