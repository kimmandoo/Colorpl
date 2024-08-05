from sqlalchemy.orm import Session
from cs2 import models
from typing import Optional

# Member

def get_member(db: Session, member_id: int):
    return db.query(models.Member).filter(models.Member.member_id == member_id).first()

def get_members(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Member).order_by(models.Member.modified_date.desc()).offset(skip).limit(limit).all()

def search_members(db: Session, email: Optional[str], nickname: Optional[str], category: Optional[str], skip: int = 0, limit: int = 10):
    query = db.query(models.Member)
    if email:
        query = query.filter(models.Member.email.ilike(f'%{email}%'))
    if nickname:
        query = query.filter(models.Member.nickname.ilike(f'%{nickname}%'))
    if category:
        query = query.join(models.Member.categories).filter(models.Category.name == category)
    return query.offset(skip).limit(limit).all()

def get_member_activity(db: Session, member_id: int, skip: int = 0, limit: int = 5):
    member = db.query(models.Member).filter(models.Member.member_id == member_id).first()
    if member:
        tickets = db.query(models.Ticket).filter(models.Ticket.member_id == member_id).order_by(models.Ticket.modified_date.desc()).offset(skip).limit(limit).all()
        reviews = db.query(models.Review).filter(models.Review.member_id == member_id).order_by(models.Review.modified_date.desc()).offset(skip).limit(limit).all()
        comments = db.query(models.Comment).filter(models.Comment.member_id == member_id).order_by(models.Comment.modified_date.desc()).offset(skip).limit(limit).all()
        return {
            "member": member,
            "recent_tickets": tickets,
            "recent_reviews": reviews,
            "recent_comments": comments
        }
    return None

# Ticket

def get_tickets(db: Session, member_id: int, skip: int = 0, limit: int = 100):
    return db.query(models.Ticket).filter(models.Ticket.member_id == member_id).order_by(models.Ticket.modified_date.desc()).offset(skip).limit(limit).all()

def get_ticket(db: Session, ticket_id: int):
    return db.query(models.Ticket).filter(models.Ticket.ticket_id == ticket_id).first()

def get_tickets_all(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Ticket).order_by(models.Ticket.modified_date.desc()).offset(skip).limit(limit).all()

def get_ticket_activity(db: Session, ticket_id: int, skip: int = 0, limit: int = 5):
    ticket = db.query(models.Ticket).filter(models.Ticket.ticket_id == ticket_id).first()
    if ticket:
        owner = db.query(models.Member).filter(models.Member.member_id == ticket.member_id).first()
        reviews = db.query(models.Review).filter(models.Review.ticket_id == ticket_id).order_by(models.Review.modified_date.desc()).offset(skip).limit(limit).all()
        return {
            "ticket": ticket,
            "owner": owner,
            "recent_reviews": reviews
        }
    return None

def search_tickets(db: Session, email: Optional[str], nickname: Optional[str], category: Optional[str], skip: int = 0, limit: int = 10):
    query = db.query(models.Ticket)
    if email:
        query = query.join(models.Member).filter(models.Member.email.ilike(f'%{email}%'))
    if nickname:
        query = query.join(models.Member).filter(models.Member.nickname.ilike(f'%{nickname}%'))
    if category:
        query = query.filter(models.Ticket.category == category)
    return query.offset(skip).limit(limit).all()

# Review

def get_review(db: Session, review_id: int):
    return db.query(models.Review).filter(models.Review.review_id == review_id).first()

def get_reviews(db: Session, ticket_id: int, skip: int = 0, limit: int = 100):
    return db.query(models.Review).filter(models.Review.ticket_id == ticket_id).order_by(models.Review.modified_date.desc()).offset(skip).limit(limit).all()

def get_reviews_all(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Review).order_by(models.Review.modified_date.desc()).offset(skip).limit(limit).all()

def get_review_activity(db: Session, review_id: int, skip: int = 0, limit: int = 5):
    review = db.query(models.Review).filter(models.Review.review_id == review_id).first()
    if review:
        ticket = db.query(models.Ticket).filter(models.Ticket.ticket_id == review.ticket_id).first()
        owner = db.query(models.Member).filter(models.Member.member_id == review.member_id).first()
        comments = db.query(models.Comment).filter(models.Comment.review_id == review_id).order_by(models.Comment.modified_date.desc()).offset(skip).limit(limit).all()
        return {
            "review": review,
            "ticket": ticket,
            "owner": owner,
            "recent_comments": comments
        }
    return None

def search_reviews(db: Session, email: Optional[str], nickname: Optional[str], category: Optional[str], content: Optional[str], skip: int = 0, limit: int = 10):
    query = db.query(models.Review)
    if email:
        query = query.join(models.Member).filter(models.Member.email.ilike(f'%{email}%'))
    if nickname:
        query = query.join(models.Member).filter(models.Member.nickname.ilike(f'%{nickname}%'))
    if category:
        query = query.join(models.Ticket).filter(models.Ticket.category == category)
    if content:
        query = query.filter(models.Review.content.ilike(f'%{content}%'))
    return query.offset(skip).limit(limit).all()

# Comment

def get_comment(db: Session, comment_id: int):
    return db.query(models.Comment).filter(models.Comment.comment_id == comment_id).first()

def get_comments(db: Session, review_id: int, skip: int = 0, limit: int = 100):
    return db.query(models.Comment).filter(models.Comment.review_id == review_id).order_by(models.Comment.modified_date.desc()).offset(skip).limit(limit).all()

def get_comments_all(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Comment).order_by(models.Comment.modified_date.desc()).offset(skip).limit(limit).all()

def get_comment_activity(db: Session, comment_id: int):
    comment = db.query(models.Comment).filter(models.Comment.comment_id == comment_id).first()
    if comment:
        review = db.query(models.Review).filter(models.Review.review_id == comment.review_id).first()
        owner = db.query(models.Member).filter(models.Member.member_id == comment.member_id).first()
        return {
            "comment": comment,
            "review": review,
            "owner": owner
        }
    return None

def search_comments(db: Session, email: Optional[str], nickname: Optional[str], category: Optional[str], content: Optional[str], skip: int = 0, limit: int = 10):
    query = db.query(models.Comment)
    if email:
        query = query.join(models.Member).filter(models.Member.email.ilike(f'%{email}%'))
    if nickname:
        query = query.join(models.Member).filter(models.Member.nickname.ilike(f'%{nickname}%'))
    if category:
        query = query.join(models.Review).join(models.Ticket).filter(models.Ticket.category == category)
    if content:
        query = query.filter(models.Comment.commentContent.ilike(f'%{content}%'))
    return query.offset(skip).limit(limit).all()

