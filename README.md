## 1. Try Eat !

- 성공적인 식당운영을 위한 신규 메뉴 테스트 플랫폼
  
- 시연 영상: https://www.youtube.com/watch?v=m9j3hvhfzhk
  
- 중앙대 캡스톤 디자인 어워즈 창업 부문 장려상 수상 

![Frame 1 10](https://github.com/JMS1208/IntervuStella/assets/90887876/30f33d33-1481-40da-857b-7e4b3d3e4c3e)


## 2. 프로젝트 시기 & 담당

- 2022.07 ~ 2023.01 (약 6개월)

- 안드로이드 & UI/UX 일임 (팀 3명)

## 3. 문제 파악

- 악화되어가는 경제 상황 속 식당 폐업률은 점점 높아져가고 있습니다. 식당 사업을 성공으로 이끄는 수많은 방법이 있지만, 그 중 시장성을 파악하는 것은 필수적이라고 할 수 있습니다.
  
- 사장님은 오픈할 식당 또는 개발한 메뉴가 고객들에게 피력되는 아이템인지 확인하기 위해 테스트를 하고 싶어하고, 손님들은 ‘가오픈족’이라는 신조어가 나올만큼 출시되기 전 미리 경험해보고 싶어합니다.
  
- 정식메뉴로서 시장성을 테스트하기엔 식자재를 대량 구비해두어야 하고 손님들의 기대에 미치지 못할 수 있는 위험과 기회비용이 너무 많이 든다는 문제점이 있었습니다.
  
- 그래서 성공적으로 식당이 운영되기를 바라는 사장님의 심리와 젊은 세대들의 소비심리를 결합한 음식 테스트를 위한 플랫폼을 만들기로 하였습니다.

## 4. 수요

- 인스타그램에서 가오픈 관련 해시태그가 약 60만건 이상이 있었고, 그외에도 네이버 키워드 조회를 통해 신메뉴 개발과 가오픈에 대한 관심도를 확인할 수 있었습니다.

## 5. 주요 요구사항

a. 회원가입 및 로그인
b. 식당 등록 & 메뉴 관리 (등록, 삭제, 품절)
c. 내 위치 주변 식당 정보 리스트
d. 식당 질문지 템플릿 제공 및 커스텀 질문지 생성
e. AI를 활용한 리뷰 요약
f. 리뷰 통계 제공
g. 식당 큐알 코드 생성 및 인증 후 리뷰 작성

## 6. 사용 스킬

- MPAndroidChart / Dagger-Hilt / NaverMap-View / Room-database / Retrofit2 / LottieAnimation / Location-Request

## 7. 프로젝트를 통해 배운 스킬

- Dagger-Hilt를 통해 Retrofit, OkHttp 객체 의존성 주입, GPS를 통해 현재 위치 가져오기, Retrofit을 사용하여 REST API 구현 및 Multipart를 이용한 사진 전송

## 8. 어려웠던점과 해결 방법

- 스크롤 가능한 Fragment 안에 있는 NaverMapView를 터치했을 때, 터치이벤트가 상위 뷰에 의해 가로채지는 문제가 발생하였고, 이를 해결하기 위해서 이벤트를 상위 뷰가 가로채지 않도록 방지함으로써 스크롤 동작의 충돌을 해결하였습니다.

- AI 리뷰 요약에 걸리는 시간이 2분 이상으로 너무 오랜 시간이 걸리는 문제가 생겼습니다. 근본적으로는 AI 리뷰 요약자체를 최적화하는 것이 맞지만, 당시 저는 안드로이드 담당이었기 때문에 UX를 해치지 않기 위해 제가 할 수 있는 방법을 고민해보았고 클라이언트 단에서 Lottie Animation을 통해 로딩 애니메이션을 넣음으로써 UX를 개선할 수 있었습니다. 

- 질문지 생성 화면과 마이페이지에 MotionLayout을 넣음으로써 앱 사용 경험을 개선하기 위해 노력했습니다.

## 9. 주요 화면

![Group 178](https://github.com/JMS1208/IntervuStella/assets/90887876/f051f49b-dfc8-450d-9180-85d2cb049635)

![Group 179](https://github.com/JMS1208/IntervuStella/assets/90887876/ec832884-76a7-42a7-9143-11fd0296623d)

![Group 180](https://github.com/JMS1208/IntervuStella/assets/90887876/157aa679-26ed-4905-a299-db87609f75eb)

![Group 181](https://github.com/JMS1208/IntervuStella/assets/90887876/c96cd9ef-27e2-47bb-8204-a2d47d3cb546)

![Group 182](https://github.com/JMS1208/IntervuStella/assets/90887876/c9cef8b1-409a-494f-9618-e2b42f3f5ab5)

![Group 183](https://github.com/JMS1208/IntervuStella/assets/90887876/1ee5d647-9464-4b41-adab-c1427f7c020d)

![Group 177](https://github.com/JMS1208/IntervuStella/assets/90887876/fc9a07fd-6f35-4e73-8428-28f48136cdd8)
