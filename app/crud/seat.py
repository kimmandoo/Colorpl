from typing import List
from sqlalchemy.orm import Session
from app.models import Seat
from app.schemas.seat import SeatCreate

def create_seat(db: Session, seat: SeatCreate):
    db_seat = Seat(
        seat_col=seat.seat_col,
        seat_row=seat.seat_row,
        seat_class=seat.seat_class,
        show_detail_id=seat.show_detail_id
    )
    db.add(db_seat)
    db.commit()
    db.refresh(db_seat)
    return db_seat

def create_seats_bulk(db: Session, seats: List[SeatCreate]):
    db_seats = [Seat(**seat.dict()) for seat in seats]
    db.add_all(db_seats)  # 객체를 세션에 추가합니다.
    db.commit()  # 커밋하여 데이터베이스에 반영합니다.
    for seat in db_seats:
        db.refresh(seat)  # 커밋 후 세션에 있는 객체를 새로고침합니다.
    return db_seats

def get_seats_by_show_detail(db: Session, show_detail_id: int):
    return db.query(Seat).filter(Seat.show_detail_id == show_detail_id).all()
