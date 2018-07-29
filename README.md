# Lang - Android

<img src="image/lang_icon.png" width="80">

외국인과 내국인이 함께하는 글로벌 언어교환 & 모임 서비스 **Lang**입니다.

## # 개요

국내 체류 외국인이 증가하고 글로벌 비즈니스 사회에서의 외국어 능력의 중요도가 증가하고 있지만, 정작 우리는 소통을 위한 외국어 공부를 하고 있지 않습니다. 가장 좋은 외국어 학습 방법은 실제로 외국인과 대화를 통해서 자연스럽게 언어를 교환하는 것이지만, 내국인과 외국이느이 상호교류에 대한 정보와 플랫폼이 부족한 현실입니다. 이를 해결하기 위해서 Lang이라는 프로젝트에 참여해서 개발을 하게 되었습니다.

## # 워크플로우

![](/image/Lang_formboard.png)

## # Develop Environment

* Language - **Kotlin**, java
* Minimum SDK Version - 19
* Target SDK Version - 26
* Optimization Device - **Galaxy s6**


## # Library

1. **Layout**
* 'com.sothree.slidinguppanel:library:3.4.0'
* 'com.android.support:cardview-v7:26.+'
* 'com.android.support:recyclerview-v7:26.+'
* 'com.thoughtbot:expandablerecyclerview:1.3'
* 'com.zarinpal:cardviewpager:0.5.3'

2. **HTTP REST API**
* 'com.squareup.retrofit2:retrofit:2.0.2'
* 'com.squareup.retrofit2:converter-gson:2.0.2'

3. **Material design**
* 'com.android.support:design:26.+'

4. **Animation**
* 'com.airbnb.android:lottie:2.1.0'

5. **Image Load**
* 'com.github.bumptech.glide:glide:3.7.0'
* 'de.hdodenhof:circleimageview:2.0.0'

6. **Firebase**
* "com.google.firebase:firebase-auth:11.0.2"
* 'com.google.firebase:firebase-storage:11.0.2'
* 'com.google.firebase:firebase-database:11.0.2'
* 'com.google.firebase:firebase-messaging:11.0.2'

7. **SNS**
* compile group: 'com.kakao.sdk', name: 'usermgmt', version: project.KAKAO_SDK_VERSION
* 'com.facebook.android:facebook-android-sdk:4.28.+'