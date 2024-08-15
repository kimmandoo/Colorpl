from typing import List
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from crud.show_detail import create_show_detail, get_all_shows, get_show_detail_by_id, get_show_detail_by_api_id, search_show_details
from schemas.show_detail import ShowDetailCreate, ShowDetailResponse, ShowDetailSearch
from database import get_db

router = APIRouter()

@router.get("/shows", response_model=List[ShowDetailResponse])
def get_shows(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    shows = get_all_shows(db, skip=skip, limit=limit)
    return shows

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
def search_shows(search_data: ShowDetailSearch, db: Session = Depends(get_db)):
    shows = search_show_details(db, search_data)
    if not shows:
        raise HTTPException(status_code=404, detail="No shows found with the given criteria")
    return shows
