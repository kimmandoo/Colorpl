from sqlalchemy.orm import Session
from models import ShowDetail, Hall, Seat, ShowSchedule, PriceBySeatClass
from schemas.show_detail import ShowDetailCreate

def create_show_detail(db: Session, show_detail: ShowDetailCreate):
    db_show_detail = ShowDetail(
        show_detail_api_id=show_detail.show_detail_api_id,
        show_detail_area=show_detail.show_detail_area,
        show_detail_cast=show_detail.show_detail_cast,
        show_detail_category=show_detail.show_detail_category,
        show_detail_name=show_detail.show_detail_name,
        show_detail_poster_image_path=show_detail.show_detail_poster_image_path,
        show_detail_runtime=show_detail.show_detail_runtime,
        show_detail_state=show_detail.show_detail_state,
        hall_id=show_detail.hall_id
    )
    db.add(db_show_detail)
    db.commit()
    db.refresh(db_show_detail)
    return db_show_detail

def get_show_detail_by_id(db: Session, show_detail_id: int):
    return db.query(ShowDetail).filter(ShowDetail.show_detail_id == show_detail_id).first()

def get_show_detail_by_api_id(db: Session, show_detail_api_id: str):
    return db.query(ShowDetail).filter(ShowDetail.show_detail_api_id == show_detail_api_id).first()

def get_show_details_by_name(db: Session, show_name: str):
    return db.query(ShowDetail).filter(ShowDetail.show_detail_name.ilike(f"%{show_name}%")).all()