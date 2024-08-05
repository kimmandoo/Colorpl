from fastapi import APIRouter, Depends, HTTPException, UploadFile, File
from sqlalchemy.orm import Session
from .database import get_db
from .models import Administrator, AdministratorStatus, AdminProfile
from .schemas import AdministratorCreate, Token
from .schemas import Administrator as AdministratorSchema
from .utils import (
    create_access_token,
    get_password_hash, 
    verify_password, 
    oauth2_scheme, 
    get_current_user,
    get_current_user_role,
    super_admin_required, 
    verify_token, 
    TOKEN_BLACKLIST
)
from fastapi.security import OAuth2PasswordRequestForm, OAuth2PasswordBearer
from datetime import timedelta, datetime
from common.security import security_settings
import shutil, os, re, hashlib
from jose import jwt

ACCESS_TOKEN_EXPIRE_MINUTES = security_settings.ACCESS_TOKEN_EXPIRE_MINUTES

router = APIRouter()

@router.post("/admin/signup")
async def signup(signup_data: AdministratorCreate, db: Session = Depends(get_db)):
    existing_admin = db.query(Administrator).filter(Administrator.username == signup_data.username).first()
    if existing_admin:
        raise HTTPException(
            status_code=400,
            detail="Username already registered"
        )
    
    hashed_password = get_password_hash(signup_data.password)
    new_admin = Administrator(
        username=signup_data.username, 
        email=signup_data.email, 
        hashed_password=hashed_password,
        # approved=False
    )
    db.add(new_admin)
    db.commit()

    new_status = AdministratorStatus(
        admin_id=new_admin.id
    )
    db.add(new_status)
    db.commit()

    new_profile = AdminProfile(
        admin_id=new_admin.id
    )
    db.add(new_profile)
    db.commit()
    # db.refresh(new_admin)
    return {"message": "Account created successfully, waiting for approval", "user_id": new_admin.id}

