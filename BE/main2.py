import uuid
from sqlalchemy import create_engine, Column, Integer, String, Boolean
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, relationship, Session
from pydantic import BaseModel
from passlib.context import CryptContext
from typing import Optional
from datetime import datetime, timedelta
from fastapi import FastAPI, Depends, HTTPException, Request
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from jose import JWTError, jwt
from dotenv import load_dotenv
from starlette.middleware.sessions import SessionMiddleware
from starlette.middleware.cors import CORSMiddleware
import os
from urllib.parse import quote_plus

load_dotenv()

SECRET_KEY = os.getenv('SECRET_KEY')
ALGORITHM = 'HS256'
ACCESS_TOKEN_EXPIRE_MINUTES = 30

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

def get_password_hash(password):
    return pwd_context.hash(password)

def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def create_access_token(data: dict, expires_delta: Optional[timedelta] = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    to_encode.update({"exp": expire})
    encode_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encode_jwt

def verify_token(token: str):
    credentials_exception = HTTPException(
        status_code=401,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        if username is None:
            raise credentials_exception
    except JWTError:
        raise credentials_exception
    return username

app = FastAPI()

app.add_middleware(SessionMiddleware, secret_key=SECRET_KEY)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

DATABASE_USER = os.getenv('DATABASE_USER')
DATABASE_PASSWORD = quote_plus(os.getenv('DATABASE_PASSWORD'))
DATABASE_HOST = os.getenv('DATABASE_HOST')
DATABASE_NAME = os.getenv('DATABASE_NAME')
DATABASE_URL = f"mysql+pymysql://{DATABASE_USER}:{DATABASE_PASSWORD}@{DATABASE_HOST}/{DATABASE_NAME}"
engine = create_engine(DATABASE_URL)
Base = declarative_base()

class Administrator(Base):
    __tablename__ = "administrators"
    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), unique=True, index=True)
    username = Column(String(100), unique=True, index=True)
    email = Column(String(200))
    hashed_password = Column(String(512))
    role = Column(Integer, default=3)  # 기본 역할은 sub-admin
    approved = Column(Boolean, default=False)  # 승인 상태 필드

class AdministratorCreate(BaseModel):
    username: str
    email: str
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base.metadata.create_all(bind=engine)

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

def get_current_user_role(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)) -> str:
    credentials_exception = HTTPException(
        status_code=401,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        if username is None:
            raise credentials_exception
        admin = db.query(Administrator).filter(Administrator.username == username).first()
        if admin is None:
            raise credentials_exception
        return admin.role
    except JWTError:
        raise credentials_exception

def super_admin_required(role: int = Depends(get_current_user_role)):
    if role != 1:
        raise HTTPException(status_code=403, detail="Super Admin access required")

@app.post("/signup")
async def signup(signup_data: AdministratorCreate, db: Session = Depends(get_db)):
    hashed_password = get_password_hash(signup_data.password)
    new_admin = Administrator(
        username=signup_data.username, 
        email=signup_data.email, 
        hashed_password=hashed_password,
        approved=False  # 승인 대기 상태로 설정
    )
    db.add(new_admin)
    db.commit()
    db.refresh(new_admin)
    return {"message": "Account created successfully, waiting for approval", "user_id": new_admin.id}

@app.post("/token", response_model=Token)
async def login_for_access_token(form_data: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)):
    admin = db.query(Administrator).filter(Administrator.username == form_data.username).first()
    if admin is None or not verify_password(form_data.password, admin.hashed_password):
        raise HTTPException(
            status_code=401,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    if not admin.approved:
        raise HTTPException(status_code=403, detail="Administrator not approved")
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": admin.username}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}

@app.get("/administrators/me")
async def read_administrator_me(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    username = verify_token(token)
    admin = db.query(Administrator).filter(Administrator.username == username).first()
    if admin is None:
        raise HTTPException(status_code=404, detail="Administrator not found")
    return admin

@app.get("/admin/administrators", dependencies=[Depends(super_admin_required)])
async def list_administrators(db: Session = Depends(get_db)):
    admins = db.query(Administrator).all()
    return admins

@app.post("/admin/administrators/{admin_id}/approve", dependencies=[Depends(super_admin_required)])
async def approve_administrator(admin_id: str, role: int, db: Session = Depends(get_db)):
    admin = db.query(Administrator).filter(Administrator.id == admin_id).first()
    if admin is None:
        raise HTTPException(status_code=404, detail="Administrator not found")
    
    admin.approved = True
    admin.role = role  # role을 설정
    db.commit()
    return {"message": f"Administrator {admin.username} approved and assigned role {role}"}

@app.get("/admin/administrators/{admin_id}", dependencies=[Depends(super_admin_required)])
async def get_administrator(admin_id: str, db: Session = Depends(get_db)):
    admin = db.query(Administrator).filter(Administrator.id == admin_id).first()
    if admin is None:
        raise HTTPException(status_code=404, detail="Administrator not found")
    return admin

@app.get('/')
async def read_root(request: Request):
    return {"message": "Hello, world"}
