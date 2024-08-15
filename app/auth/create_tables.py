from auth.database import engine, Base
from auth.models import Administrator, AdministratorStatus, AdminProfile

Base.metadata.create_all(bind=engine)
