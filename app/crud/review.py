from sqlalchemy.orm import Session
from app.models import Review, Schedule, Member, Comment, Empathy
from sqlalchemy import func
from app.schemas.review import ReviewSearch, ReviewUpdateDTO

def get_reviews_activity(db: Session, skip: int = 0, limit: int = 10):
    reviews = db.query(
        Review.review_id,
        Review.schedule_id,
        Member.member_id,
        Member.nickname,
        Review.create_date,
        Schedule.schedule_name,
        Review.is_spoiler,
        Schedule.schedule_category,
        func.coalesce(Review.review_emotion, 0).label('review_emotion'),  # 기본값 설정
        func.count(Empathy.review_review_id).label("emphathy_num")
    ).join(Schedule, Review.schedule_id == Schedule.schedule_id)\
     .join(Member, Schedule.member_id == Member.member_id)\
     .outerjoin(Empathy, Review.review_id == Empathy.review_review_id)\
     .group_by(Review.review_id, Review.schedule_id, Member.member_id, Member.nickname, Review.create_date, Schedule.schedule_name, Review.is_spoiler, Schedule.schedule_category)\
     .offset(skip).limit(limit).all()

    review_list = []
    for review in reviews:
        comments_count = db.query(func.count(Comment.comment_id)).filter(Comment.review_id == review.review_id).scalar()
        review_list.append({
            "review_id": review.review_id,
            "schedule_id": review.schedule_id,
            "member_id": review.member_id,
            "nickname": review.nickname,
            "create_date": review.create_date,
            "schedule_name": review.schedule_name,
            "is_spoiler": review.is_spoiler,
            "comments_count": comments_count,
            "schedule_category": review.schedule_category,
            "review_emotion": review.review_emotion,
            "emphathy_num": review.emphathy_num
        })

    return review_list

def get_review_by_id(db: Session, review_id: int):
    # 리뷰와 관련된 데이터를 함께 조회
    review = db.query(
        Review.review_id,
        Review.schedule_id,
        Review.review_emotion,
        Review.review_content,
        Review.review_filename,
        Review.is_spoiler,
        Review.schedule_id,
        Review.create_date,
        Review.update_date,
        func.coalesce(func.count(Empathy.review_review_id), 0).label('emphathy_num')  # 공감 수를 계산하며 Null 처리
    ).outerjoin(Empathy, Review.review_id == Empathy.review_review_id)\
     .filter(Review.review_id == review_id)\
     .group_by(Review.review_id, Review.schedule_id)\
     .first()

    if not review:
        return None

    # Schedule과 Member는 LEFT JOIN이므로, None일 가능성을 고려해야 합니다.
    schedule = db.query(Schedule).filter(Schedule.schedule_id == review.schedule_id).first()
    if schedule:
        member = db.query(Member).filter(Member.member_id == schedule.member_id).first()
    else:
        member = None

    comments = db.query(Comment).filter(Comment.review_id == review.review_id).all()

    return {
        "review_id": review.review_id,
        "schedule_id": review.schedule_id,
        "member_id": member.member_id if member else None,
        "review_filename": review.review_filename,
        "nickname": member.nickname if member else None,  # member가 None일 경우 처리
        "email": member.email if member else None,  # member가 None일 경우 처리
        "create_date": review.create_date,
        "update_date": review.update_date,
        "schedule_name": schedule.schedule_name if schedule else None,  # schedule이 None일 경우 처리
        "schedule_category": schedule.schedule_category if schedule else None,  # schedule이 None일 경우 처리
        "review_content": review.review_content,
        "review_emotion": review.review_emotion,
        "is_spoiler": bool(review.is_spoiler),
        "comments": [Comment(comment_id=c.comment_id, content=c.content) for c in comments],
        "emphathy_num": review.emphathy_num  # 동적으로 계산된 empathy_num
    }

def update_review(db: Session, review_id: int, review_update: ReviewUpdateDTO):
    # Update review based on provided data
    review = db.query(Review).filter(Review.review_id == review_id).first()
    
    if not review:
        return None
    
    update_data = review_update.dict(exclude_unset=True)
    for key, value in update_data.items():
        setattr(review, key, value)
    
    db.commit()
    db.refresh(review)
    
    # Update 후 get_review_by_id를 사용하여 최신 상태를 반환
    updated_review = get_review_by_id(db, review_id)
    
    return updated_review

def search_reviews(db: Session, search: ReviewSearch, skip: int = 0, limit: int = 10):
    query = db.query(
        Review.review_id,
        Review.schedule_id,
        Member.member_id,
        Member.nickname,
        Review.create_date,
        Schedule.schedule_name,
        Review.is_spoiler,
        Schedule.schedule_category,
        func.coalesce(Review.review_emotion, 0).label('review_emotion'),  # 기본값 설정
        func.count(Empathy.review_review_id).label("emphathy_num")
    ).join(Schedule, Review.schedule_id == Schedule.schedule_id)\
     .join(Member, Schedule.member_id == Member.member_id)\
     .outerjoin(Empathy, Review.review_id == Empathy.review_review_id)\
     .group_by(Review.review_id, Review.schedule_id, Member.member_id, Member.nickname, Review.create_date, Schedule.schedule_name, Review.is_spoiler, Schedule.schedule_category)

    search_data = search.dict(exclude_unset=True)

    if "member_id" in search_data:
        query = query.filter(Member.member_id == search_data["member_id"])
    
    if "schedule_id" in search_data:
        query = query.filter(Schedule.schedule_id == search_data["schedule_id"])

    if "nickname" in search_data:
        query = query.filter(Member.nickname.ilike(f'%{search_data["nickname"]}%'))

    if "create_date_from" in search_data and "create_date_to" in search_data:
        if search_data["create_date_from"] == search_data["create_date_to"]:
            # 같은 날짜인 경우, 하루 전체를 포함하도록 시간 범위 지정
            query = query.filter(
                Review.create_date.between(
                    f'{search_data["create_date_from"]} 00:00:00',
                    f'{search_data["create_date_to"]} 23:59:59'
                )
            )
        else:
            # 다른 날짜인 경우, 두 날짜 사이의 범위를 필터링
            query = query.filter(
                Review.create_date.between(
                    f'{search_data["create_date_from"]} 00:00:00',
                    f'{search_data["create_date_to"]} 23:59:59'
                )
            )
    elif "create_date_from" in search_data:
        query = query.filter(Review.create_date >= f'{search_data["create_date_from"]} 00:00:00')
    elif "create_date_to" in search_data:
        query = query.filter(Review.create_date <= f'{search_data["create_date_to"]} 23:59:59')

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
            "schedule_id": review.schedule_id,
            "member_id": review.member_id,
            "nickname": review.nickname,
            "create_date": review.create_date,
            "schedule_name": review.schedule_name,
            "is_spoiler": review.is_spoiler,
            "comments_count": comments_count,
            "schedule_category": review.schedule_category,
            "review_emotion": review.review_emotion,
            "emphathy_num": review.emphathy_num
        })

    return review_list

