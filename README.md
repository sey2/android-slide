# android-slide
Android 학습 프로젝트 #2

## (Step 3-1 ) 클래스 생성
---

- 태블릿 사이즈 화면에 대응하는 layout을 추가
- Slide interface를 구현하여 하위 클래스들은 필수 속성을 반드시 구현하도록 강제성을 부여
- 정사각형을 표현하는 React 클래스와 정사각형 클래스를 생성하는 ReactFactory 클래스 추가

---

### 결과화면
<img width="971" alt="스크린샷 2023-07-17 오후 3 03 56" src="https://github.com/softeerbootcamp-2nd/android-slide/assets/54762273/c4bbb4e6-0ceb-4ab0-bbe4-20918c42a723">

---
## (Step 3-2 ) 속성 변경 동작하기

- 요구사항에 따른 속성 변경 동작하기 기능
- SlideManager 클래스 테스트 코드 작성
- 화면 구성

### 결과화면
![ezgif com-video-to-gif (1)](https://github.com/softeerbootcamp-2nd/android-slide/assets/54762273/af531e7d-5a36-4d31-99e9-1d833f9242ba)

## (Step 3-3) UDF

UDF 아키텍처 패턴에 맞게 코드를 수정

이전 코드의 문제점

**UI Layer에서 ViewModel의 상태를 아래와 같이 직접 변경하는 코드를 사용**
<img width="570" alt="스크린샷 2023-07-19 오전 11 37 40" src="https://github.com/softeerbootcamp-2nd/android-slide/assets/54762273/78071a9f-89ca-4c3f-a50c-c721e3881448">

**개선후**
<img width="394" alt="스크린샷 2023-07-19 오전 11 41 05" src="https://github.com/softeerbootcamp-2nd/android-slide/assets/54762273/204e3819-91c7-47b7-a9e6-f9fd9269e0fa">

## (Step 3-4) Slide 목록 구현 하기
- 버튼을 누르면 사각형 Slide가 화면에 배치
- 드래그를 이용하여 사각형을 이동 시킬 수 있는 기능
- 리싸이클러뷰를 이용해 목록 표시
- 목록을 터치할 때마다 해당 슬라이드 View의 선택과 해제를 반복하도록 처리하는 기능
- 목록을 드래그해서 순서를 바꿀 수 있는 기능

### 결과화면
![ezgif com-video-to-gif (1)](https://github.com/softeerbootcamp-2nd/android-slide/assets/54762273/8551813b-64b7-41b5-9fed-56cdf5fa9aad)

## (Step 3-5) Slide 목록 구현 하기
- 사진 슬라이드 추가
- 사진 슬라이드내 이미지가 10dp 여백을 가지도록 설정
- 사진 슬라이드는 배경도를 지원하지 않게 하고 투명도만 지원하는 기능
- 불러온 사진의 데이터는 ByteArray로 관리
- 이벤트를 표현하는 Action Class 추가 (Action -> ViewModel -> ViewManager)
---
### 결과화면
![ezgif com-video-to-gif](https://github.com/softeerbootcamp-2nd/android-slide/assets/54762273/959cf1c6-ee9f-43d7-a7bb-b30418a50c6c)


