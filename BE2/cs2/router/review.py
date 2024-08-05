from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List, Optional
from pydantic import BaseModel
from cs2 import crud, schemas
from cs2.database import SessionLocal
from common.dependencies import chief_or_super_admin, super_admin_only

router = APIRouter()

class ReviewSearchRequest(BaseModel):
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

@router.post("/reviews/search", response_model=List[schemas.Review], dependencies=[Depends(chief_or_super_admin)])
def search_reviews(request: ReviewSearchRequest, db: Session = Depends(get_db)):
    reviews = crud.search_reviews(db, email=request.email, nickname=request.nickname, category=request.category, content=request.content, skip=request.skip, limit=request.limit)
    return reviews

@router.get("/reviews/", response_model=List[schemas.Review], dependencies=[Depends(chief_or_super_admin)])
def read_reviews(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    reviews = crud.get_reviews_all(db, skip=skip, limit=limit)
    return reviews

@router.get("/reviews/{review_id}", response_model=schemas.Review, dependencies=[Depends(chief_or_super_admin)])
def read_review(review_id: int, db: Session = Depends(get_db)):
    db_review = crud.get_review(db, review_id=review_id)
    if db_review is None:
        raise HTTPException(status_code=404, detail="Review not found")
    return db_review

@router.get("/reviews/{review_id}/activity", response_model=schemas.ReviewActivity, dependencies=[Depends(super_admin_only)])
def read_review_activity(review_id: int, skip: int = 0, limit: int = 5, db: Session = Depends(get_db)):
    review_activity = crud.get_review_activity(db, review_id=review_id, skip=skip, limit=limit)
    if review_activity is None:
        raise HTTPException(status_code=404, detail="Review not found")
    return review_activity
