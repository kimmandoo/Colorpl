from passlib.context import CryptContext
from typing import Optional
from datetime import datetime, timedelta
from fastapi import Depends, HTTPException
from fastapi.security import OAuth2PasswordBearer
from jose import JWTError, jwt
from sqlalchemy.orm import Session
from .models import Administrator, AdministratorStatus, AdminProfile
from .database import get_db
from utils.config import settings
from utils.config import security_settings
import os

SECRET_KEY = security_settings.SECRET_KEY
ALGORITHM = security_settings.ALGORITHM
ACCESS_TOKEN_EXPIRE_MINUTES = security_settings.ACCESS_TOKEN_EXPIRE_MINUTES

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/token")

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
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

TOKEN_BLACKLIST = set()

def verify_token(token: str):
    if token in TOKEN_BLACKLIST:
        raise HTTPException(
            status_code=401,
            detail="Token has been revoked",
            headers={"WWW-Authenticate": "Bearer"},
        )
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
        return username
    except JWTError:
        raise credentials_exception
    
def get_current_user(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)) -> str:
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
        return admin.username
    except JWTError:
        raise credentials_exception


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

def create_super_admin():
    db = next(get_db())
    superadmin_username = settings.SUPERADMIN_USERNAME
    superadmin_email = settings.SUPERADMIN_EMAIL
    superadmin_password = settings.SUPERADMIN_PASSWORD
    
    existing_admin = db.query(Administrator).filter(Administrator.username == superadmin_username).first()
    if not existing_admin:
        hashed_password = get_password_hash(superadmin_password)
        super_admin = Administrator(
            username=superadmin_username,
            email=superadmin_email,
            hashed_password=hashed_password,
            role=1,
            # approved=True
        )
        db.add(super_admin)
        db.commit()
        db.refresh(super_admin)

        super_admin_status = AdministratorStatus(
            admin_id=super_admin.id,
            approved=True,
            approved_at=datetime.utcnow()
        )
        db.add(super_admin_status)
        db.commit()

        super_admin_profile = AdminProfile(
            admin_id=super_admin.id,
            image_url=None
        )
        db.add(super_admin_profile)
        db.commit()

        print("Super admin created")
    else:
        print("Super admin already exists")
    db.close()
