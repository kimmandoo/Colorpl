from sqlalchemy import Column, Integer, String, Boolean
from database import Base

class Administrator(Base):
    __tablename__ = 'Administrators'

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    hashed_password = Column(String(512))
    email = Column(String(200), unique=True)
    administrator_name = Column(String(100), unique=True, index=True)
    administrator_grade = Column(Integer, nullable=False)
    is_approved = Column(Boolean, default=False)
