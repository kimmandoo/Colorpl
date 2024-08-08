from sqlalchemy.orm import Session
from app.models import Comment, Member, Review
from sqlalchemy import func
from app.schemas.comment import CommentActivity, CommentSearch, CommentUpdate

def get_comments_activity(db: Session, skip: int = 0, limit: int = 10):
    comments = db.query(
        Comment.comment_id,
        Member.nickname,
        Comment.create_date,
        Comment.content,
        func.ifnull(func.locate('규정 위반 댓글입니다', Comment.content), 0).label('is_inappropriate')
    ).join(Member, Comment.member_id == Member.member_id)\
     .offset(skip).limit(limit).all()

    return [
        {
            "comment_id": comment.comment_id,
            "nickname": comment.nickname,
            "create_date": comment.create_date,
            "content": comment.content[:50],  # 글자 제한
            "is_inappropriate": comment.is_inappropriate > 0
        }
        for comment in comments
    ]

def get_comment_by_id(db: Session, comment_id: int):
    return db.query(Comment).filter(Comment.comment_id == comment_id).first()

def update_comment(db: Session, comment_id: int, comment_update: CommentUpdate):
    comment = db.query(Comment).filter(Comment.comment_id == comment_id).first()
    if not comment:
        return None

    update_data = comment_update.dict(exclude_unset=True)
    for key, value in update_data.items():
        setattr(comment, key, value)
    db.commit()
    db.refresh(comment)
    return comment

def search_comments(db: Session, search: CommentSearch, skip: int = 0, limit: int = 10):
    query = db.query(
        Comment.comment_id,
        Member.nickname,
        Member.email,
        Comment.create_date,
        Comment.update_date,
        Comment.content,
        Review.review_id,
        func.ifnull(func.locate('규정 위반 댓글입니다', Comment.content), 0).label('is_inappropriate')
    ).join(Member, Comment.member_id == Member.member_id)\
     .join(Review, Comment.review_id == Review.review_id)

    if search.nickname:
        query = query.filter(Member.nickname.ilike(f'%{search.nickname}%'))
    if search.email:
        query = query.filter(Member.email.ilike(f'%{search.email}%'))
    if search.create_date_from:
        query = query.filter(Comment.create_date >= search.create_date_from)
    if search.create_date_to:
        query = query.filter(Comment.create_date <= search.create_date_to)
    if search.review_id:
        query = query.filter(Comment.review_id == search.review_id)
    if search.is_inappropriate is not None:
        if search.is_inappropriate:
            query = query.filter(Comment.content.like('%규정 위반 댓글입니다%'))
        else:
            query = query.filter(~Comment.content.like('%규정 위반 댓글입니다%'))

    comments = query.offset(skip).limit(limit).all()

    return [
        {
            "comment_id": comment.comment_id,
            "nickname": comment.nickname,
            "email": comment.email,
            "create_date": comment.create_date,
            "update_date": comment.update_date,
            "content": comment.content,
            "review_id": comment.review_id,
            "is_inappropriate": comment.is_inappropriate > 0
        }
        for comment in comments
    ]
