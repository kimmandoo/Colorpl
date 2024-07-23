import os
from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.middleware.cors import CORSMiddleware
from starlette.middleware.sessions import SessionMiddleware
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session
from datetime import timedelta
from uuid import UUID
from dotenv import load_dotenv

from database import Base, engine, get_db
from schemas import AdministratorCreate, AdministratorResponse, AdministratorUpdate, Token
from models import Administrator
from auth_utils import (
    authenticate_administrator, create_access_token, get_current_active_administrator, get_super_admin, get_password_hash,
    ACCESS_TOKEN_EXPIRE_MINUTES
)

load_dotenv()

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.add_middleware(SessionMiddleware, secret_key=os.getenv("SECRET_KEY"))

Base.metadata.create_all(bind=engine)

@app.get("/")
async def read_root():
    return {"message": "Welcome to the FastAPI application!"}

@app.post("/token", response_model=Token)
async def login_for_access_token(
    form_data: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)
):
    administrator = authenticate_administrator(db, form_data.username, form_data.password)
    if not administrator:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password or account not approved",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": administrator.administrator_name}, expires_delta=access_token_expires
    )
    return Token(access_token=access_token, token_type="bearer")

@app.post("/administrators/", response_model=AdministratorResponse)
async def create_administrator(
    administrator: AdministratorCreate, db: Session = Depends(get_db)
):
    db_administrator = db.query(Administrator).filter(
        (Administrator.administrator_name == administrator.administrator_name) | 
        (Administrator.email == administrator.email)
    ).first()
    if db_administrator:
        raise HTTPException(status_code=400, detail="Username or email already registered")
    hashed_password = get_password_hash(administrator.password1)
    new_administrator = Administrator(
        administrator_name=administrator.administrator_name,
        email=administrator.email,
        hashed_password=hashed_password,
        administrator_grade=0,
        is_approved=False
    )
    db.add(new_administrator)
    db.commit()
    db.refresh(new_administrator)
    return AdministratorResponse.from_orm(new_administrator)

@app.put("/administrators/{admin_id}", response_model=AdministratorResponse)
async def approve_administrator(
    admin_id: UUID, 
    admin_update: AdministratorUpdate, 
    current_administrator: Administrator = Depends(get_super_admin), 
    db: Session = Depends(get_db)
):
    admin_to_update = db.query(Administrator).filter(Administrator.id == admin_id).first()
    if not admin_to_update:
        raise HTTPException(status_code=404, detail="Administrator not found")
    admin_to_update.is_approved = admin_update.is_approved
    admin_to_update.administrator_grade = admin_update.administrator_grade
    db.commit()
    db.refresh(admin_to_update)
    return AdministratorResponse.from_orm(admin_to_update)

@app.get("/administrators/me/", response_model=AdministratorResponse)
async def read_administrators_me(
    current_administrator: Administrator = Depends(get_current_active_administrator)
):
    return AdministratorResponse.from_orm(current_administrator)

@app.get("/administrators/me/items/")
async def read_own_items(
    current_administrator: Administrator = Depends(get_current_active_administrator)
):
    return [{"item_id": "Foo", "owner": current_administrator.administrator_name}]
