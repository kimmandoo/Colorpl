from pydantic import BaseModel
from typing import Optional
from datetime import datetime

class AdministratorCreate(BaseModel):
    username: str
    email: str
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str

class TokenData(BaseModel):
    username: str

class UpdateProfileImage(BaseModel):
    image_url: Optional[str]

class AdminProfile(BaseModel):
    image_url: Optional[str]

    class Config:
        from_attributes = True

class AdministratorStatus(BaseModel):
    approved: bool
    approval_requested_at: Optional[datetime]
    approved_at: Optional[datetime]
    deleted: bool
    delete_requested_at: Optional[datetime]
    deleted_at: Optional[datetime]

    class Config:
        from_attributes = True

class Administrator(BaseModel):
    id: str
    username: str
    email: str
    role: int
    profile: Optional[AdminProfile]
    status: Optional[AdministratorStatus]

    class Config:
        from_attributes = True