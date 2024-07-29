# Buff
from sqlalchemy.orm import Session
from typing import Optional
from . import models, schemas

# Members
def get_member(db: Session, nickname: str):
    return db.query(models.Member).filter(models.Member.nickname == nickname).all()

def get_members(db: Session, skip: int=0, limit: int=100):
    members = db.query(models.Member).offset(skip).limit(limit).all()
    total_count = db.query(models.Member).count()
    return members, total_count

def manage_member(db: Session, member_id: int, management_by: str, management_reason: str):
    member = db.query(models.Member).filter(models.Member.member_id == member_id).first()
    if member:
        db.delete(member)
        db.commit()
        log = models.ManagementLog(
            manage_category=1,
            member_id=member_id,
            management_action=1,
            management_by=management_by,
            management_reason=management_reason,
        )
        db.add(log)
        db.commit()
    return member

# Reviews
def get_review(db: Session, review_id: Optional[int] = None, nickname: Optional[str] = None):
    if review_id is not None:
        return db.query(models.Review).filter(models.Review.review_id == review_id).first()
    elif nickname is not None:
        return db.query(models.Review).join(models.Member).filter(models.Member.nickname == nickname).all()
    else:
        return None
    
def get_reviews(db: Session, skip: int = 0, limit: int = 100):
    reviews = db.query(models.Review).offset(skip).limit(limit).all()
    total_count = db.query(models.Review).count()
    return reviews, total_count

def update_review(db: Session, review_id: int, review_update: schemas.ReviewUpdate, management_by: str, management_reason: str):
    review = db.query(models.Review).filter(models.Review.review_id == review_id).first()
    if review:
        update_data = review_update.dict(exclude_unset=True)
        for key, value in update_data.items():
            setattr(review, key, value)
        db.commit()
        db.refresh(review)
        log = models.ManagementLog(
            management_category = 2,
            member_id=review.member_id,
            management_action=2,
            management_by=management_by,
            management_reason=management_reason
        )
        db.add(log)
        db.commit()
    return review

# Comments
def get_comment(db: Session, comment_id: int):
    return db.query(models.Comment).filter(models.Comment.comment_id == comment_id).first()

def get_comments(db: Session, skip: int = 0, limit: int = 10):
    comments = db.query(models.Comment).offset(skip).limit(limit).all()
    total_count = db.query(models.Comment).count()
    return comments, total_count

def update_comment(db: Session, comment_id: int, comment_update: schemas.CommentUpdate, management_by: str, management_reason: str):
    comment = db.query(models.Comment).filter(models.Comment.comment_id == comment_id).first()
    if comment:
        update_data = comment_update.dict(exclude_unset=True)
        for key, value in update_data.items():
            setattr(comment, key, value)
        db.commit()
        db.refresh(comment)
        log = models.ManagementLog(
            management_category=3,
            member_id=comment.member_id,
            management_action=2,
            management_by=management_by,
            management_reason=management_reason
        )
        db.add(log)
        db.commit()
    return comment