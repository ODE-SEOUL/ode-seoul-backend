from dotenv import load_dotenv
import requests
import pymysql
import datetime
import os
import hashlib
import uuid

def strip_to_empty(text):
  return str(text or "").strip()

def handler(_event, _context):
  load_dotenv()
  db = pymysql.connect(host=os.environ["ODE_SEOUL_DB_HOSTNAME"], port=int(os.environ["ODE_SEOUL_DB_PORT"]),
                       user=os.environ["ODE_SEOUL_DB_USERNAME"], passwd=os.environ["ODE_SEOUL_DB_PASSWORD"],
                       db=os.environ["ODE_SEOUL_DB_SCHEMA"], charset="utf8mb4", ssl={"rejectUnauthorized": True})

  page_start = 1
  paging_size = 1000
  events = []

  while True:
    get_event_info_url = "http://openapi.seoul.go.kr:8088/{}/json/culturalEventInfo/{}/{}/".format(
      os.environ["ODE_SEOUL_DATA_SEOUL_OPEN_API_KEY"], page_start, page_start + paging_size - 1)
    print(get_event_info_url)

    event_info_response = requests.get(get_event_info_url).json()
    print(event_info_response)

    try:
      events += event_info_response["culturalEventInfo"]["row"]
    except:
      if event_info_response["RESULT"]["CODE"] == "INFO-200":
        # 해당하는 데이터가 없습니다.
        break

      raise RuntimeError(event_info_response)

    page_start += paging_size

  now = datetime.datetime.now()

  print("START TRANSACTION")
  db.begin()

  with db.cursor() as cursor:
    sql = "DELETE FROM event"
    print(sql)

    cursor.execute(sql)

  with db.cursor() as cursor:
    sql = """
    INSERT INTO event
      (uuid, codename, guname, title, place, use_target, use_fee, org_link, main_image, start_date, end_date, register_date, created_at)
    VALUES
      (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)""".strip()
    print(sql)

    def build_event_row(event):
      keys = ["CODENAME", "GUNAME", "TITLE", "PLACE", "USE_TRGT", "USE_FEE", "ORG_LINK", "MAIN_IMG", "STRTDATE", "END_DATE", "RGSTDATE"]
      row = list(map(strip_to_empty, map(lambda x: event.get(x, None), keys)))
      uid = str(uuid.UUID(hashlib.sha256(str(row).encode("utf-8")).hexdigest()[:32]))
      return tuple([uid] + row + [now.isoformat()])

    cursor.executemany(sql, tuple(map(build_event_row, events)))

  print("COMMIT")
  db.commit()
  db.close()

if __name__ == "__main__":
  handler(None, None)
