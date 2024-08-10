from typing import Optional
from sqlalchemy.orm import Session
from sqlalchemy import func, select, join
from app.models import Theater, Hall, ShowDetail, ShowSchedule
from datetime import datetime
from app.schemas.theater import HallCreate, HallInfo, ShowInfo, TheaterInfo, TheaterWithHallsCreate, TheaterWithHallsUpdate
from geopy.geocoders import Nominatim

def get_theater_info(db: Session):
    theaters = db.query(Theater).all()
    
    theater_info_list = []
    for theater in theaters:
        theater_info = TheaterInfo(
            theater_id=theater.theater_id,
            theater_name=theater.theater_name,
            theater_address=theater.theater_address,
            theater_latitude=theater.theater_latitude,
            theater_longitude=theater.theater_longitude,
            halls=[HallInfo(hall_id=hall.hall_id, hall_name=hall.hall_name, hall_api_id=hall.hall_api_id) for hall in theater.halls] or []
        )
        theater_info_list.append(theater_info)

    return theater_info_list

def get_theater_by_id(db: Session, theater_id: int):
    theater_info = db.query(
        Theater.theater_id,
        Theater.theater_name,
        Theater.theater_address,
        Theater.theater_latitude,
        Theater.theater_longitude,
        Theater.theater_api_id,
        Hall.hall_id,
        Hall.hall_name,
        Hall.hall_api_id
    ).outerjoin(Hall, Theater.theater_id == Hall.theater_id
    ).filter(Theater.theater_id == theater_id).all()

    if not theater_info:
        return None

    theater_dict = {
        "theater_id": theater_info[0].theater_id,
        "theater_name": theater_info[0].theater_name,
        "theater_address": theater_info[0].theater_address,
        "theater_latitude": theater_info[0].theater_latitude,
        "theater_longitude": theater_info[0].theater_longitude,
        "theater_api_id": theater_info[0].theater_api_id,
        "halls": []
    }

    for info in theater_info:
        if info.hall_id:
            theater_dict["halls"].append({
                "hall_id": info.hall_id,
                "hall_name": info.hall_name,
                "hall_api_id": info.hall_api_id
            })

    return theater_dict

def get_current_shows_in_hall(db: Session, hall_id: int, start_time: datetime, end_time: datetime):
    return db.query(
        ShowDetail.show_detail_id,
        ShowDetail.show_detail_name,
        ShowDetail.show_detail_poster_image_path,
        ShowSchedule.show_schedule_date_time
    ).join(
        ShowSchedule, ShowDetail.show_detail_id == ShowSchedule.show_detail_id
    ).filter(
        ShowDetail.hall_id == hall_id,
        ShowSchedule.show_schedule_date_time.between(start_time, end_time)
    ).all()


def get_lat_long_from_address(address: str):
    geolocator = Nominatim(user_agent="theater_registration")
    location = geolocator.geocode(address)
    
    if location:
        return location.latitude, location.longitude
    else:
        return None, None

def generate_api_id(prefix: str, current_max_id: int) -> str:
    """
    Generates a new API ID.
    """
    return f"{prefix}{str(current_max_id + 1).zfill(6)}"

def generate_hall_api_id(theater_api_id: str, current_max_hall_id: int) -> str:
    """
    Generates a new Hall API ID based on the theater's API ID.
    """
    return f"{theater_api_id}-{str(current_max_hall_id + 1).zfill(3)}"

def register_theater_with_halls(db: Session, theater_with_halls: TheaterWithHallsCreate):
    # 현재 가장 큰 theater_id를 가져옴
    current_max_theater_id = db.query(func.max(Theater.theater_id)).scalar() or 0
    theater_api_id = generate_api_id("ER", current_max_theater_id)
    
    # 위도와 경도 가져오기
    lat, long = get_lat_long_from_address(theater_with_halls.theater_address)

    # 극장 등록
    new_theater = Theater(
        theater_name=theater_with_halls.theater_name,
        theater_address=theater_with_halls.theater_address,
        theater_latitude=lat,
        theater_longitude=long,
        theater_api_id=theater_api_id
    )
    db.add(new_theater)
    db.flush()  # 극장을 먼저 DB에 저장하여 ID 생성

    # 홀 등록
    for hall in theater_with_halls.halls:
        # 해당 극장에 속한 홀들 중 가장 큰 hall_api_id를 가져옴
        max_hall_api_id = db.query(func.max(Hall.hall_api_id)).filter(Hall.theater_id == new_theater.theater_id).first()[0]
        hall_api_id = generate_hall_api_id(max_hall_api_id, theater_api_id)
        
        new_hall = Hall(
            hall_name=hall.hall_name,
            hall_api_id=hall_api_id,
            theater_id=new_theater.theater_id
        )
        db.add(new_hall)

    db.commit()
    db.refresh(new_theater)
    return new_theater


def perform_update_theater_with_halls(
    db: Session, 
    theater_id: int, 
    theater_with_halls: TheaterWithHallsUpdate
):
    theater = db.query(Theater).filter(Theater.theater_id == theater_id).first()

    if not theater:
        raise ValueError("Theater not found")

    if theater_with_halls.theater_name:
        theater.theater_name = theater_with_halls.theater_name

    if theater_with_halls.theater_address:
        theater.theater_address = theater_with_halls.theater_address
        theater.theater_latitude, theater.theater_longitude = get_lat_long_from_address(theater_with_halls.theater_address)

    if theater_with_halls.halls:
        for hall_data in theater_with_halls.halls:
            existing_hall = db.query(Hall).filter(Hall.theater_id == theater_id, Hall.hall_name == hall_data.hall_name).first()
            if existing_hall:
                existing_hall.hall_name = hall_data.hall_name
            else:
                # 새로운 홀을 추가할 때 theater_id를 사용하여 새로운 hall_api_id 생성
                hall_api_id = generate_api_id("HL", theater_id)
                new_hall = Hall(
                    hall_name=hall_data.hall_name,
                    hall_api_id=hall_api_id,
                    theater_id=theater.theater_id
                )
                db.add(new_hall)

    db.commit()
    db.refresh(theater)
    return theater

def add_hall_to_theater(db: Session, theater_id: int, hall_data: HallCreate):
    # 극장이 존재하는지 확인
    theater = db.query(Theater).filter(Theater.theater_id == theater_id).first()

    if not theater:
        raise ValueError("Theater not found")

    # 홀 추가
    current_max_hall_id = db.query(func.max(Hall.hall_id)).scalar() or 0
    hall_api_id = generate_hall_api_id(theater.theater_api_id, current_max_hall_id)

    new_hall = Hall(
        hall_name=hall_data.hall_name,
        hall_api_id=hall_api_id,
        theater_id=theater_id
    )
    db.add(new_hall)
    db.commit()
    db.refresh(new_hall)

    return new_hall