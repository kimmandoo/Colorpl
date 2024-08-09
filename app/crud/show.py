from sqlalchemy.orm import Session
from collections import defaultdict
from app.models import ShowDetail, Theater, Hall, ShowSchedule
from app.schemas.show import ShowSummary
from sqlalchemy import func
from fastapi import Query

DAYS = ["월", "화", "수", "목", "금", "토", "일"]

def get_show_table(db: Session, skip: int = 0, limit: int = 10):
    raw_results = db.query(
        ShowDetail.show_detail_id,
        ShowDetail.show_detail_name,
        ShowDetail.show_detail_cast,
        ShowDetail.show_detail_area,
        ShowDetail.show_detail_category,
        ShowDetail.show_detail_state,
        Theater.theater_name,
        Hall.hall_name,
        func.group_concat(func.dayname(ShowSchedule.show_schedule_date_time)).label("schedule_days")
    ).join(
        ShowSchedule, ShowDetail.show_detail_id == ShowSchedule.show_schedule_id
    ).join(
        Hall, ShowDetail.hall_id == Hall.hall_id
    ).join(
        Theater, Hall.theater_id == Theater.theater_id
    ).group_by(
        ShowDetail.show_detail_id,
        ShowDetail.show_detail_name,
        ShowDetail.show_detail_cast,
        ShowDetail.show_detail_area,
        ShowDetail.show_detail_category,
        ShowDetail.show_detail_state,
        Theater.theater_name,
        Hall.hall_name
    ).offset(skip).limit(limit).all()  # 페이지네이션 적용

    day_map = {
        "Monday": 0,
        "Tuesday": 1,
        "Wednesday": 2,
        "Thursday": 3,
        "Friday": 4,
        "Saturday": 5,
        "Sunday": 6
    }

    formatted_results = []
    for result in raw_results:
        day_names = result.schedule_days.split(',') if result.schedule_days else []
        day_indices = sorted(set(day_map[day] for day in day_names if day in day_map))
        summarized_days = summarize_days(day_indices)

        formatted_results.append({
            "show_detail_id": result.show_detail_id,
            "show_detail_name": result.show_detail_name,
            "show_detail_cast": result.show_detail_cast,
            "show_detail_area": result.show_detail_area,
            "show_detail_category": result.show_detail_category,
            "show_detail_state": result.show_detail_state,
            "theater_name": result.theater_name,
            "hall_name": result.hall_name,
            "schedule_days": summarized_days
        })

    return formatted_results

def summarize_days(day_indices):
    """Given a sorted list of day indices, summarize them into ranges like '월~금'."""
    if not day_indices:
        return ""

    result = []
    start = day_indices[0]
    end = start

    for i in range(1, len(day_indices)):
        if day_indices[i] == end + 1:
            end = day_indices[i]
        else:
            if start == end:
                result.append(DAYS[start])
            else:
                result.append(f"{DAYS[start]}~{DAYS[end]}")
            start = end = day_indices[i]

    if start == end:
        result.append(DAYS[start])
    else:
        result.append(f"{DAYS[start]}~{DAYS[end]}")

    return ", ".join(result)
