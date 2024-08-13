from sqlalchemy.orm import Session
from app.models import ShowSchedule, ShowDetail
from app.schemas.show_schedule import ShowScheduleCreate

def create_show_schedule(db: Session, show_schedule: ShowScheduleCreate):
    db_show_schedule = ShowSchedule(
        show_schedule_date_time=show_schedule.show_schedule_date_time,
        show_detail_id=show_schedule.show_detail_id
    )
    db.add(db_show_schedule)
    db.commit()
    db.refresh(db_show_schedule)
    return db_show_schedule

def get_schedules_by_hall(db: Session, hall_id: int):
    return db.query(ShowSchedule).join(ShowDetail).filter(ShowDetail.hall_id == hall_id).all()
