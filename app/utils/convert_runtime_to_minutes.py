import re

def convert_runtime_to_minutes(runtime_str: str) -> int:
    try:
        # 정규 표현식을 사용하여 "2시간 5분" 형식을 분해
        match = re.match(r"(?:(\d+)시간)?\s*(\d+)분?", runtime_str)
        if not match:
            return 0  # 형식이 맞지 않으면 0 반환

        hours = int(match.group(1)) if match.group(1) else 0
        minutes = int(match.group(2)) if match.group(2) else 0

        total_minutes = hours * 60 + minutes
        return total_minutes

    except Exception as e:
        print(f"Error converting runtime: {e}")
        return 0
