# FlexshMall ![최종 썸네일](https://github.com/shinyujin97/FlexshMallProject/assets/79908872/3bbd241f-a296-4989-9d4a-c3b9cb9d63be)

## 프로젝트 소개
> 신선한게 Flex 할 수 있는 신선식품 쇼핑몰 사이트
> > 시연 영상 : https://youtu.be/zSt-7Ssa78U




### 💫프로젝트 주요 기능
### 1. 회원가입 및 로그인
  <details> <summary>회원가입 : 이메일 인증을 통한 회원가입</summary>
  </details>

  - 아이디 중복체크 기능 JavaScript사용 alert호출
  - 비밀번호 중복체크 기능 AJAX 사용 비동기 호출으로 화면에서 바로 처리
  - 실제 이메일로 인증번호 발송 후 인증
  - 카카오MAP API 사용으로 주소 입력 받기 가능

### 2. 상품등록  
  <details> <summary>상품등록 : 관리자 권한 확인 후 관리자만 등록 가능</summary>
  </details>

  - 관리자로 로그인 했을 경우에만 상품등록 조회가능
  - 상품 이미지 여러개 등록 가능
  - 상품 수정시 이미지 재등록 가능
  - 관리자는 등록,수정,삭제 가능 / 일반 사용자는 구매만 가능하도록 설정
  - 상품 등록 완료 시 나중에 등록한 상품이 처음으로 오도록 역순 정렬

### 3. 잔액충전,상품구매
   <details> <summary>잔액충전 : 카카오PAY API사용 테스트 충전 진행</summary>
  </details>

  - 물품을 구매하기 위한 잔액 충전 기능
  - 소셜로그인 사용자가 아니더라도 카카오페이 충전 가능

  <details> <summary>상품구매 : 구매자 권한 확인 후 구매 가능</summary>
  </details>

  - 상품 카트에 담기 기능 구현 / 재고수량보다 많이 담을 시 JavaScript사용 재고수량 안내 alert호출
  - 한번에 여러 상품 구매 가능, 현재 선택 구매는 불가능
  - 장바구니에서 배송지 정보 입력시 카카오 MAP API사용 주소 불러오기/ 및 회원가입시 기존 사용자의 주소 Script사용하여 가져오기 가능
  - 구매자 잔액 감소 및 관리자 잔액 증가 구현완료.




### 🖥️구동화면
|`회원가입 및 로그인`|
|-------|
|<img src="https://github.com/shinyujin97/FlexshMallProject/assets/79908872/49487740-08b9-468c-8d6a-68b5917b0d48" width="800" height="600">|

|`상품 등록`|
|-------|
|<img src="https://github.com/shinyujin97/FlexshMallProject/assets/79908872/555beded-bfb2-47ed-8147-788a855be0f1" width="950" height="600">|

|`금액 충전`|
|-------|
|<img src="https://github.com/shinyujin97/FlexshMallProject/assets/79908872/555beded-bfb2-47ed-8147-788a855be0f1" width="950" height="600">|

|`주문 및 배`|
|-------|
|<img src="https://github.com/shinyujin97/FlexshMallProject/assets/79908872/555beded-bfb2-47ed-8147-788a855be0f1" width="950" height="600">|

|`환불 처리`|
|-------|
|<img src="https://github.com/shinyujin97/FlexshMallProject/assets/79908872/555beded-bfb2-47ed-8147-788a855be0f1" width="950" height="600">|

|`환불 처리`|
|-------|
|<img src="https://github.com/shinyujin97/FlexshMallProject/assets/79908872/555beded-bfb2-47ed-8147-788a855be0f1" width="950" height="600">|

|`환불 처리`|
|-------|
|<img src="https://github.com/shinyujin97/FlexshMallProject/assets/79908872/555beded-bfb2-47ed-8147-788a855be0f1" width="950" height="600">|

|`환불 처리`|
|-------|
|<img src="https://github.com/shinyujin97/FlexshMallProject/assets/79908872/555beded-bfb2-47ed-8147-788a855be0f1" width="950" height="600">|

|`환불 처리`|
|-------|
|<img src="https://github.com/shinyujin97/FlexshMallProject/assets/79908872/555beded-bfb2-47ed-8147-788a855be0f1" width="950" height="600">|




### 🖥️ 개발 환경
### FRONT-END
<div>
  <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> 
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> 
  <img src="https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white">
  <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
  <img src="https://img.shields.io/badge/chart.js-F5788D.svg?style=for-the-badge&logo=chart.js&logoColor=white">
</div>

### BACK-END
<div>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white">
  <img src="https://img.shields.io/badge/SpringSecurity-%236DB33F.svg?style=for-the-badge&logo=SpringSecurity&logoColor=white">
  <img src="https://img.shields.io/badge/SpringBoot-%236DB33F.svg?style=for-the-badge&logo=SpringBoot&logoColor=white">
  <img src="https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white">
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white">
  
</div>

### DATABASE
<div>
  <img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
  <img src="https://img.shields.io/badge/oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white">
</div>

### SERVER
<div>
  <img src="https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black">
  <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white">
</div>

### IDE
<div>
  <img src="https://img.shields.io/badge/Eclipse-FE7A16.svg?style=for-the-badge&logo=Eclipse&logoColor=white">
  <img src="https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
</div>

### CLOUD
<div>
  <img src="https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/GitKraken-%238A4182?style=for-the-badge&logo=GitKraken&logoColor=white">
</div>

#### 폴더 컨벤션
```
📦FlexshMall
├── 🗂️.gradle
└── 🗂️files
└── 🗂️gradle
└── 🗂️src
    ├── 🗂️main
    |   ├── 🗂️java
    │   |   ├──🗂️config
    |   |   |  ├──🗂️auth
    │   |   ├──🗂️constant
    │   |   ├──🗂️controller
    │   |   ├──🗂️domain
    │   |   ├──🗂️dto
    │   |   ├──🗂️exception
    │   │   ├──🗂️service
    │   │   ├── BootApplication
    │   ├── 🗂️resources
    │   │   ├──🗂️static
    |   |   |  ├──🗂️assets
    |   |   |  ├──🗂️css
    |   |   |  ├──🗂️files
    |   |   |  ├──🗂️images
    |   |   |  ├──🗂️js
    │   │   ├──🗂️templates
    |   |   |  ├──🗂️board
    |   |   |  ├──🗂️cart
    |   |   |  ├──🗂️fragment
    |   |   |  ├──🗂️none
    |   |   |  ├──🗂️user
    └── application.properties
    └── log4jdbc.log4j2.properties
```
