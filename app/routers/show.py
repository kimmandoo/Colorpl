from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from app.crud.show import get_show_table
from app.schemas.show import ShowSummary
from app.database import get_db

router = APIRouter()

@router.get("/shows", response_model=list[ShowSummary])
def read_show_table(
    db: Session = Depends(get_db), skip: int = 0, limit: int = 10):
    return get_show_table(db, skip=skip, limit=limit)