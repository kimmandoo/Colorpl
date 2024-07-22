from datetime import datetime, timedelta, timezone
from typing import Annotated
import jwt
from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from jwt.exceptions import InvalidTokenError
from passlib.context import CryptContext
from pydantic import BaseModel
from sqlalchemy.orm import Session
from models import Administrator
from database import get_db


# to get a string like this run:
# openssl rand -hex 32
SECRET_KEY = "09d25e094faa6ca2556c818166b7a9563b93f7099f6f0f4caa6cf63b88e8d3e7"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30

class Token(BaseModel):
    access_token: str
    token_type: str

class TokenData(BaseModel):
    administrator_name: str | None = None

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def get_password_hash(password):
    return pwd_context.hash(password)

def get_administrator(db: Session, administrator_name: str):
    return db.query(Administrator).filter(Administrator.administrator_name == administrator_name).first()

def authenticated_administrator(db: Session, administrator_name: str, password: str):
    administrator = get_administrator(db, administrator_name)
    if not administrator:
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
        detail="could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        administrator_name: str = payload.get("sub")
        if administrator_name is None:
            raise credentials_exception
        token_data = TokenData(administrator_name=administrator_name)
    except InvalidTokenError:
        raise credentials_exception
    administrator = get_administrator(db, administrator_name=token_data.administrator_name)
    if administrator is None:
        raise credentials_exception
    return administrator

async def get_current_active_administrator(
    currnet_administrator: Annotated[Administrator, Depends(get_current_administrator)]
):
    return currnet_administrator