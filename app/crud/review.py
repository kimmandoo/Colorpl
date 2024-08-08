from sqlalchemy.orm import Session
from app.models import Review, Schedule, Member, Comment
from sqlalchemy import func
from app.schemas.review import ReviewSearch, ReviewUpdateDTO

def get_reviews_activity(db: Session, skip: int = 0, limit: int = 10):
    reviews = db.query(
        Review.review_id,
        Member.nickname,
        Review.create_date,
        Schedule.schedule_name,
        Review.is_spoiler,
        Schedule.schedule_category
    ).join(Schedule, Review.schedule_id == Schedule.schedule_id)\
     .join(Member, Schedule.member_id == Member.member_id)\
     .offset(skip).limit(limit).all()

    review_list = []
    for review in reviews:
        comments_count = db.query(func.count(Comment.comment_id)).filter(Comment.review_id == review.review_id).scalar()
        review_list.append({
            "review_id": review.review_id,
            "nickname": review.nickname,
            "create_date": review.create_date,
            "schedule_name": review.schedule_name,
            "is_spoiler": review.is_spoiler,
            "comments_count": comments_count,
            "schedule_category": review.schedule_category
        })

    return review_list

def get_review_by_id(db: Session, review_id: int):
    review = db.query(Review).filter(Review.review_id == review_id).first()
    if not review:
        return None
    comments = db.query(Comment).filter(Comment.review_id == review_id).all()
    return {
        "review_id": review.review_id,
        "review_filename": review.review_filename,
        "nickname": review.schedule.member.nickname,
        "email": review.schedule.member.email,
        "create_date": review.create_date,
        "update_date": review.update_date,
        "schedule_name": review.schedule.schedule_name,
        "schedule_category": review.schedule.schedule_category,
        "review_content": review.review_content,
        "review_emotion": review.review_emotion,
        "is_spoiler": review.is_spoiler,
        "comments": comments
    }

def update_review(db: Session, review_id: int, review_update: ReviewUpdateDTO):
    review = db.query(Review).filter(Review.review_id == review_id).first()
    if not review:
        return None
    update_data = review_update.dict(exclude_unset=True)
    for key, value in update_data.items():
        setattr(review, key, value)
    db.commit()
    db.refresh(review)
    comments = db.query(Comment).filter(Comment.review_id == review_id).all()
    return {
        "review_id": review.review_id,
        "review_filename": review.review_filename,
        "nickname": review.schedule.member.nickname,
        "email": review.schedule.member.email,
        "create_date": review.create_date,
        "update_date": review.update_date,
        "schedule_name": review.schedule.schedule_name,
        "schedule_category": review.schedule.schedule_category,
        "review_content": review.review_content,
        "review_emotion": review.review_emotion,
        "is_spoiler": review.is_spoiler,
        "comments": comments
    }

def search_reviews(db: Session, search: ReviewSearch, skip: int = 0, limit: int = 10):
    query = db.query(
        Review.review_id,
        Member.nickname,
        Review.create_date,
        Schedule.schedule_name,
        Review.is_spoiler,
        Schedule.schedule_category
    ).join(Schedule, Review.schedule_id == Schedule.schedule_id)\
     .join(Member, Schedule.member_id == Member.member_id)

    search_data = search.dict(exclude_unset=True)
    if "nickname" in search_data:
        query = query.filter(Member.nickname.ilike(f'%{search_data["nickname"]}%'))
    if "email" in search_data:
        query = query.filter(Member.email.ilike(f'%{search_data["email"]}%'))
    if "schedule_name" in search_data:
        query = query.filter(Schedule.schedule_name.ilike(f'%{search_data["schedule_name"]}%'))
    if "is_spoiler" in search_data:
        query = query.filter(Review.is_spoiler == search_data["is_spoiler"])
    if "schedule_category" in search_data:
        query = query.filter(Schedule.schedule_category == search_data["schedule_category"])

    reviews = query.offset(skip).limit(limit).all()

    review_list = []
    for review in reviews:
        comments_count = db.query(func.count(Comment.comment_id)).filter(Comment.review_id == review.review_id).scalar()
        review_list.append({
            "review_id": review.review_id,
            "nickname": review.nickname,
            "create_date": review.create_date,
            "schedule_name": review.schedule_name,
            "is_spoiler": review.is_spoiler,
            "comments_count": comments_count,
            "schedule_category": review.schedule_category
        })

    return review_list
