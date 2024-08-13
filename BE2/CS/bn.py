# from fastapi import APIRouter, Depends, HTTPException
# from sqlalchemy.orm import Session
# from app.database import get_db
# from app.crud.theater import add_hall_to_theater, get_current_shows_in_hall_with_runtime, get_theater_by_id, get_theater_info, get_current_shows_in_hall, perform_update_theater_with_halls, register_theater_with_halls, search_theater
# from app.models import Hall, ShowDetail, Theater
# from app.schemas.theater import HallResponse, TheaterInfo, ShowInfo, TheaterWithHallsCreate, TheaterWithHallsResponse, TheaterWithHallsUpdate, HallCreate, TheaterSearchRequest
# from typing import List
# from datetime import datetime

# router = APIRouter()

# @router.get("/theaters", response_model=list[TheaterInfo])
# def read_theater_info(db: Session = Depends(get_db)):
#     return get_theater_info(db)

# @router.get("/theaters/{theater_id}", response_model=TheaterInfo)
# def read_theater_by_id(theater_id: int, db: Session = Depends(get_db)):
#     theater_data = get_theater_by_id(db, theater_id)
#     if theater_data is None:
#         raise HTTPException(status_code=404, detail="Theater not found")
#     return theater_data

# @router.get("/halls/{hall_id}/shows", response_model=List[ShowInfo])
# def read_current_shows_in_hall(
#     hall_id: int, 
#     start_time: datetime, 
#     end_time: datetime, 
#     db: Session = Depends(get_db)
# ):
#     shows = get_current_shows_in_hall_with_runtime(db, hall_id, start_time, end_time)
#     if not shows:
#         raise HTTPException(status_code=404, detail="No shows found in the specified time range")
#     return shows

# @router.post("/theater", response_model=TheaterWithHallsResponse)
# def create_theater_with_halls(theater_with_halls: TheaterWithHallsCreate, db: Session = Depends(get_db)):
#     try:
#         theater_data = register_theater_with_halls(db, theater_with_halls)
#         return theater_data
#     except ValueError as e:
#         raise HTTPException(status_code=400, detail=str(e))

# @router.put("/theaters/{theater_id}", response_model=TheaterWithHallsResponse)
# def update_theater_with_halls(theater_id: int, theater_with_halls: TheaterWithHallsUpdate, db: Session = Depends(get_db)):
#     try:
#         theater_data = perform_update_theater_with_halls(db, theater_id, theater_with_halls)
#         return theater_data
#     except ValueError as e:
#         raise HTTPException(status_code=400, detail=str(e))

# @router.delete("/theaters/{theater_id}", response_model=dict)
# def delete_theater(theater_id: int, db: Session = Depends(get_db)):
#     theater = db.query(Theater).filter(Theater.theater_id == theater_id).first()
#     if not theater:
#         raise HTTPException(status_code=404, detail="Theater not found")
#     # 연결된 홀도 함께 삭제
#     db.query(Hall).filter(Hall.theater_id == theater_id).delete()
#     db.delete(theater)
#     db.commit()
#     return {"message": "Theater and associated halls deleted successfully"}

# @router.delete("/hall/{hall_id}", response_model=dict)
# def delete_hall(hall_id: int, db: Session = Depends(get_db)):
#     # Hall을 먼저 조회
#     hall = db.query(Hall).filter(Hall.hall_id == hall_id).first()
    
#     if not hall:
#         raise HTTPException(status_code=404, detail="Hall not found")
    
#     # 연결된 show_details가 있는지 확인하고 삭제
#     show_details = db.query(ShowDetail).filter(ShowDetail.hall_id == hall_id).all()
#     if show_details:
#         for detail in show_details:
#             db.delete(detail)

#     # Hall 삭제
#     db.delete(hall)
#     db.commit()
    
#     return {"message": "Hall deleted successfully"}

# @router.post("/theater/{theater_id}/hall", response_model=HallResponse)
# def create_hall_for_theater(theater_id: int, hall_data: HallCreate, db: Session = Depends(get_db)):
#     try:
#         new_hall = add_hall_to_theater(db, theater_id, hall_data)
#         return new_hall
#     except ValueError as e:
#         raise HTTPException(status_code=404, detail=str(e))
    
# @router.post("/theater/search", response_model=List[TheaterInfo])
# def search_theater_route(
#     search_request: TheaterSearchRequest,
#     db: Session = Depends(get_db)
# ):
#     theaters = search_theater(
#         db=db,
#         region=search_request.region,
#         theater_name=search_request.theater_name,
#         hall_name=search_request.hall_name,
#         show_detail_name=search_request.show_detail_name
#     )

#     if not theaters:
#         raise HTTPException(status_code=404, detail="No theaters found with the provided search criteria")
    
#     return theaters