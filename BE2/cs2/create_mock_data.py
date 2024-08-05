import sys
import os
from sqlalchemy.orm import Session
from sqlalchemy.exc import IntegrityError

# 현재 디렉토리를 파이썬 모듈 검색 경로에 추가
current_dir = os.path.dirname(os.path.abspath(__file__))
sys.path.append(os.path.dirname(current_dir))

from cs2.database import SessionLocal, engine
from cs2 import models
import random
import datetime

# 데이터베이스 초기화
models.Base.metadata.create_all(bind=engine)

def create_mock_data():
    db = SessionLocal()
    try:
        # 카테고리 생성
        categories = ["Music Lover", "Book Worm", "Movie Buff"]
        category_objects = [models.Category(name=category) for category in categories]
        db.add_all(category_objects)
        db.commit()

        # 사용자 생성
        for i in range(1, 31):
            while True:
                try:
                    member = models.Member(
                        email=f"user{i}@example.com",
                        nickname=f"user{i}",
                        profile=f"profile{i}.jpg",
                        categories=[random.choice(category_objects)]
                    )
                    db.add(member)
                    db.commit()
                    break  # 성공적으로 추가되면 루프 종료
                except IntegrityError:
                    db.rollback()
                    # 중복된 경우 다른 고유한 값을 생성
                    i += 1

            # 티켓 생성 및 리뷰, 댓글 생성
            for j in range(5):
                ticket = models.Ticket(
                    filepath=f"ticket_{i}_{j}.jpg",
                    ticket_name=f"Event {i}-{j}",
                    theater=f"Theater {i}",
                    seat=f"Seat {j}",
                    category=random.choice(categories),
                    member_id=member.member_id
                )
                db.add(ticket)
                db.commit()

                review = models.Review(
                    ticket_id=ticket.ticket_id,
                    content=f"This is review {j} by user {i}.",
                    spoiler=bool(random.getrandbits(1)),
                    emotion=random.choice(["happy", "sad", "excited"]),
                    member_id=member.member_id
                )
                db.add(review)
                db.commit()

                for k in range(5):
                    comment = models.Comment(
                        review_id=review.review_id,
                        member_id=member.member_id,
                        commentContent=f"This is comment {k} on review {j} by user {i}."
                    )
                    db.add(comment)
                db.commit()

    finally:
        db.close()

if __name__ == "__main__":
    create_mock_data()
