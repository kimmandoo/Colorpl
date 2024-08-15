from sqlalchemy.orm import Session
from models import Theater, Hall
from schemas.theater_hall import TheaterCreate, TheaterUpdate

def get_theater_by_api_id(db: Session, theater_api_id: str):
    return db.query(Theater).filter(Theater.theater_api_id == theater_api_id).first()

def create_theater(db: Session, theater_data: TheaterCreate):
    new_theater = Theater(
        theater_api_id=theater_data.theater_api_id,
        theater_name=theater_data.theater_name,
        theater_address=theater_data.theater_address,
        theater_latitude=theater_data.theater_latitude,
        theater_longitude=theater_data.theater_longitude
    )
    
    db.add(new_theater)
    db.commit()
    db.refresh(new_theater)

    for hall_data in theater_data.halls:
        # hall_seats = hall_data.hall_seats if hall_data.hall_seats is not None else None
        
        new_hall = Hall(
            hall_api_id=hall_data.hall_api_id,
            hall_name=hall_data.hall_name,
            # hall_seats=hall_seats,
            theater_id=new_theater.theater_id
        )
        db.add(new_hall)
    
    db.commit()
    return new_theater


def get_theater(db: Session, theater_id: int):
    return db.query(Theater).filter(Theater.theater_id == theater_id).first()

def delete_theater(db: Session, theater_id: int):
    db.query(Theater).filter(Theater.theater_id == theater_id).delete()
    db.commit()

def update_theater(db: Session, theater_id: int, theater_update: TheaterUpdate):
    db_theater = db.query(Theater).filter(Theater.theater_id == theater_id).first()
    if not db_theater:
        return None
    
    for key, value in theater_update.dict(exclude_unset=True, exclude={"halls"}).items():
        setattr(db_theater, key, value)

    db.commit()
    db.refresh(db_theater)

    existing_halls = db.query(Hall).filter(Hall.theater_id == theater_id).all()
    existing_hall_map = {hall.hall_api_id: hall for hall in existing_halls}

    for hall_update in theater_update.halls:
        if hall_update.hall_api_id in existing_hall_map:
            db_hall = existing_hall_map[hall_update.hall_api_id]
            for key, value in hall_update.dict(exclude_unset=True).items():
                setattr(db_hall, key, value)
            db.add(db_hall)
        else:
            new_hall = Hall(
                hall_api_id=hall_update.hall_api_id,
                hall_name=hall_update.hall_name,
                # hall_seats=hall_update.hall_seats,
                theater_id=theater_id
            )
            db.add(new_hall)
        
        updated_hall_api_ids = {hall.hall_api_id for hall in theater_update.halls}

        for existing_hall in existing_halls:
            if existing_hall.hall_api_id not in updated_hall_api_ids:
                db.delete(existing_hall)

        db.commit()
        return db_theater
    
def get_all_theaters(db: Session, skip: int = 0, limit: int = 10):
    return db.query(Theater).offset(skip).limit(limit).all()

def search_theaters_by_name(db: Session, theater_name: str, skip: int = 0, limit: int = 10):
    return db.query(Theater).filter(Theater.theater_name.ilike(f"%{theater_name}%")).offset(skip).limit(limit).all()

def get_halls_by_theater(db: Session, theater_id: int):
    return db.query(Hall).filter(Hall.theater_id == theater_id).all()