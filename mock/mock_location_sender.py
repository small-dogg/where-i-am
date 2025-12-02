import time
import json
import random
import requests
from datetime import datetime

# API endpoint
API_URL = "http://localhost:8888/api/v1/location"

# 수원시(광교/시청 인근) 대략 중심 좌표
CENTER_LAT = 37.2636
CENTER_LON = 127.0286


def generate_and_send_positions(
    user_seq: int = 3,
    step: float = 0.0005,      # 이동 폭 (대략 수십 미터)
    limit_delta: float = 0.05  # 중심에서 벗어날 수 있는 최대 거리
):
    lat = CENTER_LAT
    lon = CENTER_LON

    while True:
        created_at = datetime.now().strftime("%Y-%m-%dT%H:%M:%S")

        payload = {
            "userSeq": user_seq,
            "latitude": round(lat, 6),
            "longitude": round(lon, 6),
            "createdAt": created_at,
        }

        # print to console
        print("SEND:", json.dumps(payload, ensure_ascii=False))

        try:
            # API POST 요청
            res = requests.post(API_URL, json=payload, timeout=3)
            print(f"STATUS: {res.status_code}, RESPONSE: {res.text}")
        except Exception as e:
            print("REQUEST ERROR:", e)

        # 좌표를 랜덤 이동
        lat += random.uniform(-step, step)
        lon += random.uniform(-step, step)

        # 수원 중심 기준 너무 멀어지지 않도록 제한
        lat = max(CENTER_LAT - limit_delta, min(CENTER_LAT + limit_delta, lat))
        lon = max(CENTER_LON - limit_delta, min(CENTER_LON + limit_delta, lon))

        # 1초 대기
        time.sleep(0.5)


if __name__ == "__main__":
    generate_and_send_positions(user_seq=3)

