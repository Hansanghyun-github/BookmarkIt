# 나만의 북마크 관리자 - 📚BookmarkIt
BookmarkIt을 이용해 여러분이 즐겨찾는 웹사이트를 저장하세요!

**BookmarkIt의 백엔드 레포지토리입니다**

---

## ✨프로젝트 설명

사용자가 원하는 웹사이트의 url과 별명을 관리할 수 있습니다.

폴더를 관리할 수 있습니다.

---

## 🛠️개발 언어 및 활용 기술

**개발 환경**

- **Springboot**를 이용해서 웹 애플리케이션 서버를 구축했습니다.
- 빌드도구로 **Gradle**을 사용했습니다
- **Spring Data JPA(Hibernate로 구현)** 와 **QueryDSL** 로 DB의 데이터를 관리했습니다.
- DB는 **MySQL**을 사용했습니다.
- 프론트엔드로 **React** 를 사용했습니다.

**시큐리티**

- **Spring Security** 를 사용했습니다.
- 스프링 시큐리티의 세션방식이 아닌 **JWT** 토큰 방식을 이용해서 인증을 진행했습니다.
- JWT 액세스 토큰을 발급할때 refresh token을 같이 발급하여 보안을 강화했습니다.

**인프라**

- **AWS EC2**를 사용해 서버를 구축했습니다.

---

## ⚙시스템 아키텍처
![시스템아키텍쳐](https://github.com/Hansanghyun-github/BookmarkIt/assets/56988779/210a47c1-f58d-4e99-9772-11688a3408e6)


---

## 🏗️ERD 다이어그램
![ERD](https://github.com/Hansanghyun-github/BookmarkIt/assets/56988779/5e297c0b-b7df-4dcc-8ada-c80b4c27578e)

---

## 🏗️API 명세서
https://s-private-organization.gitbook.io/bookmark-project

---

## 💼프론트엔드 레포지토리
https://github.com/Hansanghyun-github/BookmarkIt-front

