from fastapi import FastAPI, Depends, HTTPException, status, Request
from starlette.middleware.sessions import SessionMiddleware
from sqlalchemy.orm import Session
from database import engine, Base, get_db

app = FastAPI()

Base.metadata.create_all(bind=engine)
