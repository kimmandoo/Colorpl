import os
from fastapi import FastAPI, Request
from fastapi.responses import FileResponse
from fastapi.staticfiles import StaticFiles
from fastapi.middleware.cors import CORSMiddleware
from starlette.middleware.sessions import SessionMiddleware
from auth.utils import create_super_admin
from utils.config import security_settings
from database import Base, engine
from auth.routes import router as router_auth
from routers.member import router as router_member
from routers.schedule import router as router_schedule
from routers.review import router as router_review
from routers.comment import router as router_comment
from routers.reservation import router as router_reservation
# from utils.image import router as router_image
from routers.theater_hall import router as router_theater_hall
from routers.show_detail import router as router_show_detail
from routers.price import router as router_price
from routers.show_schedule import router as router_show_schedule
from routers.seat import router as router_seat

app = FastAPI()

app.add_middleware(SessionMiddleware, secret_key=security_settings.SECRET_KEY)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(router_auth, prefix='/auth', tags=["auth"])
app.include_router(router_member, prefix='/cs', tags=["member"])
app.include_router(router_schedule, prefix='/cs', tags=["schedule"])
app.include_router(router_review, prefix='/cs', tags=["review"])
app.include_router(router_comment, prefix='/cs', tags=["comment"])
app.include_router(router_reservation, prefix='/vm', tags=["reservation"])
# app.include_router(router_image, prefix='/utils', tags=["utils"])
app.include_router(router_theater_hall, prefix='/vm', tags=["theater_hall"])
app.include_router(router_show_detail, prefix='/vm', tags=["show_detail"])
app.include_router(router_price, prefix='/vm', tags=["price"])
app.include_router(router_show_schedule, prefix='/vm', tags=["show_schedule"])
app.include_router(router_seat, prefix='/vm', tags=["seat"])

@app.on_event("startup")
def on_startup():
    Base.metadata.create_all(bind=engine)
    create_super_admin()

@app.get("/")
async def read_root(request: Request):
    return {"message": "Hello"}

# static_directory = r"C:\Users\SSAFY\Desktop\mobile\S11P12D109\FE\demo\build"

# app.mount("/static", StaticFiles(directory=os.path.join(static_directory, "static")), name="static")


# @app.get("/{path_name:path}")
# async def serve_react_app():
#     return FileResponse(os.path.join(static_directory, "index.html"))

# if __name__ == "__main__":
#     import uvicorn
#     uvicorn.run(app, host="0.0.0.0", port=8000)
