# from sqlalchemy.orm import Session
# from collections import defaultdict
# from app.models import ShowDetail, Theater, Hall, ShowSchedule, PriceBySeatClass, Seat
# from app.schemas.show import (
#     ShowDetailCreate, ShowDetailUpdate, ShowScheduleCreate, 
#     ShowScheduleUpdate, PriceBySeatClassCreate, PriceBySeatClassUpdate,
#     SeatCreate, SeatUpdate
# )
# from sqlalchemy import func
# from fastapi import Query, UploadFile
# from app.utils.image import save_image_file, delete_image_file
# from typing import Optional


# DAYS = ["월", "화", "수", "목", "금", "토", "일"]

# def get_show_table(db: Session, skip: int = 0, limit: int = 10):
#     raw_results = db.query(
#         ShowDetail.show_detail_id,
#         ShowDetail.show_detail_name,
#         ShowDetail.show_detail_cast,
#         ShowDetail.show_detail_area,
#         ShowDetail.show_detail_category,
#         ShowDetail.show_detail_state,
#         Theater.theater_name,
#         Hall.hall_name,
#         func.group_concat(func.dayname(ShowSchedule.show_schedule_date_time)).label("schedule_days")
#     ).join(
#         ShowSchedule, ShowDetail.show_detail_id == ShowSchedule.show_schedule_id
#     ).join(
#         Hall, ShowDetail.hall_id == Hall.hall_id
#     ).join(
#         Theater, Hall.theater_id == Theater.theater_id
#     ).group_by(
#         ShowDetail.show_detail_id,
#         ShowDetail.show_detail_name,
#         ShowDetail.show_detail_cast,
#         ShowDetail.show_detail_area,
#         ShowDetail.show_detail_category,
#         ShowDetail.show_detail_state,
#         Theater.theater_name,
#         Hall.hall_name
#     ).offset(skip).limit(limit).all()  # 페이지네이션 적용

#     day_map = {
#         "Monday": 0,
#         "Tuesday": 1,
#         "Wednesday": 2,
#         "Thursday": 3,
#         "Friday": 4,
#         "Saturday": 5,
#         "Sunday": 6
#     }

#     formatted_results = []
#     for result in raw_results:
#         day_names = result.schedule_days.split(',') if result.schedule_days else []
#         day_indices = sorted(set(day_map[day] for day in day_names if day in day_map))
#         summarized_days = summarize_days(day_indices)

#         formatted_results.append({
#             "show_detail_id": result.show_detail_id,
#             "show_detail_name": result.show_detail_name,
#             "show_detail_cast": result.show_detail_cast,
#             "show_detail_area": result.show_detail_area,
#             "show_detail_category": result.show_detail_category,
#             "show_detail_state": result.show_detail_state,
#             "theater_name": result.theater_name,
#             "hall_name": result.hall_name,
#             "schedule_days": summarized_days
#         })

#     return formatted_results

# def summarize_days(day_indices):
#     """Given a sorted list of day indices, summarize them into ranges like '월~금'."""
#     if not day_indices:
#         return ""

#     result = []
#     start = day_indices[0]
#     end = start

#     for i in range(1, len(day_indices)):
#         if day_indices[i] == end + 1:
#             end = day_indices[i]
#         else:
#             if start == end:
#                 result.append(DAYS[start])
#             else:
#                 result.append(f"{DAYS[start]}~{DAYS[end]}")
#             start = end = day_indices[i]

#     if start == end:
#         result.append(DAYS[start])
#     else:
#         result.append(f"{DAYS[start]}~{DAYS[end]}")

#     return ", ".join(result)

# def generate_api_id(db: Session) -> str:
#     # 현재 가장 큰 API ID를 조회합니다.
#     max_api_id = db.query(func.max(ShowDetail.show_detail_api_id)).scalar()

#     if max_api_id:
#         # 숫자 부분만 추출하여 1을 더합니다.
#         next_id_number = int(max_api_id[2:]) + 1
#     else:
#         next_id_number = 0

#     # 새로운 API ID를 생성합니다.
#     new_api_id = f"SH{next_id_number:06d}"
#     return new_api_id

# def get_next_show_detail_id(db: Session) -> int:
#     max_id = db.query(func.max(ShowDetail.show_detail_id)).scalar()
#     return (max_id or 0) + 1

# def create_show_detail(db: Session, show_detail: ShowDetailCreate, image_path: str) -> ShowDetail:
#     # 다음 ID 가져오기
#     next_id = get_next_show_detail_id(db)
    
#     # 새로운 API ID 생성
#     new_api_id = generate_api_id(db)
    
#     # 새로운 ShowDetail 객체 생성
#     show_detail_data = ShowDetail(
#         show_detail_id=next_id,  # 수동으로 ID 설정
#         hall_id=show_detail.hall_id,
#         show_detail_api_id=new_api_id,  # 생성된 API ID 설정
#         show_detail_area=show_detail.show_detail_area,
#         show_detail_cast=show_detail.show_detail_cast,
#         show_detail_category=show_detail.show_detail_category,
#         show_detail_name=show_detail.show_detail_name,
#         show_detail_poster_image_path=image_path,
#         show_detail_runtime=show_detail.show_detail_runtime,
#         show_detail_state=show_detail.show_detail_state
#     )
    
