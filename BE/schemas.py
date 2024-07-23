from pydantic import BaseModel, EmailStr, validator

class AdministratorCreate(BaseModel):
    administrator_name: str
    email: EmailStr
    password1: str
    password2: str

    @validator('password2')
    def passwords_match(cls, v, values, **kwargs):
        if 'password1' in values and v != values['password1']:
            raise ValueError('Passwords do not match')
        return v

class AdministratorResponse(BaseModel):
    id: int
    administrator_name: str
    email: EmailStr

    class Config:
        orm_mode = True

class AdministratorUpdate(BaseModel):
    is_approved: bool
    administrator_grade: int

class Token(BaseModel):
    access_token: str
    token_type: str
