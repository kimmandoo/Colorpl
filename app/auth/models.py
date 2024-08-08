from sqlalchemy import Column, String, Integer, Boolean, ForeignKey, DateTime
from sqlalchemy.orm import relationship
from .database import Base
import uuid
from datetime import datetime

class Administrator(Base):
    __tablename__ = "administrators"
    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), unique=True, index=True)
    username = Column(String(100), unique=True, index=True)
    email = Column(String(200))
    hashed_password = Column(String(512))
    role = Column(Integer, default=3)  # 기본 역할은 sub-admin
    status = relationship("AdministratorStatus", uselist=False, back_populates="admin")
    profile = relationship("AdminProfile", uselist=False, back_populates="admin")

class AdministratorStatus(Base):
    __tablename__ = "administrator_status"
    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    admin_id = Column(String(36), ForeignKey('administrators.id'), unique=True)
    approved = Column(Boolean, default=False)
    approval_requested_at = Column(DateTime, nullable=True)
    approved_at = Column(DateTime, nullable=True)
    deleted = Column(Boolean, default=False)
    delete_requested_at = Column(DateTime, nullable=True)
    deleted_at = Column(DateTime, nullable=True)
    admin = relationship("Administrator", back_populates="status")

class AdminProfile(Base):
    __tablename__ = "admin_profiles"
    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    admin_id = Column(String(36), ForeignKey('administrators.id'), unique=True)
    image_url = Column(String(512), nullable=True)
    admin = relationship("Administrator", back_populates="profile")