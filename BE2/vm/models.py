from sqlalchemy import Column, Integer, String, BigInteger, Enum, ForeignKey, DateTime, Double, Text
from sqlalchemy.orm import relationship
from vm.database import Base

class ShowDetail(Base):
    __tablename__ = "show_detail"

    show_detail_id = Column(BigInteger, primary_key=True)
    show_detail_api_id = Column(String(255))
    show_detail_area = Column(String(255))
    show_detail_cast = Column(String(255))
    show_detail_category = Column(Integer)
    show_hall = Column(String(255))
    show_detail_name = Column(String(255))
    show_detail_poster_image_path = Column(String(255))
    show_detail_runtime = Column(String(255))
    show_detail_state = Column(Enum('COMPLETED','SCHEDULED','SHOWING'))
    theater_id = Column(BigInteger, ForeignKey("theater.theater_id"))

    theater = relationship("Theater", back_populates="show_details")
    schedules = relationship("ShowSchedule", back_populates="show_detail")
    seats = relationship("Seat", back_populates="show_detail")
    prices = relationship("PriceBySeatClass", back_populates="show_detail")

class ShowSchedule(Base):
    __tablename__ = "show_schedule"

    show_schedule_id = Column(BigInteger, primary_key=True, autoincrement=True)
    show_schedule_date_time = Column(DateTime(6))
    show_detail_id = Column(BigInteger, ForeignKey("show_detail.show_detail_id"))

    show_detail = relationship("ShowDetail", back_populates="schedules")

class Theater(Base):
    __tablename__ = "theater"

    theater_id = Column(BigInteger, primary_key=True)
    theater_address = Column(String(255))
    theater_api_id = Column(String(255))
    theater_latitude = Column(Double)
    theater_longitude = Column(Double)
    theater_name = Column(String(255))

    show_details = relationship("ShowDetail", back_populates="theater")

class Seat(Base):
    __tablename__ = "seat"

    seat_id = Column(BigInteger, primary_key=True, autoincrement=True)
    seat_col = Column(Integer)
    seat_row = Column(Integer)
    seat_class = Column(String(255))
    show_detail_id = Column(BigInteger, ForeignKey("show_detail.show_detail_id"))

    show_detail = relationship("ShowDetail", back_populates="seats")

class PriceBySeatClass(Base):
    __tablename__ = "price_by_seat_class"

    show_detail_id = Column(BigInteger, ForeignKey("show_detail.show_detail_id"), primary_key=True)
    price_by_seat_class_price = Column(Integer)
    price_by_seat_class_seat_class = Column(String(255), primary_key=True)

    show_detail = relationship("ShowDetail", back_populates="prices")
