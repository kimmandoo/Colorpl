from pydantic import BaseModel

class AdministratorCreate(BaseModel):
    username: str
    email: str
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str

class UpdateProfileImage(BaseModel):
    image_url: str
