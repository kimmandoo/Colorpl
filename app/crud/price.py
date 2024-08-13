from sqlalchemy.orm import Session
from app.models import PriceBySeatClass
from app.schemas.price import PriceBySeatClassCreate

def create_price_by_seat_class(db: Session, price_data: PriceBySeatClassCreate):
    # price_data가 단일 항목인지, 리스트인지 확인하고 각각 처리
    if isinstance(price_data.price_by_seat_class_seat_class, list):
        db_prices = []
        for seat_class, price in zip(price_data.price_by_seat_class_seat_class, price_data.price_by_seat_class_price):
            db_price = PriceBySeatClass(
                show_detail_id=price_data.show_detail_id,
                price_by_seat_class_price=price,
                price_by_seat_class_seat_class=seat_class
            )
            db.add(db_price)
            db_prices.append(db_price)
        db.commit()
        return db_prices
    else:
        # 단일 항목인 경우 처리
        db_price = PriceBySeatClass(
            show_detail_id=price_data.show_detail_id,
            price_by_seat_class_price=price_data.price_by_seat_class_price,
            price_by_seat_class_seat_class=price_data.price_by_seat_class_seat_class
        )
        db.add(db_price)
        db.commit()
        db.refresh(db_price)
        return db_price


def get_price_by_seat_class(db: Session, show_detail_id: int):
    return db.query(PriceBySeatClass).filter(PriceBySeatClass.show_detail_id == show_detail_id).all()
