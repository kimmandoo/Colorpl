from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from utils.config import ec2

DATABASE_URL = f"mysql+pymysql://{ec2.EC2_DB_ID}:{ec2.EC2_DB_PW}@{ec2.EC2_DB_HOST}:{ec2.EC2_DB_PORT}/{ec2.EC2_DB_NAME}"

engine = create_engine(DATABASE_URL)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()