from sqlalchemy.orm import Session
from sqlalchemy import select, join
from app.models import Theater, Hall, ShowDetail, ShowSchedule
from datetime import datetime
from app.schemas.theater import ShowInfo

def get_theater_info(db: Session):
    # Step 1: Construct the query
    query = (
        db.query(
            Theater.theater_id,
            Theater.theater_name,
            Theater.theater_address,
            Theater.theater_latitude,
            Theater.theater_longitude,
            Hall.hall_id,
            Hall.hall_name,
            Hall.hall_api_id
        )
        .join(Hall, Hall.theater_id == Theater.theater_id)
        .all()
    )

    # Step 2: Process results into a dictionary format
    theater_dict = {}
    for row in query:
        if row.theater_id not in theater_dict:
            theater_dict[row.theater_id] = {
                "theater_id": row.theater_id,
                "theater_name": row.theater_name,
                "theater_address": row.theater_address,
                "theater_latitude": row.theater_latitude,
                "theater_longitude": row.theater_longitude,
                "halls": []
            }
        theater_dict[row.theater_id]["halls"].append({
            "hall_id": row.hall_id,
            "hall_name": row.hall_name,
            "hall_api_id": row.hall_api_id
        })
    
    # Step 3: Convert the dictionary to a list for easy JSON serialization
    return list(theater_dict.values())

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
