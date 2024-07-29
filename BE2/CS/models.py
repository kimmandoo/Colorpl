from sqlalchemy import Column, Integer, String, Boolean, DateTime, ForeignKey, BigInteger, VARCHAR, Text, SMALLINT
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from .database import Base

class BaseEntity(Base):
    __abstract__ = True
    created_date = Column(DateTime, default=func.now())
    modified_date = Column(DateTime, default=func.now(), onupdate=func.now())
    is_deleted = Column(Boolean, default=False)

class Member(BaseEntity):
    __tablename__ = 'member'
    member_id = Column(Integer, primary_key=True, index=True)
    email = Column(String(255), unique=True, nullable=False)
    nickname = Column(String(100), nullable=False)
    schedules = relationship("Schedule", back_populates="member")
    reviews = relationship("Review", back_populates="member")
    comments = relationship("Comment", back_populates="member")
    categories = relationship("UserCategory", back_populates="member")

class Schedule(BaseEntity):
    __tablename__ = 'schedule'
    schedule_id = Column(BigInteger, primary_key=True, index=True)
    member_id = Column(BigInteger, ForeignKey('member.member_id'), nullable=True)
    member = relationship("Member", back_populates="schedules")
    reviews = relationship("Review", back_populates="schedule")

class Review(BaseEntity):
    __tablename__ = 'review'
    review_id = Column(BigInteger, primary_key=True, index=True)
    schedule_id = Column(BigInteger, ForeignKey('schedule.schedule_id'), nullable=False)
    member_id = Column(BigInteger, ForeignKey('member.member_id'), nullable=False)
    content = Column(VARCHAR(255), nullable=False)
    is_spoiler = Column(Boolean, nullable=False)
    schedule = relationship("Schedule", back_populates="reviews")
    member = relationship("Member", back_populates="reviews")
    comments = relationship("Comment", back_populates="review")

class Comment(BaseEntity):
    __tablename__ = 'comment'
    comment_id = Column(BigInteger, primary_key=True, index=True)
    review_id = Column(BigInteger, ForeignKey('review.review_id'), nullable=False)
    member_id = Column(BigInteger, ForeignKey('member.member_id'), nullable=False)
    content = Column(VARCHAR(255), nullable=False)
    review = relationship("Review", back_populates="comments")
    member = relationship("Member", back_populates="comments")

class Category(Base):
    __tablename__ = 'category'
    category_id = Column(Integer, primary_key=True, nullable=False)
    category_name = Column(String(64), nullable=False)
    users = relationship("UserCategory", back_populates="category")

class UserCategory(Base):
    __tablename__ = 'user_category'
    member_id = Column(Integer, ForeignKey('member.member_id'), primary_key=True, nullable=False)
    category_id = Column(Integer, ForeignKey('category.category_id'), primary_key=True, nullable=False)
    member = relationship("Member", back_populates="categories")
    category = relationship("Category", back_populates="users")

class ManagementLog(Base):
    __tablename__ = 'management_log'
    id = Column(Integer, primary_key=True, index=True)
    management_category = Column(SMALLINT, nullable=False)
    # 1. 사용자, 2. 리뷰, 3. 댓글
    member_id = Column(Integer, nullable=False)
    management_action = Column(SMALLINT, nullable=False)
    # 1. 삭제, 2. 수정
    management_by = Column(String(200), nullable=False)
    # 관리자 이메일
    management_reason = Column(String(255), nullable=True)
    managed_at = Column(DateTime, default=func.now())