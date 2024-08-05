from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List, Optional
from cs2 import crud, schemas
from cs2.database import get_db
from common.dependencies import admin_only, chief_or_super_admin, super_admin_only

router = APIRouter()

@router.get("/tickets", response_model=List[schemas.Ticket], dependencies=[Depends(chief_or_super_admin)])
def read_tickets(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    tickets = crud.get_tickets_all(db, skip=skip, limit=limit)
    return tickets

@router.get("/tickets/{ticket_id}", response_model=schemas.Ticket, dependencies=[Depends(chief_or_super_admin)])
def read_ticket(ticket_id: int, db: Session = Depends(get_db)):
    db_ticket = crud.get_ticket(db, ticket_id=ticket_id)
    if db_ticket is None:
        raise HTTPException(status_code=404, detail="Ticket not found")
    return db_ticket

@router.get("/tickets/{ticket_id}/activity", response_model=schemas.TicketActivity, dependencies=[Depends(chief_or_super_admin)])
def read_ticket_activity(ticket_id: int, skip: int = 0, limit: int = 5, db: Session = Depends(get_db)):
    ticket_activity = crud.get_ticket_activity(db, ticket_id=ticket_id, skip=skip, limit=limit)
    if ticket_activity is None:
        raise HTTPException(status_code=404, detail="Ticket not found")
    return ticket_activity

@router.post("/tickets/search", response_model=List[schemas.Ticket], dependencies=[Depends(chief_or_super_admin)])
def search_tickets(email: Optional[str] = None, nickname: Optional[str] = None, category: Optional[str] = None, skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    tickets = crud.search_tickets(db, email=email, nickname=nickname, category=category, skip=skip, limit=limit)
    return tickets
