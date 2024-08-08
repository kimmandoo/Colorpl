from sqlalchemy.orm import Session
from . import models, schemas

def get_show_details(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.ShowDetail).offset(skip).limit(limit).all()

def get_show_detail(db: Session, show_detail_id: int):
    return db.query(models.ShowDetail).filter(models.ShowDetail.show_detail_id == show_detail_id).first()

def create_show_detail(db: Session, show_detail: schemas.ShowDetailCreate):
    db_show_detail = models.ShowDetail(**show_detail.dict())
    db.add(db_show_detail)
    db.commit()
    db.refresh(db_show_detail)
    return db_show_detail

def get_show_schedules(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.ShowSchedule).offset(skip).limit(limit).all()

def create_show_schedule(db: Session, show_schedule: schemas.ShowScheduleCreate):
    db_show_schedule = models.ShowSchedule(**show_schedule.dict())
    db.add(db_show_schedule)
    db.commit()
    db.refresh(db_show_schedule)
    return db_show_schedule

def get_theaters(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Theater).offset(skip).limit(limit).all()

def create_theater(db: Session, theater: schemas.TheaterCreate):
    db_theater = models.Theater(**theater.dict())
    db.add(db_theater)
    db.commit()
    db.refresh(db_theater)
    return db_theater

def get_seats(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Seat).offset(skip).limit(limit).all()

def create_seat(db: Session, seat: schemas.SeatCreate):
    db_seat = models.Seat(**seat.dict())
    db.add(db_seat)
    db.commit()
    db.refresh(db_seat)
    return db_seat

def get_prices_by_seat_class(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.PriceBySeatClass).offset(skip).limit(limit).all()

def create_price_by_seat_class(db: Session, price_by_seat_class: schemas.PriceBySeatClassCreate):
    db_price_by_seat_class = models.PriceBySeatClass(**price_by_seat_class.dict())
    db.add(db_price_by_seat_class)
    db.commit()
    db.refresh(db_price_by_seat_class)
    return db_price_by_seat_class
