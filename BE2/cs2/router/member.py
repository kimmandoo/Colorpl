from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List, Optional
from pydantic import BaseModel
from cs2 import crud, schemas
from cs2.database import SessionLocal
from common.dependencies import super_admin_only

router = APIRouter()

class MemberSearchRequest(BaseModel):
    email: Optional[str] = None
    nickname: Optional[str] = None
    category: Optional[str] = None
    skip: int = 0
    limit: int = 10

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.get("/members/", response_model=List[schemas.Member], dependencies=[Depends(super_admin_only)])
def read_members(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    members = crud.get_members(db, skip=skip, limit=limit)
    return members

@router.get("/members/{member_id}", response_model=schemas.Member, dependencies=[Depends(super_admin_only)])
def read_member(member_id: int, db: Session = Depends(get_db)):
    db_member = crud.get_member(db, member_id=member_id)
    if db_member is None:
        raise HTTPException(status_code=404, detail="Member not found")
    return db_member

@router.get("/members/{member_id}/activity", response_model=schemas.MemberActivity, dependencies=[Depends(super_admin_only)])
def read_member_activity(member_id: int, skip: int = 0, limit: int = 5, db: Session = Depends(get_db)):
    member_activity = crud.get_member_activity(db, member_id=member_id, skip=skip, limit=limit)
    if member_activity is None:
        raise HTTPException(status_code=404, detail="Member not found")
    return member_activity

@router.post("/members/search", response_model=List[schemas.Member], dependencies=[Depends(super_admin_only)])
def search_members(request: MemberSearchRequest, db: Session = Depends(get_db)):
    members = crud.search_members(db, email=request.email, nickname=request.nickname, category=request.category, skip=request.skip, limit=request.limit)
    return members
