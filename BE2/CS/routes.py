from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import Optional
from . import schemas, crud
from .database import get_db
from common.dependencies import chief_or_super_admin, admin_only, super_admin_only
from auth.schemas import Administrator

router = APIRouter()

# member
@router.get('/members/', response_model=dict)
def read_members(skip: int = 0, limit: int = 100, nickname: Optional[str] = Query(None), db: Session = Depends(get_db), current_administrator: Administrator = Depends(super_admin_only)):
    if nickname:
        db_members = crud.get_member(db, nickname)
        total_count = len(db_members)
    else:
        db_members, total_count = crud.get_members(db, skip=skip, limit=limit)
    if not db_members:
        raise HTTPException(status_code=404, detail="Users not found")
    return {"data": db_members, "total": total_count}

@router.put('/members/{member_id}/status', response_model=schemas.Member)
def update_member_status(member_id: int, member_update: schemas.MemberUpdateStatus, db: Session = Depends(get_db), current_administrator: Administrator = Depends(super_admin_only)):
    db_member = crud.manage_member(db, member_id, is_deleted=member_update.is_deleted, management_by=current_administrator.email, management_reason=member_update.management_reason, ban_days=member_update.ban_days, ban_hours=member_update.ban_hours)
    if db_member is None:
        raise HTTPException(status_code=404, detail="User not found")
    return db_member

# review
@router.get('/reviews/{review_id}', response_model=schemas.Review)
def read_review(review_id: int, db: Session = Depends(get_db), current_administrator: Administrator = Depends(chief_or_super_admin)):
    db_review = crud.get_review(db, review_id)
    if db_review is None:
        raise HTTPException(status_code=404, detail="Review not found")
    return db_review

@router.put('/reviews/{review_id}', response_model=schemas.Review)
def spoiler_review(review_id: int, review_update: schemas.ReviewUpdate, management_reason: str, db: Session = Depends(get_db), current_administrator: Administrator = Depends(chief_or_super_admin)):
    db_review = crud.update_review(db, review_id, review_update, management_by=current_administrator.email, management_reason=management_reason)
    if db_review is None:
        raise HTTPException(status_code=404, detail="Review not found")
    return db_review

# comment

@router.get('/comments/{comment_id}', response_model=schemas.Comment)
def read_comment(comment_id: int, db: Session = Depends(get_db), current_administrator: Administrator = Depends(admin_only)):
    db_comment = crud.get_comment(db, comment_id)
    if db_comment is None:
        raise HTTPException(status_code=404, detail="Comment not found")
    return db_comment

@router.put('/comments/{comment_id}', response_model=schemas.Comment)
def update_comment(comment_id: int, comment_update: schemas.CommentUpdate, management_reason: str, db: Session = Depends(get_db), current_administrator: Administrator = Depends(admin_only)):
    db_comment = crud.update_comment(db, comment_id, comment_update, management_by=current_administrator.email, management_reason=management_reason)
    if db_comment is None:
        raise HTTPException(status_code=404, detail="Comment not found")
    return db_comment