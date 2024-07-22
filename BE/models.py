from sqlalchemy import Column, Integer, String, Float, Boolean, TEXT, TIMESTAMP, func
from .database import Base
from .base import CustomBase

class Administrator(CustomBase):
    __tablename__ = 'Administrators'

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    hashed_password = Column(String(512))
    email = Column(String(200), unique=True)
    administrator_name = Column(String(100), unique=True, index=True)
    administrator_grade = Column(Integer, nullable=False)
