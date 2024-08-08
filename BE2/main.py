from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from starlette.middleware.sessions import SessionMiddleware
from auth.routes import router as auth_router
from common.security import security_settings
from auth.database import Base, engine
from auth.utils import create_super_admin
from cs.routes import router as cs_router
from cs2.router import member, review, comment, ticket
from vm.routes import router as vm_router

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
app.include_router(cs_router, prefix="/cs", tags=["cs"])
app.include_router(member.router, prefix="/cs2", tags=["member"])
app.include_router(review.router, prefix="/cs2", tags=["review"])
app.include_router(comment.router, prefix="/cs2", tags=["comment"])
app.include_router(ticket.router, prefix="/cs2", tags=["ticket"])
app.include_router(vm_router, prefix="/vm", tags=["vm"])

@app.get("/")
async def read_root(request: Request):
    return {"message": "Hello"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=80)
