# android-slide
Android 학습 프로젝트 #2

## (Step 1-1 ) 클래스 생성
---

- 태블릿 사이즈 화면에 대응하는 layout을 추가
- Slide interface를 구현하여 하위 클래스들은 필수 속성을 반드시 구현하도록 강제성을 부여
- 정사각형을 표현하는 React 클래스와 정사각형 클래스를 생성하는 ReactFactory 클래스 추가

---

### 결과화면
<img width="971" alt="스크린샷 2023-07-17 오후 3 03 56" src="https://github.com/softeerbootcamp-2nd/android-slide/assets/54762273/c4bbb4e6-0ceb-4ab0-bbe4-20918c42a723">

---
## (Step 1-2 ) 속성 변경 동작하기

- 요구사항에 따른 속성 변경 동작하기 기능
- SlideManager 클래스 테스트 코드 작성
- 화면 구성

### 결과화면
![ezgif com-video-to-gif (1)](https://github.com/softeerbootcamp-2nd/android-slide/assets/54762273/af531e7d-5a36-4d31-99e9-1d833f9242ba)

## (Step 1-3) UDF

UDF 아키텍처 패턴에 맞게 코드를 수정

이전 코드의 문제점

**UI Layer에서 ViewModel의 상태를 아래와 같이 직접 변경하는 코드를 사용**
<img width="570" alt="스크린샷 2023-07-19 오전 11 37 40" src="https://github.com/softeerbootcamp-2nd/android-slide/assets/54762273/78071a9f-89ca-4c3f-a50c-c721e3881448">

**개선후**
<img width="394" alt="스크린샷 2023-07-19 오전 11 41 05" src="https://github.com/softeerbootcamp-2nd/android-slide/assets/54762273/204e3819-91c7-47b7-a9e6-f9fd9269e0fa">


