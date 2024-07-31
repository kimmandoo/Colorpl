from sqlalchemy.orm import Session
from datetime import datetime, timedelta
from cs import models
from cs.database import SessionLocal, engine

# 데이터베이스 연결
models.Base.metadata.create_all(bind=engine)

def create_mock_data(db: Session):
    # 목업 데이터 생성
    member1 = models.Member(email="user1@example.com", nickname="user1", is_deleted=False)
    member2 = models.Member(email="user2@example.com", nickname="user2", is_deleted=False)

    category1 = models.Category(category_name="Category1")
    category2 = models.Category(category_name="Category2")

    db.add(member1)
    db.add(member2)
    db.add(category1)
    db.add(category2)

    db.commit()

    # Schedule 데이터를 먼저 삽입
    schedule1 = models.Schedule(member_id=member1.member_id)
    schedule2 = models.Schedule(member_id=member2.member_id)

    db.add(schedule1)
    db.add(schedule2)

    db.commit()

    # Review 데이터를 삽입
    review1 = models.Review(schedule_id=schedule1.schedule_id, member_id=member1.member_id, content="Review 1 content", is_spoiler=False)
    review2 = models.Review(schedule_id=schedule2.schedule_id, member_id=member2.member_id, content="Review 2 content", is_spoiler=True)

    db.add(review1)
    db.add(review2)

    db.commit()

    # Comment 데이터를 삽입
    comment1 = models.Comment(review_id=review1.review_id, member_id=member1.member_id, content="Comment 1 content")
    comment2 = models.Comment(review_id=review2.review_id, member_id=member2.member_id, content="Comment 2 content")

    db.add(comment1)
    db.add(comment2)

    db.commit()

    # UserCategory 데이터를 삽입
    user_category1 = models.UserCategory(member_id=member1.member_id, category_id=category1.category_id)
    user_category2 = models.UserCategory(member_id=member2.member_id, category_id=category2.category_id)

    db.add(user_category1)
    db.add(user_category2)

    db.commit()

    # ManagementLog 데이터를 삽입
    ban_duration = 7  # 차단 기간을 7일로 설정
    ban_until = datetime.utcnow() + timedelta(days=ban_duration)

    log1 = models.ManagementLog(
        management_category=1,
        member_id=member1.member_id,
        management_action=1,
        management_by="admin@example.com",
        management_reason="Violation of terms",
        ban_until=ban_until
    )
    
    log2 = models.ManagementLog(
        management_category=1,
        member_id=member2.member_id,
        management_action=1,
        management_by="admin@example.com",
        management_reason="Violation of terms",
        ban_until=ban_until
    )

    db.add(log1)
    db.add(log2)

    db.commit()

# 세션 생성
db = SessionLocal()
try:
    create_mock_data(db)
finally:
    db.close()
