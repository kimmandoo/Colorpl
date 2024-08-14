from sqlalchemy.orm import Session
from app.models import Reservation, Member, ReservationDetail, ShowSchedule, ShowDetail, Hall
from app.schemas.reservation import ReservationUpdate, ReservationSearch

def get_reservations_activity(db: Session, skip: int = 0, limit: int = 10):
    query = db.query(
        Reservation.reserve_id,
        Member.member_id,
        Member.nickname,
        Reservation.create_date,
        Reservation.reserve_amount,
        Reservation.reserve_date,
        Reservation.is_refunded,
        ShowDetail.show_detail_category
    ).join(Member, Reservation.member_id == Member.member_id)\
     .join(ReservationDetail, Reservation.reserve_id == ReservationDetail.reserve_id)\
     .join(ShowSchedule, ReservationDetail.show_schedule_id == ShowSchedule.show_schedule_id)\
     .join(ShowDetail, ShowSchedule.show_detail_id == ShowDetail.show_detail_id)\
     .offset(skip).limit(limit)

    reservations = query.all()

    return [
        {
            "reserve_id": reservation.reserve_id,
            "member_id": reservation.member_id,
            "nickname": reservation.nickname,
            "create_date": reservation.create_date,
            "reserve_amount": reservation.reserve_amount,
            "reserve_date": reservation.reserve_date,
            "is_refunded": reservation.is_refunded,
            "show_detail_category": reservation.show_detail_category,
        }
        for reservation in reservations
    ]

def get_reservation_by_id(db: Session, reservation_id: int):
    reservation = db.query(
        Reservation.reserve_id,
        Member.member_id,
        Member.nickname,
        Member.profile,
        Reservation.create_date,
        Reservation.update_date,
        Reservation.reserve_date,
        Reservation.reserve_amount,
        Reservation.reserve_comment,
        Reservation.is_refunded,
        ReservationDetail.seat_col,
        ReservationDetail.seat_row,
        ShowDetail.show_detail_name,
        Hall.hall_name
    ).join(Member, Reservation.member_id == Member.member_id)\
     .join(ReservationDetail, Reservation.reserve_id == ReservationDetail.reserve_id)\
     .join(ShowSchedule, ReservationDetail.show_schedule_id == ShowSchedule.show_schedule_id)\
     .join(ShowDetail, ShowSchedule.show_detail_id == ShowDetail.show_detail_id)\
     .join(Hall, ShowDetail.hall_id == Hall.hall_id)\
     .filter(Reservation.reserve_id == reservation_id)\
     .first()

    if not reservation:
        return None

    return {
        "reserve_id": reservation.reserve_id,
        "member_id": reservation.member_id,
        "nickname": reservation.nickname,
        "profile": reservation.profile,
        "create_date": reservation.create_date,
        "update_date": reservation.update_date,
        "reserve_date": reservation.reserve_date,
        "reserve_amount": reservation.reserve_amount,
        "reserve_comment": reservation.reserve_comment,
        "is_refunded": reservation.is_refunded,
        "seat_col": reservation.seat_col,
        "seat_row": reservation.seat_row,
        "show_detail_name": reservation.show_detail_name,
        "hall_name": reservation.hall_name,
    }

def update_reservation(db: Session, reservation_id: int, reservation_update: ReservationUpdate):
    reservation = db.query(Reservation).filter(Reservation.reserve_id == reservation_id).first()
    if not reservation:
        return None

    update_data = reservation_update.dict(exclude_unset=True)
    for key, value in update_data.items():
        setattr(reservation, key, value)
    db.commit()
    db.refresh(reservation)
    return reservation

def search_reservations(db: Session, search: ReservationSearch, skip: int = 0, limit: int = 10):
    query = db.query(
        Reservation.reserve_id,
        Member.member_id,
        Member.nickname,
        Reservation.create_date,
        Reservation.reserve_amount,
        Reservation.reserve_date,
        Reservation.is_refunded,
        ShowDetail.show_detail_category
    ).join(Member, Reservation.member_id == Member.member_id)\
     .join(ReservationDetail, Reservation.reserve_id == ReservationDetail.reserve_id)\
     .join(ShowSchedule, ReservationDetail.show_schedule_id == ShowSchedule.show_schedule_id)\
     .join(ShowDetail, ShowSchedule.show_detail_id == ShowDetail.show_detail_id)

    if search.member_id:
        query = query.filter(Member.member_id == search.member_id)
    if search.nickname:
        query = query.filter(Member.nickname.ilike(f'%{search.nickname}%'))
    if search.email:
        query = query.filter(Member.email.ilike(f'%{search.email}%'))
    if search.reserve_id:
        query = query.filter(Reservation.reserve_id == search.reserve_id)
    if search.show_detail_category:
        query = query.filter(ShowDetail.show_detail_category == search.show_detail_category)
    if search.is_refunded is not None:
        query = query.filter(Reservation.is_refunded == search.is_refunded)

    reservations = query.offset(skip).limit(limit).all()

    return [
        {
            "reserve_id": reservation.reserve_id,
            "member_id": reservation.member_id,
            "nickname": reservation.nickname,
            "create_date": reservation.create_date,
            "reserve_amount": reservation.reserve_amount,
            "reserve_date": reservation.reserve_date,
            "is_refunded": reservation.is_refunded,
            "show_detail_category": reservation.show_detail_category,
        }
        for reservation in reservations
    ]

