from fastapi import Depends, HTTPException
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.orm import Session
from database import get_db
from .models import Administrator
from utils.config import security_settings
from jose import JWTError, jwt
from auth.schemas import TokenData

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/token")

def get_current_administrator(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    try:
        payload = jwt.decode(token, security_settings.SECRET_KEY, algorithms=[security_settings.ALGORITHM])
        username = payload.get("sub")
        if username is None:
            raise HTTPException(status_code=401, detail="Invalid token")
        token_data = TokenData(username=username)
    except JWTError:
        raise HTTPException(status_code=401, detail="Invalid token")

    admin = db.query(Administrator).filter(Administrator.username == token_data.username).first()
    if admin is None:
        raise HTTPException(status_code=401, detail="Administrator not found")
    return admin

def super_admin_only(current_administrator: Administrator = Depends(get_current_administrator)):
    if current_administrator.role != 1:
        raise HTTPException(status_code=403, detail="Not enough permissions")
    return current_administrator

def chief_or_super_admin(current_administrator: Administrator = Depends(get_current_administrator)):
    if current_administrator.role not in [1, 2]:
        raise HTTPException(status_code=403, detail="Not enough permissions")
    return current_administrator

def admin_only(current_administrator: Administrator = Depends(get_current_administrator)):
    if current_administrator.role not in [1, 2, 3]:
        raise HTTPException(status_code=403, detail="Not enough permissions")
    return current_administrator
