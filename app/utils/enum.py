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
    
class Region(str, Enum):
    SEOUL = "서울특별시"
    BUSAN = "부산광역시"
    DAEGU = "대구광역시"
    INCHEON = "인천광역시"
    GWANGJU = "광주광역시"
    DAEJEON = "대전광역시"
    ULSAN = "울산광역시"
    SEJON = "세종특별자치시"
    GYEONGGI = "경기도"
    GANGWON = "강원특별자치도"
    CHUNGCHEONGBUKDO = "충청북도"
    CHUNGCHEONGNAMDO = "충청남도"
    JEOLLABUKDO = "전라북도"
    JEOLLANAMDO = "전라남도"
    GYEONGSANGBUKDO = "경상북도"
    GYEONGSANGNAMDO = "경상남도"
    JEJU = "제주특별자치도"
    ETC = "해외 기타 지역"

    @classmethod
    def to_korean(cls, value: str) -> str:
        gangwon_variants = ["강원도", "강원특별자치도"]
        mapping = {
            cls.SEOUL: "서울특별시",
            cls.BUSAN: "부산광역시",
            cls.DAEGU: "대구광역시",
            cls.INCHEON: "인천광역시",
            cls.GWANGJU: "광주광역시",
            cls.DAEJEON: "대전광역시",
            cls.ULSAN: "울산광역시",
            cls.SEJON: "세종특별자치시",
            cls.GYEONGGI: "경기도",
            cls.GANGWON: "강원특별자치도",
            cls.CHUNGCHEONGBUKDO: "충청북도",
            cls.CHUNGCHEONGNAMDO: "충청남도",
            cls.JEOLLABUKDO: "전라북도",
            cls.JEOLLANAMDO: "전라남도",
            cls.GYEONGSANGBUKDO: "경상북도",
            cls.GYEONGSANGNAMDO: "경상남도",
            cls.JEJU: "제주특별자치도",
            cls.ETC: "해외 기타 지역"
        }
        
        if value in gangwon_variants:
            return "강원특별자치도"
        return mapping.get(value, "Unknown")

class ShowState(str, Enum):
    COMPLETED = "COMPLETED"
    SCHEDULED = "SCHEDULED"
    SHOWING = "SHOWING"

class SeatGrade(int, Enum):
    EMPTY = -1
    B = 0
    A = 1
    S = 2
    R = 3

    @classmethod
    def to_label(cls, value: int) -> str:
        label_mapping = {
            cls.EMPTY: "Empty",
            cls.B: "B",
            cls.A: "A",
            cls.S: "S",
            cls.R: "R"
        }
        return label_mapping.get(value, "Unknown")