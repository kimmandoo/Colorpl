from sqlalchemy.orm import Session
from typing import Optional, List, Tuple
from datetime import datetime, timedelta
from . import models, schemas

# Members
def get_member(db: Session, nickname: str) -> List[schemas.Member]:
    members = db.query(models.Member).filter(models.Member.nickname == nickname).all()
    return [schemas.Member.model_validate(member, from_attributes=True) for member in members]

def get_members(db: Session, skip: int = 0, limit: int = 100) -> Tuple[List[schemas.Member], int]:
    members = db.query(models.Member).offset(skip).limit(limit).all()
    total_count = db.query(models.Member).count()
    return [schemas.Member.model_validate(member, from_attributes=True) for member in members], total_count

def manage_member(db: Session, member_id: int, is_deleted: bool, management_by: str, management_reason: str, ban_duration: Optional[int] = None) -> Optional[schemas.Member]:
    member = db.query(models.Member).filter(models.Member.member_id == member_id).first()
    if member:
        member.is_deleted = is_deleted
        db.commit()

        action = 1 if is_deleted else 2
        ban_until = datetime.utcnow() + timedelta(days=ban_duration) if is_deleted and ban_duration else None

        log = models.ManagementLog(
            management_category=1,
            member_id=member_id,
            management_action=action,
            management_by=management_by,
            management_reason=management_reason,
            ban_until=ban_until,
        )
        db.add(log)
        db.commit()
    return schemas.Member.model_validate(member, from_attributes=True) if member else None

# Reviews
def get_review(db: Session, review_id: Optional[int] = None, nickname: Optional[str] = None) -> Optional[schemas.Review]:
    if review_id is not None:
        review = db.query(models.Review).filter(models.Review.review_id == review_id).first()
        return schemas.Review.model_validate(review, from_attributes=True) if review else None
    elif nickname is not None:
        reviews = db.query(models.Review).join(models.Member).filter(models.Member.nickname == nickname).all()
        return [schemas.Review.model_validate(review, from_attributes=True) for review in reviews]
    else:
        return None

def get_reviews(db: Session, skip: int = 0, limit: int = 100) -> Tuple[List[schemas.Review], int]:
    reviews = db.query(models.Review).offset(skip).limit(limit).all()
    total_count = db.query(models.Review).count()
    return [schemas.Review.model_validate(review, from_attributes=True) for review in reviews], total_count

def update_review(db: Session, review_id: int, review_update: schemas.ReviewUpdate, management_by: str, management_reason: str) -> Optional[schemas.Review]:
    review = db.query(models.Review).filter(models.Review.review_id == review_id).first()
    if review:
        update_data = review_update.dict(exclude_unset=True)
        for key, value in update_data.items():
            if key == 'member_id' and value is None:
                continue  # member_id가 None인 경우 건너뛰기
            setattr(review, key, value)
        db.commit()
        db.refresh(review)
        log = models.ManagementLog(
            management_category=2,
            member_id=review.member_id,
            management_action=2,
            management_by=management_by,
            management_reason=management_reason
        )
        db.add(log)
        db.commit()
    return schemas.Review.model_validate(review, from_attributes=True) if review else None

# Comments
def get_comment(db: Session, comment_id: int) -> Optional[schemas.Comment]:
    comment = db.query(models.Comment).filter(models.Comment.comment_id == comment_id).first()
    return schemas.Comment.model_validate(comment, from_attributes=True) if comment else None

def get_comments(db: Session, skip: int = 0, limit: int = 10) -> Tuple[List[schemas.Comment], int]:
    comments = db.query(models.Comment).offset(skip).limit(limit).all()
    total_count = db.query(models.Comment).count()
    return [schemas.Comment.model_validate(comment, from_attributes=True) for comment in comments], total_count

def update_comment(db: Session, comment_id: int, comment_update: schemas.CommentUpdate, management_by: str, management_reason: str) -> Optional[schemas.Comment]:
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
    return schemas.Comment.model_validate(comment, from_attributes=True) if comment else None


# 남은거: is_spoiler 변경하도록 되야하는데 안 되는 부분 수정
# delete는 side_effect 우려점을 고려하여 일시 차단 구현