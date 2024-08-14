from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.crud import comment as crud_comment
from app.schemas.comment import CommentActivity, CommentDetail, CommentUpdate, CommentSearch
from app.database import get_db
from typing import List

router = APIRouter()

@router.get("/comments/activity", response_model=List[CommentActivity])
def get_comments_activity(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    comments = crud_comment.get_comments_activity(db, skip=skip, limit=limit)
    return comments

@router.get("/comments/{comment_id}", response_model=CommentDetail)
def get_comment_by_id(comment_id: int, db: Session = Depends(get_db)):
    comment = crud_comment.get_comment_by_id(db, comment_id)
    if comment is None:
        raise HTTPException(status_code=404, detail="Comment not found")
    return comment

@router.patch("/comments/{comment_id}", response_model=CommentDetail)
def update_comment(comment_id: int, comment_update: CommentUpdate, db: Session = Depends(get_db)):
    comment = crud_comment.update_comment(db, comment_id, comment_update)
    if comment is None:
        raise HTTPException(status_code=404, detail="Comment not found")
    return comment

@router.post("/comments/search", response_model=List[CommentDetail])
def search_comments(search: CommentSearch, skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    comments = crud_comment.search_comments(db, search, skip=skip, limit=limit)
    return comments
