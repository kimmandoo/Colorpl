from enum import Enum

class Category(str, Enum):
    PLAY = "PLAY"
    MOVIE = "MOVIE"
    PERFORMANCE = "PERFORMANCE"
    CONCERT = "CONCERT"
    MUSICAL = "MUSICAL"
    EXHIBITION = "EXHIBITION"
    ETC = "ETC"

    @classmethod
    def to_korean(cls, value: str) -> str:
        mapping = {
            cls.PLAY: "연극",
            cls.MOVIE: "영화",
            cls.PERFORMANCE: "공연",
            cls.CONCERT: "콘서트",
            cls.MUSICAL: "뮤지컬",
            cls.EXHIBITION: "전시회",
            cls.ETC: "기타"
        }
        return mapping.get(value, "기타")
