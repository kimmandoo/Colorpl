from datetime import timedelta
from sqlalchemy.orm import Session
from sqlalchemy import func
from ..models import Member, Schedule, Review, Comment, Reservation, MemberCategories
from ..schemas.member import MemberSearch, MemberUpdateDTO, MemberActivity
from typing import List

def get_member(db: Session, member_id: int):
    return db.query(Member).filter(Member.member_id == member_id).first()

def update_member(db: Session, member_id: int, member_update: MemberUpdateDTO):
    member = get_member(db, member_id)
    if not member:
        return None

    updated_data = member_update.dict(exclude_unset=True)
    for key, value in updated_data.items():
        setattr(member, key, value)

    db.commit()
    db.refresh(member)
    return member

def get_members_activity(db: Session, skip: int = 0, limit: int = 10):
    members = db.query(Member).offset(skip).limit(limit).all()
    return _get_member_activities(db, members)

def search_member(db: Session, search: MemberSearch, skip: int = 0, limit: int = 10):
    query = db.query(Member)
    query = apply_member_search_filters(query, search)
    members = query.offset(skip).limit(limit).all()
    return _get_member_activities(db, members)

def apply_member_search_filters(query, search: MemberSearch):
    search_data = search.dict(exclude_unset=True)

    if "member_id" in search_data:
        query = query.filter(Member.member_id == search_data["member_id"])
    
    if "nickname" in search_data:
        query = query.filter(Member.nickname.ilike(f'%{search_data["nickname"]}%'))
        
    if "email" in search_data:
        query = query.filter(Member.email.ilike(f'%{search_data["email"]}%'))
        
    if "create_date_from" in search_data and "create_date_to" in search_data:
        if search_data["create_date_from"] == search_data["create_date_to"]:
            start_of_day = search_data["create_date_from"]
            end_of_day = start_of_day + timedelta(days=1)
            query = query.filter(Member.create_date >= start_of_day, Member.create_date < end_of_day)
        else:
            query = query.filter(Member.create_date >= search_data["create_date_from"])
            query = query.filter(Member.create_date <= search_data["create_date_to"])
    
    elif "create_date_from" in search_data:
        query = query.filter(Member.create_date >= search_data["create_date_from"])
        
    elif "create_date_to" in search_data:
        query = query.filter(Member.create_date <= search_data["create_date_to"])
        
    if "category" in search_data:
        if isinstance(search_data["category"], list) and search_data["category"]:
            query = query.join(MemberCategories).filter(MemberCategories.categories.in_(search_data["category"]))
        else:
            query = query.join(MemberCategories).filter(MemberCategories.categories == search_data["category"])
    
    return query

def _get_member_activities(db: Session, members):
    member_activities = []
    for member in members:
        schedules_count = db.query(Schedule).filter(Schedule.member_id == member.member_id).count()
        reviews_count = db.query(Review).join(Schedule).filter(Schedule.member_id == member.member_id).count()
        comments_count = db.query(Comment).filter(Comment.member_id == member.member_id).count()
        reservations_count = db.query(Reservation).filter(Reservation.member_id == member.member_id).count()

        member_activities.append({
            "member_id": member.member_id,
            "nickname": member.nickname,
            "email": member.email,
            "create_date": member.create_date,
            "categories": [category.categories for category in member.categories],
            "schedules_count": schedules_count,
            "reviews_count": reviews_count,
            "comments_count": comments_count,
            "reservations_count": reservations_count,
            "type": member.type
        })
    return member_activities