from typing import List
from sqlalchemy.orm import Session
from models import ShowDetail, Hall, Seat, ShowSchedule, PriceBySeatClass
from schemas.show_detail import ShowDetailCreate, ShowDetailSearch

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

def get_all_shows(db: Session, skip: int = 0, limit: int = 10):
    return db.query(ShowDetail).offset(skip).limit(limit).all()

def get_show_detail_by_id(db: Session, show_detail_id: int):
    return db.query(ShowDetail).filter(ShowDetail.show_detail_id == show_detail_id).first()

def get_show_detail_by_api_id(db: Session, show_detail_api_id: str):
    return db.query(ShowDetail).filter(ShowDetail.show_detail_api_id == show_detail_api_id).first()

def search_show_details(db: Session, search_data: ShowDetailSearch) -> List[ShowDetail]:
    query = db.query(ShowDetail)
    
    # 필터 조건 리스트
    filters = []
    
    if search_data.show_detail_api_id:
        filters.append(ShowDetail.show_detail_api_id.ilike(f"%{search_data.show_detail_api_id}%"))
    
    if search_data.show_detail_name:
        filters.append(ShowDetail.show_detail_name.ilike(f"%{search_data.show_detail_name}%"))
    
    if search_data.show_detail_area:
        filters.append(ShowDetail.show_detail_area == search_data.show_detail_area)
    
    if search_data.show_detail_state:
        filters.append(ShowDetail.show_detail_state == search_data.show_detail_state)
    
    if search_data.show_detail_category:
        filters.append(ShowDetail.show_detail_category == search_data.show_detail_category)
    
    # 필터 적용
    if filters:
        query = query.filter(*filters)
    
    # 페이지네이션 처리
    query = query.offset(search_data.skip).limit(search_data.limit)
    
    # 결과 반환
    results = query.all()

    # 로깅 (선택 사항)
    print(f"Query executed: {str(query)}")  # 실제 사용 환경에서는 logging 모듈을 사용하는 것이 좋습니다.
    
    return results