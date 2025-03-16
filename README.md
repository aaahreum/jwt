#  바로인턴 10기 JAVA 과제

- Spring Boot를 이용하여 JWT 인증/인가 로직과 API 구현
- Junit 기반 테스트 코드 작성
- Swagger를 활용한 API 문서화
- AWS EC2 배포(미진행)


###  Swagger UI & API 명세서

- **Swagger UI**

    http://localhost:8080/swagger-ui/index.html
- **API 명세서(JSON)**

    http://localhost:8080/v3/api-docs 

---

## 1. 사용자 API

### 회원가입 API
####  **[POST] /signup**
- 설명: 사용자를 등록합니다.
- 요청 헤더
  ```http
  Content-Type: application/json
  ```
- 요청 바디
  ```json
  {
    "username": "tester",
    "password": "Abc1234!",
    "nickname": "nickname"
  }
  ```
- 응답 예시(성공)
  ```json
  {
    "username": "tester",
    "nickname": "nickname",
    "roles": [
      {
        "role": "USER"
      }
    ]
  }
  ```
- 에러 응답
  ```json
  {
    "error": {
      "code": "USER_ALREADY_EXISTS",
      "message": "이미 가입된 사용자입니다."
    }
  }
  ```
- 상태 코드
    - 200 OK - 회원가입 성공
    - 400 Bad Request - 중복된 사용자



### 로그인 API
#### **[POST] /login**
- 요청 헤더
  ```http
  Content-Type: application/json
  ```
- 요청 바디
  ```json
  {
    "username": "tester",
    "password": "Abc1234!"
  }
  ```
- 응답 예시(성공)
  ```json
  {
    "token": "eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL"
  }
  ```
- 응답
  ```json
  {
    "error": {
      "code": "INVALID_CREDENTIALS",
      "message": "아이디 또는 비밀번호가 올바르지 않습니다."
    }
  }
  ```
- 상태 코드
    - 200 OK - 로그인 성공 
    - 400 Bad Request - 아이디 또는 비밀번호 오류

---

## 관리자 API

### 관리자 권한 부여 API
####  **[PATCH] /admin/users/{userId}/roles**
- 설명: 권한을 **ADMIN** 으로 변경합니다.
- 요청 헤더
  ```http
  Authorization: Bearer {accessToken}
  Content-Type: application/json
  ```
- 요청 예시
  ```http
  PATCH /admin/users/2/roles
  ```
- 응답 예시 (성공)
  ```json
  {
    "username": "tester2",
    "nickname": "nickname2",
    "roles": [
      {
        "role": "ADMIN"
      }
    ]
  }
  ```
- 에러 응답
  ```json
  {
    "error": {
      "code": "ACCESS_DENIED",
      "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
    }
  }
  ```
  ```json
  {
    "error": {
      "code": "USER_NOT_FOUND",
      "message": "사용자를 찾을 수 없습니다."
    }
  }
  ```
- 상태 코드
    - 200 OK - 권한 변경 성공
    - 400 Bad Request - 사용자 없음
    - 403 Forbidden - 권한 없음
