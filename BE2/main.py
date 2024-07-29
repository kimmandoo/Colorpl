from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from starlette.middleware.sessions import SessionMiddleware
from auth.routes import router as auth_router
from common.security import security_settings
from auth.database import Base, engine
from auth.utils import create_super_admin

app = FastAPI()

app.add_middleware(SessionMiddleware, secret_key=security_settings.SECRET_KEY)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.on_event("startup")
def on_startup():
    Base.metadata.create_all(bind=engine)
    create_super_admin()


app.include_router(auth_router, prefix="/auth", tags=["auth"])

@app.get("/")
async def read_root(request: Request):
    return {"message": "Hello"}

# if __name__ == "__main__":
#     from fastapi.testclient import TestClient
#     client = TestClient(app)
