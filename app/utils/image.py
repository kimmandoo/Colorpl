import os
import uuid
from fastapi import UploadFile
from pathlib import Path

# EC2 서버의 절대 경로 설정 (확인된 경로)
UPLOAD_DIRECTORY = "/home/ubuntu/backend/images"

# URL로 변환될 베이스 경로 (이미지를 접근할 수 있는 URL)
BASE_IMAGE_URL = "https://i11d109.p.ssafy.io/images/"

def save_image_file(image: UploadFile) -> str:
    """이미지를 저장하고 파일 이름을 반환합니다."""
    if not os.path.exists(UPLOAD_DIRECTORY):
        os.makedirs(UPLOAD_DIRECTORY)
    
    # 고유한 파일명 생성
    file_extension = Path(image.filename).suffix
    unique_filename = f"{uuid.uuid4()}{file_extension}"
    file_path = os.path.join(UPLOAD_DIRECTORY, unique_filename)
    
    # 이미지 파일 저장
    with open(file_path, "wb") as buffer:
        buffer.write(image.file.read())
    
    # 파일 이름 반환 (DB에 저장)
    return unique_filename

def delete_image_file(image_filename: str):
    """파일명을 사용하여 이미지를 삭제합니다."""
    file_path = os.path.join(UPLOAD_DIRECTORY, image_filename)
    
    if os.path.exists(file_path):
        os.remove(file_path)

def update_image_file(image: UploadFile, existing_image_filename: str) -> str:
    """기존 이미지를 삭제하고 새로운 이미지를 저장합니다."""
    if existing_image_filename:
        delete_image_file(existing_image_filename)
    
    return save_image_file(image)
