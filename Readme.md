# 백엔드 개발자 사전 과제

### 사용 라이브러리
java8 - zulu  
spring boot 2.7  
h2 database - 메모리  
queryDsl


### 사용프로그램
IDE - 인텔리제이  
REST doc - 포스트맨

### 실행방법 및 설명
서버 실행시 
call/src/main/java/com/bus/call/testDataInit
의 코드로 인해 기본 회원, 기본 게시글이 생성됩니다.
서버가 실행되면 
포스트맨을 사용하셔서 해당 API 들을 테스트하실수 있습니다.
-spring test 실행시 testDataInit 코드를 주석 처리해주세요.

### 기능 부연 설명
외부 사용자는 글을 불러오는 기능만 사용 가능합니다.  
게시판 변경 히스토리 로그는 call/logs/board.log 에서 확인 가능합니다.  
좋아요 추가 히스토리는 call/logs/heart.log 에서 확인 사능합니다.  

### 기능 요구사항(요약)

- 내부, 외부 사용자 구분 하여, 글에 대한 접근 권한 부여.
- 내부 사용자만 글을 쓸수 있음.
- 글 목록에서 계정 타입을 표시
- 글 목록에서 좋아요한 글 표시
- 글 목록에서 좋아요 수 표시

### 기술 요구사항(요약)
- 글작성,수정,삭제
- 글 좋아요
- 글 목록
- HTTP header Authorization의 Value Prefix에 따라 구분 ex)curl -H "Authorization: Realtor 47" 없다면 외부사용자
- 내부 회원 테이블 생성
- 좋아요는 한 계정이 하나의 글에 한번만 가능
- 어떤 사용자가 어떤글에 좋아요했는지 히스토리 확인가능
- 각글에 작성시간 마지막 수정시간 삭제시간에 대한 히스토리 확인가능

### 그외 링크
포스트맨 파일 링크  
https://www.dropbox.com/s/32k6h9qgvsx3jyt/callbus.postman_collection.json?dl=0

