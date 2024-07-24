from dotenv import load_dotenv
from urllib.parse import quote_plus
import os

load_dotenv()

class Settings:
    DATABASE_USER: str = os.getenv('DATABASE_USER')
    DATABASE_PASSWORD: str = quote_plus(os.getenv('DATABASE_PASSWORD'))
    DATABASE_HOST: str = os.getenv('DATABASE_HOST')
    DATABASE_NAME: str = os.getenv('DATABASE_NAME')
    SUPERADMIN_USERNAME: str = os.getenv('SUPERADMIN_USERNAME')
    SUPERADMIN_EMAIL: str = os.getenv('SUPERADMIN_EMAIL')
    SUPERADMIN_PASSWORD: str = os.getenv('SUPERADMIN_PASSWORD')

settings = Settings()
