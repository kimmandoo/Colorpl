from sqlalchemy import Column, Integer, String, DateTime, ForeignKey, SmallInteger, Boolean, BigInteger, DECIMAL, CHAR, TINYINT, VARCHAR
from sqlalchemy.orm import relationship
from .database import Base

class Category(Base):
    __tablename__ = "CATEGORY"
    CATEGORY_ID = Column(Integer, primary_key=True, index=True)
    CATEGORY_NAME = Column(VARCHAR(64), nullable=False)

class ShowCategory(Base):
    __tablename__ = "SHOW_CATEGORY"
    CATEGORY_ID = Column(TINYINT, ForeignKey('CATEGORY.CATEGORY_ID'), primary_key=True, nullable=False)
    SHOW_ID = Column(Integer, ForeignKey('SHOW.SHOW_ID'), primary_key=True, nullable=False)

class Theater(Base):
    __tablename__ = "THEATER"
    THEATER_ID = Column(Integer, primary_key=True, index=True)
    THEATER_APL_ID = Column(CHAR(8), nullable=False)
    THEATER_NAME = Column(VARCHAR(255), nullable=False)
    THEATER_ADDRESS = Column(VARCHAR(255), nullable=False)
    THEATER_LATITUDE = Column(DECIMAL)
    THEATER_LONGITUDE = Column(DECIMAL)
    halls = relationship("Hall", back_populates="theater")

class Hall(Base):
    __tablename__ = "HALL"
    HALL_ID = Column(SmallInteger, primary_key=True, index=True)
    THEATER_ID = Column(Integer, ForeignKey('THEATER.THEATER_ID'))
    HALL_APL_NUM = Column(TINYINT, nullable=False)
    AB_HALL_SEATS = Column(TINYINT, nullable=False)
    theater = relationship("Theater", back_populates="halls")
    shows = relationship("Show", back_populates="hall")

class Show(Base):
    __tablename__ = "SHOW"
    SHOW_ID = Column(Integer, primary_key=True, index=True)
    HALL_ID = Column(SmallInteger, ForeignKey('HALL.HALL_ID'))
    SHOW_NAME = Column(VARCHAR(255), nullable=False)
    SHOW_START_DATE = Column(DateTime, nullable=False)
    SHOW_END_DATE = Column(DateTime, nullable=False)
    SHOW_CAST = Column(VARCHAR(255), nullable=False)
    SHOW_RUNTIME = Column(VARCHAR(255), nullable=False)
    SHOW_RATING = Column(VARCHAR(255), nullable=False)
    SHOW_POSTER_IMAGE_PATH = Column(VARCHAR(255), nullable=False)
    SHOW_AREA = Column(VARCHAR(255), nullable=False)
    SHOW_STATE = Column(VARCHAR(255), nullable=False)
    SHOW_SCHEDULE = Column(VARCHAR(255), nullable=False)
    hall = relationship("Hall", back_populates="shows")
    show_dates = relationship("ShowDate", back_populates="show")
    show_categories = relationship("ShowCategory", back_populates="show")

class ShowDate(Base):
    __tablename__ = "SHOW_DATE"
    SHOW_DATE_ID = Column(Integer, primary_key=True, index=True)
    SHOW_ID = Column(Integer, ForeignKey('SHOW.SHOW_ID'))
    SHOW_DATE = Column(DateTime, nullable=False)
    SHOW_TIME = Column(DateTime, nullable=False)  # Time 타입으로 변경
    show = relationship("Show", back_populates="show_dates")

class SeatInfo(Base):
    __tablename__ = "SEAT_INFO"
    SEAT_INFO_ID = Column(Integer, primary_key=True, index=True)
    SHOW_ID = Column(Integer, ForeignKey('SHOW.SHOW_ID'))
    FIELD = Column(VARCHAR(255), nullable=False)
    show = relationship("Show", back_populates="seat_infos")

class SeatGradePrice(Base):
    __tablename__ = "SEAT_GRADEPRICE"
    SEAT_PRICE = Column(BigInteger, nullable=False)
    SHOW_ID = Column(Integer, ForeignKey('SHOW.SHOW_ID'), primary_key=True)
    FIELD1 = Column(VARCHAR(255), nullable=False)
    FIELD2 = Column(SmallInteger, nullable=True)
    show = relationship("Show", back_populates="seat_grade_prices")

class SeatRecord(Base):
    __tablename__ = "SEAT_RECORD"
    SEAT_RECORD_ID = Column(Integer, primary_key=True, index=True)
    SHOW_DATE_ID = Column(Integer, ForeignKey('SHOW_DATE.SHOW_DATE_ID'), nullable=False)
    ORDER_SEAT = Column(SmallInteger, nullable=False)
    SOLD = Column(Boolean, nullable=False)
    show_date = relationship("ShowDate", back_populates="seat_records")

Show.seat_infos = relationship("SeatInfo", order_by=SeatInfo.SEAT_INFO_ID, back_populates="show")
Show.seat_grade_prices = relationship("SeatGradePrice", order_by=SeatGradePrice.SHOW_ID, back_populates="show")
ShowDate.seat_records = relationship("SeatRecord", order_by=SeatRecord.SEAT_RECORD_ID, back_populates="show_date")
