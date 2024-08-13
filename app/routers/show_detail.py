from typing import List
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.crud.show_detail import create_show_detail, get_show_detail_by_id, get_show_detail_by_api_id, get_show_details_by_name
from app.schemas.show_detail import ShowDetailCreate, ShowDetailResponse, ShowDetailSearch
from app.database import get_db

router = APIRouter()

@router.post("/shows", response_model=ShowDetailResponse)
def create_show_detail_route(show_detail: ShowDetailCreate, db: Session = Depends(get_db)):
    existing_show = get_show_detail_by_api_id(db, show_detail.show_detail_api_id)
    if existing_show:
        raise HTTPException(status_code=400, detail="Show already exists")
    new_show = create_show_detail(db, show_detail)
    return new_show

@router.get("/shows/{show_detail_id}", response_model=ShowDetailResponse)
def get_show_detail_route(show_detail_id: int, db: Session = Depends(get_db)):
    show_detail = get_show_detail_by_id(db, show_detail_id)
    if not show_detail:
        raise HTTPException(status_code=404, detail="Show not found")
    return show_detail

@router.post("/shows/search", response_model=List[ShowDetailResponse])
def search_shows_by_name(search_data: ShowDetailSearch, db: Session = Depends(get_db)):
    shows = get_show_details_by_name(db, search_data.show_detail_name)
    if not shows:
        raise HTTPException(status_code=404, detail="No shows found with that name")
    return shows