# project-board
#### 게시판 이용자의 포인트 관리 서비스 (토이 프로젝트)

## 개발 환경

#### Build : Gradle 8.8
#### Language : Java 17
#### Framework : Spring Boot 3.3.1
#### Dependencies : Spring Web, Spring Security, Spring Data JPA, QueryDSL-JPA, H2 Embedded DB, Lombok, JWT ...



## 실행 방법

#### com.board.api.ProjectBoardApplication 의 main method 실행
#### gradle build 후 프로젝트 하위의 build/lib/project-board.jar 를 실행 (실행 가능 jar)

## 테스트 방법

#### Spring REST Docs 와 Swagger 를 사용하여 작성 자동화된 API 명세서
* /swagger-ui 접속해서 진행
  * /members/signup 가입
  * /members/login 으로 jwt token 발급
  * authorization 항목에 bearer 빼고 token 입력해서 인증
  * 그 외 End Point 로 테스트

## 사양

#### 게시물/댓글 작성시 이벤트가 발생하고 아래 API 로 이벤트를 전달.
* [POST] /members/signup
  * 회원가입 API
* [POST] /members/login
  * 로그인 API 
  * 인증 토큰 반환 (이하의 API 호출시 인증 수단)
* [GET] /members/points
  * 로그인 회원의 현재 포인트 조회 API
  * 현재 포인트 반환
* [POST] /posts
  * 로그인 회원의 게시물 작성 API
  * 게시물 id 반환
* [GET] /posts
  * 게시물 목록 조회 API
  * 페이지 번호, 페이지 별 갯수, 정렬방식 요청
  * 페이징 정보, 게시물 id, 게시물 제목 목록 반환
* [GET] /posts/{postId}
  * 게시물 상세 조회 API
  * 게시물 정보, 댓글 정보 반환
* [PUT] /posts
  * 게시물 수정 API
  * 작성자가 아니면 수정 불가
  * 수정된 게시물 id 반환
* [DELETE] /posts/{postId}
  * 게시물 삭제 API
* [POST] /comments
  * 게시물에 대한 댓글 작성 API
  * 게시물 id 와 작성한 댓글 id 반환
* [DELETE] /comments/{commentId}
  * 로그인 회원의 게시물에 대한 댓글 삭제 API

#### 게시판 이용자들이 게시물을 작성할 때 포인트를 부여하고, 전체/개인에 대한 포인트 부여 히스토리와 개인 별 누적 포인트를 관리.
* 게시물 작성시 작성자에 포인트 3점 부여.
* 댓글 작성시 작성자에 포인트 2점 부여하고 게시물 작성자에 포인트 1점 부여.
* 게시물 수정시 포인트 변화 없음.
* 댓글 삭제시 해당 댓글로 취득된 게시물/댓글 작성자의 포인트 회수. 
* 포인트 증감이 있을 때마다 이력이 저장.
* 사용자마다 현재 시점의 포인트 총점을 조회하거나 계산.
#### 게시물 1개에 0개 이상의 댓글이 작성될 수 있음.
#### 댓글에 대한 댓글은 작성될 수 없음.
#### 댓글은 수정될 수 없음.


## 제약사항

#### 모든 요청과 응답에 대해서 application/json 타입으로 구현.
#### 민감정보 (주민등록번호, 비밀번호 등)는 암호화 된 상태로 저장.
#### 인증 토큰의 유효시간은 발급 후 30분.
#### /member/signup, /member/login 이외의 API는 인증 대상