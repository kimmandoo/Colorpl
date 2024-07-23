from datetime import datetime, timedelta, timezone
from typing import Annotated
import jwt
from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from jwt.exceptions import InvalidTokenError
from passlib.context import CryptContext
from sqlalchemy.orm import Session
from models import Administrator
from database import get_db
import os

SECRET_KEY = os.getenv("SECRET_KEY")
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def get_password_hash(password):
    return pwd_context.hash(password)

def get_administrator(db: Session, administrator_name: str):
    return db.query(Administrator).filter(Administrator.administrator_name == administrator_name).first()

def authenticate_administrator(db: Session, administrator_name: str, password: str):
    administrator = get_administrator(db, administrator_name)
    if not administrator or not administrator.is_approved:
        return False
    if not verify_password(password, administrator.hashed_password):
        return False
    return administrator

def create_access_token(data: dict, expires_delta: timedelta | None = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.now(timezone.utc) + expires_delta
    else:
        expire = datetime.now(timezone.utc) + timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

async def get_current_administrator(token: Annotated[str, Depends(oauth2_scheme)], db: Session = Depends(get_db)):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        administrator_name: str = payload.get("sub")
        if administrator_name is None:
            raise credentials_exception
    except InvalidTokenError:
        raise credentials_exception
    administrator = get_administrator(db, administrator_name)
    if administrator is None:
        raise credentials_exception
    return administrator

async def get_current_active_administrator(
    current_administrator: Annotated[Administrator, Depends(get_current_administrator)]
):
    if not current_administrator.is_approved:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Inactive administrator")
    return current_administrator

def get_super_admin(current_administrator: Administrator):
    if current_administrator.administrator_grade != 1:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Not authorized")
    return current_administrator
