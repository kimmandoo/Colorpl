from datetime import datetime
from sqlalchemy import Column, DateTime
from sqlalchemy.ext.declarative import as_declarative, declared_attr

@as_declarative()
class CustomBase():
    
    @declared_attr
    def __tablename__(cls) -> str:
        return cls.__name__.lower()
    
    created_at = Column(DateTime, default=datetime.now)
    updated_at = Column(DateTime, default=datetime.now, onupdate=datetime.now)