from datetime import timedelta
from typing import List
from fastapi import Depends, HTTPException
from sqlalchemy.orm import Session
from app.database import get_db
from app.models import Comment, Member, Review
from sqlalchemy import func
from app.schemas.comment import CommentActivity, CommentDetail, CommentSearch, CommentUpdate

def get_comments_activity(db: Session, skip: int = 0, limit: int = 10) -> List[CommentActivity]:
    comments = db.query(
        Comment.comment_id,
        Comment.review_id,
        Comment.member_id,
        Member.nickname,
        Comment.create_date,
        Comment.content,
        func.ifnull(func.locate('규정 위반 댓글입니다', Comment.content), 0).label('is_inappropriate')
    ).join(Member, Comment.member_id == Member.member_id)\
     .offset(skip).limit(limit).all()

    return [
        CommentActivity(
            comment_id=comment.comment_id,
            review_id=comment.review_id,
            member_id=comment.member_id,
            nickname=comment.nickname,
            create_date=comment.create_date,
            content=comment.content[:50],  # 글자 제한
            is_inappropriate=comment.is_inappropriate > 0
        )
        for comment in comments
    ]

def get_comment_by_id(db: Session, comment_id: int) -> CommentDetail:
    comment = db.query(Comment).filter(Comment.comment_id == comment_id).first()
    if not comment:
        raise HTTPException(status_code=404, detail="Comment not found")

    member = db.query(Member).filter(Member.member_id == comment.member_id).first()
    review = db.query(Review).filter(Review.review_id == comment.review_id).first()

    return CommentDetail(
        comment_id=comment.comment_id,
        review_id=comment.review_id,
        member_id=comment.member_id,
        nickname=member.nickname if member else "Unknown",
        email=member.email if member else "Unknown",
        create_date=comment.create_date,
        update_date=comment.update_date,
        content=comment.content,
        review=review.review_content if review else None,
        is_inappropriate='규정 위반 댓글입니다' in comment.content
    )

# 댓글 업데이트
def update_comment(comment_id: int, comment_update: CommentUpdate, db: Session = Depends(get_db)) -> CommentDetail:
    comment = db.query(Comment).filter(Comment.comment_id == comment_id).first()
    if not comment:
        raise HTTPException(status_code=404, detail="Comment not found")

    update_data = comment_update.dict(exclude_unset=True)
    for key, value in update_data.items():
        setattr(comment, key, value)

    db.commit()
    db.refresh(comment)

    member = db.query(Member).filter(Member.member_id == comment.member_id).first()
    review = db.query(Review).filter(Review.review_id == comment.review_id).first()

    return CommentDetail(
        comment_id=comment.comment_id,
        review_id=comment.review_id,
        member_id=comment.member_id,
        nickname=member.nickname if member else "Unknown",
        email=member.email if member else "Unknown",
        create_date=comment.create_date,
        update_date=comment.update_date,
        content=comment.content,
        review=review.review_content if review else None,
        is_inappropriate='규정 위반 댓글입니다' in comment.content
    )

# 댓글 검색
def search_comments(db: Session, search: CommentSearch, skip: int = 0, limit: int = 10):    
    query = db.query(
        Comment.comment_id,
        Comment.review_id,
        Comment.member_id,
        Member.nickname,
        Member.email,
        Comment.create_date,
        Comment.update_date,
        Comment.content,
        Review.review_id,
        func.ifnull(func.locate('규정 위반 댓글입니다', Comment.content), 0).label('is_inappropriate')
    ).join(Member, Comment.member_id == Member.member_id)\
     .join(Review, Comment.review_id == Review.review_id)
    
    search_data = search.dict(exclude_unset=True)

    if "member_id" in search_data:
        query = query.filter(Comment.member_id == search_data["member_id"])

    if "review_id" in search_data:
        query = query.filter(Comment.review_id == search_data["review_id"])

    if "nickname" in search_data:
        query = query.filter(Member.nickname.ilike(f'%{search_data["nickname"]}%'))

    if "email" in search_data:
        query = query.filter(Member.email.ilike(f'%{search_data["email"]}%'))

    if "content" in search_data:
        query = query.filter(Comment.content.ilike(f'%{search_data["content"]}%'))

    if "create_date_from" in search_data and "create_date_to" in search_data:
        if search_data["create_date_from"] == search_data["create_date_to"]:
            start_of_day = search_data["create_date_from"]
            end_of_day = start_of_day + timedelta(days=1)
            query = query.filter(Comment.create_date.between(start_of_day, end_of_day))
        else:
            query = query.filter(
                Comment.create_date.between(
                    search_data["create_date_from"],
                    search_data["create_date_to"]
                )
            )
    elif "create_date_from" in search_data:
        query = query.filter(Comment.create_date >= search_data["create_date_from"])
    elif "create_date_to" in search_data:
        query = query.filter(Comment.create_date <= search_data["create_date_to"])

    if "is_inappropriate" in search_data:
        if search_data["is_inappropriate"]:
            query = query.filter(Comment.content.like('%규정 위반 댓글입니다%'))
        else:
            query = query.filter(~Comment.content.like('%규정 위반 댓글입니다%'))

    comments = query.offset(skip).limit(limit).all()

    comment_list = []
    for comment in comments:
        comment_list.append({
            "comment_id": comment.comment_id,
            "review_id": comment.review_id,
            "member_id": comment.member_id,
            "nickname": comment.nickname,
            "email": comment.email,
            "create_date": comment.create_date,
            "update_date": comment.update_date,
            "content": comment.content,
            "is_inappropriate": comment.is_inappropriate > 0
        })

    return comment_list