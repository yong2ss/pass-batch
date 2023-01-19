# 백그라운드 실행 및 무조건 강제 재생성
db-up:
	docker-compose up -d --force-recreate

# volume 까지 삭제
db-down:
	docker-compose down -v