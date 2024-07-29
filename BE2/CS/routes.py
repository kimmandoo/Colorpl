from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import Optional
from . import schemas, crud
from .database import get_db
from .dependencies import chief_or_super_admin, admin_only

router = APIRouter()