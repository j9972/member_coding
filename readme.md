## EndPoints

### 홈페이지로 이동

Get /

ex) http://localhost:3030/

### 회원가입 페이지 보여주기

Get /member/save

ex) http://localhost:3030/member/save

### 회원가입  

Post /member/save

ex) http://localhost:3030/member/save

### 로그인 요청이 왔을때 띄워줄 페이지 표시

Get /member/login

ex) http://localhost:3030/member/login

### 로그인 

Post /member/login

ex) http://localhost:3030/member/login

### 회원 목록을 보여주는 페이지

Get /member/

ex) http://localhost:3030/member/

### 개인 회원을 보여주는 페이지 

Get /member/{id}

ex) http://localhost:3030/member/{id}

### 회원 수정 페이지 요청

Get /member/update

ex) http://localhost:3030/member/update

### 회원 수정 

Post /member/update

ex) http://localhost:3030/member/update

### 회원 삭제

Get /member/delete/{id}

ex) http://localhost:3030/member/delete/{id}

### 로그아웃 페이지

Get /member/logout

ex) http://localhost:3030/member/logout

## 추가 기능
- ajax -> axios 를 사용한 이메일 중복 체크

### 아메일 중복 체크

Post /member/email-check

ex) http://localhost:3030/member/email-check