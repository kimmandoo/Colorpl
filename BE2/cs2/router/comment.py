from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List, Optional
from pydantic import BaseModel
from cs2 import crud, schemas
from cs2.database import SessionLocal
from common.dependencies import admin_only, chief_or_super_admin

router = APIRouter()

class CommentSearchRequest(BaseModel):
    email: Optional[str] = None
    nickname: Optional[str] = None
    category: Optional[str] = None
    content: Optional[str] = None
    skip: int = 0
    limit: int = 10

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/comments/search", response_model=List[schemas.Comment], dependencies=[Depends(admin_only)])
def search_comments(request: CommentSearchRequest, db: Session = Depends(get_db)):
    comments = crud.search_comments(db, email=request.email, nickname=request.nickname, category=request.category, content=request.content, skip=request.skip, limit=request.limit)
    return comments

@router.get("/comments/", response_model=List[schemas.Comment], dependencies=[Depends(admin_only)])
def read_comments(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    comments = crud.get_comments_all(db, skip=skip, limit=limit)
    return comments

@router.get("/comments/{comment_id}", response_model=schemas.Comment, dependencies=[Depends(admin_only)])
def read_comment(comment_id: int, db: Session = Depends(get_db)):
    db_comment = crud.get_comment(db, comment_id=comment_id)
    if db_comment is None:
        raise HTTPException(status_code=404, detail="Comment not found")
    return db_comment

@router.get("/comments/{comment_id}/activity", response_model=schemas.CommentActivity, dependencies=[Depends(chief_or_super_admin)])
def read_comment_activity(comment_id: int, db: Session = Depends(get_db)):
    comment_activity = crud.get_comment_activity(db, comment_id=comment_id)
    if comment_activity is None:
        raise HTTPException(status_code=404, detail="Comment not found")
    return comment_activity
