from fastapi import APIRouter, Body, Depends, HTTPException, UploadFile, File
from sqlalchemy.orm import Session
from app.schemas.show import (
    ShowDetailCreate, ShowDetailUpdate, ShowDetailResponse,
    ShowScheduleCreate, ShowScheduleUpdate, PriceBySeatClassCreate, 
    PriceBySeatClassUpdate, SeatCreate, SeatUpdate, ShowSummary
)
from app.crud.show import (
    get_show_table, create_show_detail, update_show_detail, delete_show_detail, create_show_schedule, 
    update_show_schedule, delete_show_schedule, create_price_by_seat_class, 
    update_price_by_seat_class, delete_price_by_seat_class, create_seat, 
    update_seat, delete_seat, save_image
)
from app.database import get_db
from typing import List, Optional, Dict

router = APIRouter()

@router.get("/shows", response_model=list[ShowSummary])
def read_show_table(
    db: Session = Depends(get_db), skip: int = 0, limit: int = 10):
    return get_show_table(db, skip=skip, limit=limit)

# 1. Show Detail 등록
@router.post("/shows/detail", response_model=ShowDetailResponse)
def create_show_detail_route(
    show_detail: ShowDetailCreate, 
    db: Session = Depends(get_db)
):
    return create_show_detail(db, show_detail, image_path="")

# 2. Show Detail 수정
@router.patch("/shows/{show_detail_id}", response_model=ShowDetailResponse)
def update_show_detail_route(
    show_detail_id: int,
    show_detail_update: ShowDetailUpdate,
    db: Session = Depends(get_db)
):
    show_detail = update_show_detail(db, show_detail_id, show_detail_update)
    if not show_detail:
        raise HTTPException(status_code=404, detail="Show not found")
    return show_detail

# 3. Show Detail 삭제
@router.delete("/shows/{show_detail_id}")
def delete_show_detail_route(
    show_detail_id: int,
    db: Session = Depends(get_db)
):
    success = delete_show_detail(db, show_detail_id)
    if not success:
        raise HTTPException(status_code=404, detail="Show not found")
    return {"detail": "Show deleted successfully"}

# 4. Price by Seat Class 등록
@router.post("/shows/{show_detail_id}/price")
def create_price_route(
    show_detail_id: int,
    price_class_mapping: Dict[str, int],  # 등급과 가격을 받아옴
    db: Session = Depends(get_db)
):
    for seat_class, price in price_class_mapping.items():
        price_data = PriceBySeatClassCreate(
            show_detail_id=show_detail_id,
            price_by_seat_class_seat_class=seat_class,
            price_by_seat_class_price=price
        )
        create_price_by_seat_class(db, price_data)
    return {"detail": "Price by seat class registered"}

# 5. Price by Seat Class 수정
@router.patch("/shows/{show_detail_id}/price")
def update_price_route(
    show_detail_id: int,
    price_class_mapping: Dict[str, int],
    db: Session = Depends(get_db)
):
    for seat_class, new_price in price_class_mapping.items():
        price = update_price_by_seat_class(db, show_detail_id, seat_class, new_price)
        if not price:
            raise HTTPException(status_code=404, detail=f"Price not found for seat class {seat_class}")
    return {"detail": "Prices updated successfully"}

# 6. Price by Seat Class 삭제
@router.delete("/shows/{show_detail_id}/price/{seat_class}")
def delete_price_route(
    show_detail_id: int,
    seat_class: str,
    db: Session = Depends(get_db)
):
    success = delete_price_by_seat_class(db, show_detail_id, seat_class)
    if not success:
        raise HTTPException(status_code=404, detail=f"Price not found for seat class {seat_class}")
    return {"detail": "Price deleted successfully"}

# 7. Seat 정보 등록
@router.post("/shows/{show_detail_id}/seats")
def create_seat_route(
    show_detail_id: int,
    seats: List[SeatCreate],
    db: Session = Depends(get_db)
):
    for seat in seats:
        seat.show_detail_id = show_detail_id
        create_seat(db, seat)
    return {"detail": "Seats registered"}

# 8. Seat 수정
@router.patch("/shows/seats/{seat_id}")
def update_seat_route(
    seat_id: int,
    seat_update: SeatUpdate,
    db: Session = Depends(get_db)
):
    seat = update_seat(db, seat_id, seat_update)
    if not seat:
        raise HTTPException(status_code=404, detail="Seat not found")
    return {"detail": "Seat updated successfully"}

# 9. Seat 삭제
@router.delete("/shows/seats/{seat_id}")
def delete_seat_route(
    seat_id: int,
    db: Session = Depends(get_db)
):
    success = delete_seat(db, seat_id)
    if not success:
        raise HTTPException(status_code=404, detail="Seat not found")
    return {"detail": "Seat deleted successfully"}

@router.post("/shows/{show_detail_id}/seats")
def create_seat_route(
    show_detail_id: int,
    seats: List[List[str]],  # 16x10 배열을 받아옴
    db: Session = Depends(get_db)
):
    try:
        for row_index, row in enumerate(seats):
            for col_index, seat_class in enumerate(row):
                if seat_class:  # 좌석 클래스가 있는 경우에만 처리
                    seat_data = SeatCreate(
                        show_detail_id=show_detail_id,
                        seat_row=row_index + 1,  # 행 번호 (1부터 시작)
                        seat_column=col_index + 1,  # 열 번호 (1부터 시작)
                        seat_class=seat_class
                    )
                    create_seat(db, seat_data)
        return {"detail": "Seats registered successfully"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# 10. Schedule 등록
@router.post("/shows/{show_detail_id}/schedule")
def create_schedule_route(
    show_detail_id: int,
    schedules: List[ShowScheduleCreate],
    db: Session = Depends(get_db)
):
    for schedule in schedules:
        schedule.show_detail_id = show_detail_id
        create_show_schedule(db, schedule)
    return {"detail": "Schedules registered"}

# 11. Schedule 수정
@router.patch("/shows/schedule/{schedule_id}")
def update_schedule_route(
    schedule_id: int,
    schedule_update: ShowScheduleUpdate,
    db: Session = Depends(get_db)
):
    schedule = update_show_schedule(db, schedule_id, schedule_update)
    if not schedule:
        raise HTTPException(status_code=404, detail="Schedule not found")
    return {"detail": "Schedule updated successfully"}

# 12. Schedule 삭제
@router.delete("/shows/schedule/{schedule_id}")
def delete_schedule_route(
    schedule_id: int,
    db: Session = Depends(get_db)
):
    success = delete_show_schedule(db, schedule_id)
    if not success:
        raise HTTPException(status_code=404, detail="Schedule not found")
    return {"detail": "Schedule deleted successfully"}

# 13. 이미지 업로드
@router.post("/shows/{show_detail_id}/image")
def upload_image_route(
    show_detail_id: int,
    image: UploadFile = File(...),
    db: Session = Depends(get_db)
):
    image_path = save_image(image)
    show_detail_update = ShowDetailUpdate(show_detail_poster_image_path=image_path)
    update_show_detail(db, show_detail_id, show_detail_update)
    return {"detail": "Image uploaded and path updated"}