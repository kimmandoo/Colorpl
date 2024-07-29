from fastapi import Depends, HTTPException
from fastapi_jwt_auth import AuthJWT
from sqlalchemy.orm import Session
from .database import get_db
from .models import Administrator

def get_current_administrator(Authorize: AuthJWT = Depends(), db: Session = Depends(get_db)):
    try:
        Authorize.jwt_required()
    except Exception as e:
        raise HTTPException(status_code=401, detail="Invalid authorization")
    
    admin_id = Authorize.get_jwt_subject()
    admin = db.query(Administrator).filter(Administrator.admin_id == admin_id).first()

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