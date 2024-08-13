from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from typing import List
from app.database import get_db
from app.schemas.member import MemberDetail, MemberUpdateDTO, MemberActivity, MemberSearch
from app.crud import member as crud_member
# from datetime import datetime

router = APIRouter()

@router.get("/members/activity", response_model=List[MemberActivity])
async def get_members(db: Session = Depends(get_db), skip: int = 0, limit: int = 10):
    return crud_member.get_members_activity(db, skip=skip, limit=limit)

@router.patch("/members/{member_id}", response_model=MemberDetail)
def update_member(member_id: int, member_update: MemberUpdateDTO, db: Session = Depends(get_db)):
    member = crud_member.get_member(db, member_id)
    if not member:
        raise HTTPException(status_code=404, detail="Member not found")

    updated_data = member_update.dict(exclude_unset=True)
    for key, value in updated_data.items():
        setattr(member, key, value)
    # member.update_date = datetime.utcnow()
    db.commit()
    db.refresh(member)
    return member

@router.post("/members/search", response_model=List[MemberActivity])
def search_member(search: MemberSearch, skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    members = crud_member.search_member(db, search, skip, limit)
    if not members:
        raise HTTPException(status_code=404, detail="Members not found")
    return members
    
@router.get("/members/{member_id}", response_model=MemberDetail)
def read_member(member_id: int, db: Session = Depends(get_db)):
    member = crud_member.get_member(db, member_id)
    if not member:
        raise HTTPException(status_code=404, detail="Member not found")
    return member