@router.post("/token", response_model=Token)
async def login_for_access_token(form_data: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)):
    admin = db.query(Administrator).filter(Administrator.username == form_data.username).first()
    if admin is None or not verify_password(form_data.password, admin.hashed_password):
        raise HTTPException(
            status_code=401,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    if not verify_password(form_data.password, admin.hashed_password):
        print("Invalid password")
        raise HTTPException(
            status_code=401,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    if not admin.status.approved:
        raise HTTPException(status_code=403, detail="Administrator not approved")
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": admin.username, "role": admin.role}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}

# admin

@router.post("/admin/delete_request")
async def request_delete_account(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    username = get_current_user(token, db)
    admin = db.query(Administrator).filter(Administrator.username == username).first()
    if admin is None:
        raise HTTPException(status_code=404, detail="Administrator not found")
    
    if admin.status is None:
        raise HTTPException(status_code=404, detail="Administrator status not found")

    if admin.status.delete_requested_at is not None:
        raise HTTPException(status_code=400, detail="Delete request already exists")

    admin.status.delete_requested_at = datetime.utcnow()
    db.commit()

    return {"message": "Account deletion requested"}

@router.post("/admin/approve_delete/{admin_id}")
async def approve_delete_account(admin_id: str, token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    role = get_current_user_role(token, db)
    if role != 1:
        raise HTTPException(status_code=403, detail="Super Admin access required")
    status = db.query(AdministratorStatus).filter(AdministratorStatus.admin_id == admin_id).first()
    if status is None:
        raise HTTPException(status_code=404, detail="Administrator status not found")
    if status.deleted:
        raise HTTPException(status_code=400, detail="Administrator already deleted")

    status.deleted = True
    status.deleted_at = datetime.utcnow()
    db.commit()

    return {"message": f"Administrator {admin_id} has been deleted"}    

# /profile

@router.post("/profile/upload_image")
async def upload_profile_image(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db), file: UploadFile = File(...)):
    username = get_current_user(token, db)
    admin = db.query(Administrator).filter(Administrator.username == username).first()
    if admin is None:
        raise HTTPException(status_code=404, detail="Administrator not found")
    
    save_directory = "profile_images"
    if not os.path.exists(save_directory):
        try:
            os.makedirs(save_directory)
        except OSError as e:
            raise HTTPException(status_code=500, detail=f"Failed to create directory: {e}")

    original_filename = file.filename
    safe_filename = re.sub(r'[^\w\-_\. ]', '_', original_filename)
    
    if len(safe_filename) > 100: 
        hash_object = hashlib.md5(safe_filename.encode())
        safe_filename = hash_object.hexdigest() + os.path.splitext(safe_filename)[1]

    file_path = os.path.join(save_directory, f"{admin.id}_{safe_filename}")

    try:
        with open(file_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to save file: {e}")
    
    if admin.profile is None:
        admin.profile = AdminProfile(admin_id=admin.id, image_url=file_path)
    else:
        admin.profile.image_url = file_path
    
    db.commit()

    return {"message": "Profile image uploaded successfully", "image_url": file_path}

@router.put("/profile/update_image")
async def update_profile_image(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db), file: UploadFile = File(...)):
    username = get_current_user(token, db)
    admin = db.query(Administrator).filter(Administrator.username == username).first()
    if admin is None:
        raise HTTPException(status_code=404, detail="Administrator not found")
    
    save_directory = "profile_images"
    if not os.path.exists(save_directory):
        try:
            os.makedirs(save_directory)
        except OSError as e:
            raise HTTPException(status_code=500, detail=f"Failed to create directory: {e}")
    
    original_filename = file.filename
    safe_filename = re.sub(r'[^\w\-_\. ]', '_', original_filename)
    
    if len(safe_filename) > 100: 
        hash_object = hashlib.md5(safe_filename.encode())
        safe_filename = hash_object.hexdigest() + os.path.splitext(safe_filename)[1]

    file_path = os.path.join(save_directory, f"{admin.id}_{safe_filename}")

    try:
        if admin.profile and admin.profile.image_url:
            if os.path.exists(admin.profile.image_url):
                os.remove(admin.profile.image_url)

        with open(file_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to save file: {e}")
    
    if admin.profile is None:
        admin.profile = AdminProfile(admin_id=admin.id, image_url=file_path)
    else:
        admin.profile.image_url = file_path
    
    db.commit()

    return {"message": "Profile image updated successfully", "image_url": file_path}

# /administrators

@router.get("/administrators/me", response_model=AdministratorSchema)
async def read_administrator_me(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    username = get_current_user(token, db)
    admin = db.query(Administrator).filter(Administrator.username == username).first()
    if admin is None:
        raise HTTPException(status_code=404, detail="Administrator not found")
    return admin
    # return AdministratorSchema.from_orm(admin)

@router.get("/admin/administrators", dependencies=[Depends(super_admin_required)])
async def list_administrators(db: Session = Depends(get_db)):
    admins = db.query(Administrator).all()
    return admins

@router.put("/admin/administrators/{admin_id}/approve", dependencies=[Depends(super_admin_required)])
async def approve_administrator(admin_id: str, db: Session = Depends(get_db)):
    admin = db.query(Administrator).filter(Administrator.id == admin_id).first()
    if admin is None:
        raise HTTPException(status_code=404, detail="Administrator not found")
    
    admin.status.approved = True
    admin.status.approved_at = datetime.utcnow()
    db.commit()

    return {"message": f"Administrator {admin.username} approved"}

@router.put("/admin/administrators/{admin_id}/role", dependencies=[Depends(super_admin_required)])
async def approve_administrator(admin_id: str, role: int, db: Session = Depends(get_db)):
    admin = db.query(Administrator).filter(Administrator.id == admin_id).first()
    if admin is None:
        raise HTTPException(status_code=404, detail="Administrator not found")
    
    admin.role = role
    db.commit()

    return {"message": f"Administrator {admin.username} approved assigned role {role}"}


@router.get("/admin/administrators/{admin_id}", dependencies=[Depends(super_admin_required)])
async def get_administrator(admin_id: str, db: Session = Depends(get_db)):
    admin = db.query(Administrator).filter(Administrator.id == admin_id).first()
    if admin is None:
        raise HTTPException(status_code=404, detail="Administrator not found")
    return admin

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/token")

@router.post("/logout")
async def logout(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    try:
        payload = jwt.decode(token, security_settings.SECRET_KEY, algorithms=[security_settings.ALGORITHM])
        TOKEN_BLACKLIST.add(token)
        return {"message": "Logged out successfully"}
    except Exception as e:
        raise HTTPException(status_code=401, detail="Invalid token")