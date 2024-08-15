from typing import List
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from schemas.theater_hall import HallResponse, TheaterCreate, TheaterSearchRequest, TheaterUpdate, TheaterResponse
from crud import theater_hall as crud_theater_hall
from database import get_db

router = APIRouter()

@router.post("/theaters", response_model=TheaterResponse)
def create_theater(theater: TheaterCreate, db: Session = Depends(get_db)):
    existing_theater = crud_theater_hall.get_theater_by_api_id(db, theater.theater_api_id)
    if existing_theater:
        raise HTTPException(status_code=400, detail="already exists")
    return crud_theater_hall.create_theater(db, theater)

@router.get("/theaters", response_model=List[TheaterResponse])
def get_all_theaters(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    theaters = crud_theater_hall.get_all_theaters(db, skip=skip, limit=limit)
    return theaters

@router.get("/theaters/{theater_id}", response_model=TheaterResponse)
def get_theater(theater_id: int, db: Session = Depends(get_db)):
    theater = crud_theater_hall.get_theater(db, theater_id)
    if not theater:
        raise HTTPException(status_code=404, detail="Theater not found")
    return theater

@router.put("/theaters/{theater_id}", response_model=TheaterResponse)
def update_theater(theater_id: int, theater_update: TheaterUpdate, db: Session = Depends(get_db)):
    theater = crud_theater_hall.update_theater(db, theater_id, theater_update)
    if not theater:
        raise HTTPException(status_code=404, detail="Theater not found")
    return theater

@router.delete("/theaters/{theater_id}")
def delete_theater(theater_id: int, db: Session = Depends(get_db)):
    theater = crud_theater_hall.get_theater(db, theater_id)
    if not theater:
        raise HTTPException(status_code=404, detail="Theater not found")
    crud_theater_hall.delete_theater(db, theater_id)
    return {"message": "Theater deleted successfully"}


@router.get("/theaters/{theater_id}/halls", response_model=List[HallResponse])
def get_halls_by_theater_route(theater_id: int, db: Session = Depends(get_db)):
    halls = crud_theater_hall.get_halls_by_theater(db, theater_id)
    if not halls:
        raise HTTPException(status_code=404, detail="Halls not found")
    return halls

@router.post("/theaters/search", response_model=List[TheaterResponse])
def search_theaters_route(search: TheaterSearchRequest, db: Session = Depends(get_db)):
    theaters = crud_theater_hall.search_theaters_by_name(db, search.theater_name, search.skip, search.limit)
    if not theaters:
        raise HTTPException(status_code=404, detail="No theaters found with the given name")
    return theaters