from app.auth.database import engine, Base
from app.auth.models import Administrator, AdministratorStatus, AdminProfile

Base.metadata.create_all(bind=engine)