#     db.add(show_detail_data)
#     db.commit()
#     db.refresh(show_detail_data)
    
#     return show_detail_data

# def update_show_detail(db: Session, show_detail_id: int, show_detail_update: ShowDetailUpdate):
#     db_show_detail = db.query(ShowDetail).filter(ShowDetail.show_detail_id == show_detail_id).first()
#     if not db_show_detail:
#         return None
    
#     for key, value in show_detail_update.dict(exclude_unset=True).items():
#         setattr(db_show_detail, key, value)
    
#     db.commit()
#     db.refresh(db_show_detail)
#     return db_show_detail

# def delete_show_detail(db: Session, show_detail_id: int):
#     db_show_detail = db.query(ShowDetail).filter(ShowDetail.show_detail_id == show_detail_id).first()
#     if not db_show_detail:
#         return False
#     db.delete(db_show_detail)
#     db.commit()
#     return True

# def create_show_schedule(db: Session, schedule: ShowScheduleCreate):
#     db_schedule = ShowSchedule(
#         show_detail_id=schedule.show_detail_id,
#         show_schedule_date_time=schedule.show_schedule_date_time
#     )
#     db.add(db_schedule)
#     db.commit()
#     db.refresh(db_schedule)
#     return db_schedule

# def update_show_schedule(db: Session, schedule_id: int, schedule_update: ShowScheduleUpdate):
#     db_schedule = db.query(ShowSchedule).filter(ShowSchedule.show_schedule_id == schedule_id).first()
#     if not db_schedule:
#         return None
    
#     for key, value in schedule_update.dict(exclude_unset=True).items():
#         setattr(db_schedule, key, value)
    
#     db.commit()
#     db.refresh(db_schedule)
#     return db_schedule

# def delete_show_schedule(db: Session, schedule_id: int):
#     db_schedule = db.query(ShowSchedule).filter(ShowSchedule.show_schedule_id == schedule_id).first()
#     if not db_schedule:
#         return False
#     db.delete(db_schedule)
#     db.commit()
#     return True

# def create_price_by_seat_class(db: Session, price_data: PriceBySeatClassCreate):
#     db_price = PriceBySeatClass(
#         show_detail_id=price_data.show_detail_id,
#         price_by_seat_class_seat_class=price_data.price_by_seat_class_seat_class,
#         price_by_seat_class_price=price_data.price_by_seat_class_price
#     )
#     db.add(db_price)
#     db.commit()
#     db.refresh(db_price)
#     return db_price

# def update_price_by_seat_class(
#     db: Session,
#     show_detail_id: int,
#     seat_class: str,
#     new_price: int
# ):
#     db_price = db.query(PriceBySeatClass).filter(
#         PriceBySeatClass.show_detail_id == show_detail_id,
#         PriceBySeatClass.price_by_seat_class_seat_class == seat_class
#     ).first()

#     if db_price:
#         db_price.price_by_seat_class_price = new_price
#         db.commit()
#         db.refresh(db_price)
#         return db_price
#     else:
#         return None

# def delete_price_by_seat_class(
#     db: Session,
#     show_detail_id: int,
#     seat_class: str
# ):
#     db_price = db.query(PriceBySeatClass).filter(
#         PriceBySeatClass.show_detail_id == show_detail_id,
#         PriceBySeatClass.price_by_seat_class_seat_class == seat_class
#     ).first()

#     if db_price:
#         db.delete(db_price)
#         db.commit()
#         return True
#     else:
#         return False

# def create_seat(db: Session, seat: SeatCreate):
#     db_seat = Seat(
#         show_detail_id=seat.show_detail_id,
#         seat_row=seat.seat_row,
#         seat_col=seat.seat_col,
#         seat_class=seat.seat_class
#     )
#     db.add(db_seat)
#     db.commit()
#     db.refresh(db_seat)
#     return db_seat

# def update_seat(db: Session, seat_id: int, seat_update: SeatUpdate):
#     db_seat = db.query(Seat).filter(Seat.seat_id == seat_id).first()  # Assuming you have an ID field
#     if not db_seat:
#         return None
    
#     for key, value in seat_update.dict(exclude_unset=True).items():
#         setattr(db_seat, key, value)
    
#     db.commit()
#     db.refresh(db_seat)
#     return db_seat

# def delete_seat(db: Session, seat_id: int):
#     db_seat = db.query(Seat).filter(Seat.seat_id == seat_id).first()  # Assuming you have an ID field
#     if not db_seat:
#         return False
#     db.delete(db_seat)
#     db.commit()
#     return True

# def save_image(image: UploadFile):
#     return save_image_file(image)