from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.database import get_db
from app.schemas.review import ReviewDetail, ReviewUpdateDTO, ReviewSearch, ReviewActivity
from app.crud import review as crud_review
from typing import List

router = APIRouter()

@router.get("/reviews/activity", response_model=List[ReviewActivity])
def get_reviews_activity(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    reviews = crud_review.get_reviews_activity(db, skip=skip, limit=limit)
    return reviews

@router.get("/reviews/{review_id}", response_model=ReviewDetail)
def get_review_by_id(review_id: int, db: Session = Depends(get_db)):
    review = crud_review.get_review_by_id(db, review_id)
    if not review:
        raise HTTPException(status_code=404, detail="Review not found")
    return review

@router.patch("/reviews/{review_id}", response_model=ReviewDetail)
def update_review(review_id: int, review_update: ReviewUpdateDTO, db: Session = Depends(get_db)):
    updated_review = crud_review.update_review(db, review_id, review_update)
    
    if not updated_review:
        raise HTTPException(status_code=404, detail="Review not found")
    
    return updated_review


@router.post("/reviews/search", response_model=List[ReviewActivity])
def search_reviews(search: ReviewSearch, skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    reviews = crud_review.search_reviews(db, search, skip=skip, limit=limit)
    return reviews
