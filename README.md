## 프로젝트 개요
CRUD + 동시성 제어 기반 수강 신청 시스템입니다.
## 기술 스택
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- H2 (테스트)
- JUnit5
- Mockito
## 실행 방법
1. Docker로 MySQL 실행
```bash
docker-compose up -d
```

2. 애플리케이션 실행

**Mac/Linux**
```bash
./gradlew bootRun
```
**Windows**
```bash
gradlew.bat bootRun
```
3. 유저 데이터 입력(유저 가입 기능을 만들지 않아서 도커로 들어가 insert 문을 작성해야합니다.)
```bash
docker exec -it homework.local.db mysql -u root -p1234 homework
```

```sql
-- 크리에이터 생성
INSERT INTO user (role) VALUES ('ROLE_CREATOR');

-- 수강생 생성
INSERT INTO user (role) VALUES ('ROLE_CLASSMATE');
```

## 설계 결정과 이유
## 설계 결정과 이유

### 패키지 구조 - DDD
도메인 중심으로 패키지를 분리하였습니다. Course, Enrollment, User 각 도메인이 독립적인 레이어(domain, application, infrastructure, presentation)를 가지도록 설계하였습니다. 이를 통해 도메인 간 경계를 명확히 하고 응집도를 높였습니다.

### 비즈니스 로직 위치
상태 전이, 정원 체크, 유효성 검증 등 비즈니스 로직은 도메인 객체 내부에 위치시켰습니다. Service(UseCase)는 도메인 객체를 조합하는 역할만 담당합니다.

### 동시성 제어 - 비관적 락
수강 신청 시 정원 관리를 위해 비관적 락을 적용하였습니다. 인기 강의의 경우 동시 요청이 몰릴 수 있어 낙관적 락 사용 시 재시도가 폭발적으로 늘어날 수 있기 때문에 비관적 락을 선택하였습니다.

### UseCase 단위 서비스 분리
하나의 Service 클래스에 모든 메서드를 넣는 대신 UseCase 단위로 클래스를 분리하였습니다. 단일 책임 원칙을 지키고 클래스 간 의존성을 줄이기 위함입니다.

### 인증/인가
별도의 인증 시스템 대신 X-User-Id 헤더로 유저를 식별하는 방식을 선택하였습니다. ArgumentResolver를 활용하여 컨트롤러에서 User 객체를 바로 주입받을 수 있도록 하였습니다.
### 미구현
- 유저 회원가입/로그인 기능 미구현
    - X-User-Id 헤더로 유저 식별하는 방식으로 대체
    - 초기 유저 데이터는 직접 DB에 삽입 필요

### 제약사항
- 취소 동시성 이슈 미처리
    - 동시에 여러 명이 취소할 경우 대기열 승격이 중복될 수 있음
- 컨트롤러 통합 테스트 미작성
    - 도메인, UseCase 단위 테스트로 대체
## AI 활용 범위
- 작성한 코드의 논리적 오류 및 예외 처리 검증
- README 문서 작성

AI가 생성한 코드를 그대로 사용하지 않고, 직접 설계하고 작성한 코드를 AI를 통해 검증하는 방식으로 활용하였습니다.
## API 목록 및 예시
## API 목록 및 예시

### Course

| Method | URL | 설명 | 권한 |
|--------|-----|------|------|
| POST | /courses | 강의 등록 | CREATOR |
| GET | /courses | 강의 목록 조회 | 누구나 |
| GET | /courses/{courseId} | 강의 상세 조회 | 누구나 |
| PATCH | /courses/{courseId}/status | 강의 상태 변경 | CREATOR |
| GET | /courses/{courseId}/enrollments | 수강생 목록 조회 | CREATOR |

### Enrollment

| Method | URL | 설명 | 권한 |
|--------|-----|------|------|
| POST | /enrollments/{courseId} | 수강 신청 | CLASSMATE |
| PATCH | /enrollments/{enrollId}/confirm | 결제 확정 | CLASSMATE |
| PATCH | /enrollments/{enrollId}/cancel | 수강 취소 | CLASSMATE |
| GET | /enrollments | 내 수강 신청 목록 | CLASSMATE |

### 요청/응답 예시

**강의 등록**

Request
```json
POST /courses
X-User-Id: 1
Content-Type: application/json

{
    "title": "스프링 부트 강의",
    "description": "스프링 부트 기초부터 심화까지",
    "price": 50000,
    "capacity": 30,
    "startDate": "2026-06-01",
    "endDate": "2026-12-31"
}
```

Response
```json
{
    "id": 1,
    "title": "스프링 부트 강의",
    "description": "스프링 부트 기초부터 심화까지",
    "userId": 1,
    "price": 50000,
    "capacity": 30,
    "currentApplicantCount": 0,
    "courseStatus": "DRAFT",
    "startDate": "2026-06-01",
    "endDate": "2026-12-31"
}
```

**강의 상태 변경**

Request
```
PATCH /courses/1/status?status=OPEN
X-User-Id: 1
```

**강의 목록 조회**

Request
```
GET /courses
GET /courses?status=OPEN
```

**강의 상세 조회**

Request
```
GET /courses/1
```

Response
```json
{
    "id": 1,
    "title": "스프링 부트 강의",
    "description": "스프링 부트 기초부터 심화까지",
    "userId": 1,
    "price": 50000,
    "capacity": 30,
    "currentApplicantCount": 0,
    "courseStatus": "OPEN",
    "startDate": "2026-06-01",
    "endDate": "2026-12-31"
}
```

**수강 신청**

Request
```
POST /enrollments/1
X-User-Id: 2
```

Response
```json
{
    "id": 1,
    "courseId": 1,
    "courseTitle": "스프링 부트 강의",
    "status": "PENDING",
    "waitlistCount": null
}
```

**결제 확정**

Request
```
PATCH /enrollments/1/confirm
X-User-Id: 2
```

**수강 취소**

Request
```
PATCH /enrollments/1/cancel
X-User-Id: 2
```

**내 수강 신청 목록**

Request
```
GET /enrollments?page=0&size=10
X-User-Id: 2
```

Response
```json
{
    "content": [
        {
            "id": 1,
            "courseId": 1,
            "courseTitle": "스프링 부트 강의",
            "status": "CONFIRMED",
            "waitlistCount": null
        }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0
}
```
## 데이터 모델 설명

### User
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | 식별자 |
| role | ROLE_CREATOR, ROLE_CLASSMATE | 역할 |

### Course
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | 식별자 |
| title | String | 강의 제목 |
| description | String | 강의 설명 |
| user | User | 강의 생성자 |
| price | Integer | 가격 |
| capacity | Integer | 최대 정원 |
| currentApplicantCount | Integer | 현재 신청 인원 |
| courseStatus | DRAFT, OPEN, CLOSED | 강의 상태 |
| startDate | LocalDate | 수강 시작일 |
| endDate | LocalDate | 수강 종료일 |

### Enrollment
| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | 식별자 |
| course | Course | 강의 |
| user | User | 신청자 |
| status | PENDING, CONFIRMED, CANCELLED, WAITED | 신청 상태 |
| confirmedTime | LocalDateTime | 결제 확정 시간 |
| waitlistCount | Integer | 대기열 순서 |

## 테스트 실행 방법

```bash
# Mac/Linux
./gradlew test

# Windows
gradlew.bat test
```

