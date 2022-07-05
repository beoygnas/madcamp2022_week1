# Week 1 : 덤벼라 인스타그램!


> 4분반 김상엽, 이상민 팀

몰입캠프 1주차에는 공통과제로 탭 구조를 활용한 안드로이드 앱 제작을 하였다.
Android Studio와 Git을 활용한 협업에 익숙해지는 것이 궁극적인 목표이다.
* 탭 1: 나의 연락처 구축. 휴대폰의 연락처 데이터를 활용하거나, JSON 형식을 이용해서 임의의 연락처 데이터를 구축. (추천: ListView나 RecyclerView 등을 이용해서 데이터 보여 주기)
* 탭 2: 나만의 이미지 갤러리 구축. 이미지는 대략 20개 정도.
* 탭 3: 자유 주제 -> 캘린더를 활용한 일정관리 프로그램(일종의 Todolist + Calendar)


<br/>

## 팀원

* 고려대학교 컴퓨터학과 [김상엽](https://github.com/beoygnas)
* KAIST 전산학부 [이상민](https://github.com/alex6095)

## 개발 환경 (Development Environment)

* OS : Android
* SDK 
```
minSdk 26
targetSdk 32
```
* Language : Kotlin & Java
* IDE : Android Studio
* Target Device : Galaxy S7




<br/>

## 어플리케이션 소개 (Application Introduction)
***

### Tab 1

연락처 기능을 구현한 탭으로 임의의 연락처 보다는 실제 휴대폰의 연락처 데이터를 활용하는 것이 현실감이 높아 보일 것 같아서 실제 데이터를 불러와서 보여주는 형식으로 구현하였다. 또한 RecyclerView가 작동방식부터 ListView보다 효율적이고, 구글에서 권장하게 되면서 더 많은 기능들이 추가됬기 때문에 RecyclerView를 적극 활용하였다.


한글 -> 영어 -> 숫자 & 특수문자 순서대로 정렬하여 화면에 보여주며 검색 기능과 스크롤 시 버블로 초성을 보여줄 수 있도록 하였다. 연락처 리스트를 Json파일에 따로 저장할 수 있게 하여 우리 연락처만의 프로필 사진을 저장 할 수 있도록 구현하였다. 프로필 사진으로 선택한 사진의 Uri를 Json에 이름, 전화번호와 함께 저장하는 방식이다. 실제 연락처와 동기화할 때(연락처 삭제 및 추가) 비효율적이고 문제가 발생할 여지가 크지만 훗날 차이점을 LCS 알고리즘 같은것으로 비교 검증한다면 빠르게 동기화 할 수 있을 것이다.

아쉬운 점은 우리의 앱에서 연락처를 추가 및 삭제를 하는 Floating Action Button을 만들 계획이었으나 시간이 부족하여 구현하지 못하였다.

![](https://i.imgur.com/yVtzynq.jpg)
![](https://i.imgur.com/60Ky8DD.jpg)
![](https://i.imgur.com/eDVwBAg.jpg)


### Tab 2

휴대폰 갤러리에 저장된 사진과 연동하여 사진을 보여주는 갤러리이다. RecyclerView를 이용하여 그리드 형식으로 리스트를 보여주고, 사진 선택 시 dialogue로 확대된 사진을 볼 수 있다. 화면의 크기에 맞춰 보기 편한 그리드 숫자로 자동으로 변경되도록 구현하였고, 태블릿이나 가로로 긴 핸드폰을 가로로 두는 경우에 보기 편해졌다. 스크롤 시 버블에 촬영된 날짜가 나온다.
사진의 경우 연락처와 달리 용량이 크기 때문에 불러오는데 시간이 걸릴 수 있어서 preload를 통한 fast scroll을 구현하려고 하였으나 시간이 부족하여 아쉽게도 못하였다.

![](https://i.imgur.com/Ji25a0g.jpg)
![](https://i.imgur.com/MnTl0kR.png)
![](https://i.imgur.com/RTw2ihO.jpg)





### Tab 3

날짜를 확인 할 수 있는 기본적인 캘린더에 그치지 않고, 날짜 선택 시 그 날의 일정을 추가/삭제 할 수 있는 실용적이고 심플한 캘린더를 구현하고자 하였다. 휴대폰의 내부저장소에 json파일을 읽고/쓰는 방식으로 구현했기 때문에, 어플을 삭제하지 않는 이상 데이터를 보존할 수 있다.

기본적인 캘린더는 캘린더뷰를 통해, 일정의 추가/삭제는 각 버튼 클릭 시에 다이얼로그/recyclerview의 새로고침을 통해 구현했다. 
![](https://i.imgur.com/wk2GxNw.jpg)
![](https://i.imgur.com/d03JxGr.jpg)



***

## 어려웠던 점

* Kotlin의 경우 null, type등에 의한 에러에 매우 민감하기 때문에 기본적으로 구현할 때 생각이 좀 더 필요했고, 한국어로 검색할 때 Java보다 활용할 수 있을만한 자료가 부족하여서 영어로 최대한 검색했다. 또한 deprecated 함수를 최대한 활용하지 않으려고 노력하여서 더 힘들었던 것도 있다.
 
* 또한 Kotlin 문법 자체와 안드로이드 액티비티, 프래그먼트의 생명주기 및 관계에 대해 지식이 부족하여 Context, this 같은 것들에 있어서 올바른 구현을 위해 많은 고뇌를 할 수 밖에 없었다.
 
* Permission 허가를 Permission를 요구하기 전에 받아와야하는데 액티비티 위에서 여러 프래그먼트들을 돌리다보니 적절한 타이밍이 없어서 고민한 결과, 인트로 액티비티를 만들고 인트로 화면에서 권한을 받아온 후에 메인 액티비티에 이동을 하는 방식으로 구현을 하였다. 받지 못할 경우 다시 권한을 물어보도록 설계를 하였다. 2번 이상 거절 당할 경우 자동으로 다시 묻지 않는다고 하여서 권한 설정 페이지로 직접 이동하도록 구현하였다. 결과적으로 복잡한 화면이 뜨지 않고 인트로 화면에서 권한 설정을 모두 받을 때까지 반복되며 다 받은 후에 인트로 화면이 0.7초 유지된 후에 메인 액티비티로 넘어간다.
 
* 액티비티로 페이지를 구현했을 경우 더 쉬웠겠지만 자원을 아끼기 위해 액티비티 위에 3개의 Fragment를 띄우는 방식으로 구현을 하도록 고집하였다.
 
* Dialog를 제외한 Activity, Fragment에서 최대한 binding을 이용해서 컴포넌트에 접근을 하려했고 findViewById를 지양함으로써 최적화를 하려고 노력하였다. View Refresh할 때도 최적화를 위해 Activity나 Fragment가 아닌 RecyclerView같은 View만 업데이트 하는 방식으로 낭비를 줄였다. Dialog의 경우 깉은 Layout을 가지기 힘들 것 같아 부분부분 구현에서 finViewById를 활용하도록 내버려 두었다.
 
* 초성을 스크롤바의 버블에 출력하기 위해 유니코드를 활용해 첫글자의 초성을 분해하여 출력하였다. 또한 한글 -> 영어 -> 숫자 & 특수문자 순으로 정렬할 수 있는 Comparable를 만들어서 연락처를 정렬하였다.
 
* 자원을(연락처, 사진 등) Json 파일로 저장한 것은 사실 일종의 캐시와 같은 효과를 받아서 빠르게 불러오려면 계획이었으나 DB를 중점으로 하는 과제가 아니었기에 그러한 최적화는 하지 못하였다.
