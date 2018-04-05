package com.sopt.lang.Login.kakao

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast

import com.kakao.auth.KakaoSDK
import com.sopt.lang.Network.NetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by johee on 2017-12-31.
 */

class GlobalApplication : Application() {

    /*FIXME
    * NetworkService 인터페이스로부터 객체 하나를 생성
    * 이 Url이 내가 통신하려는 서버 Url이다.
    * 그리고 이 뒤에 어떤 것이 붙느냐에 따라서 분기가 달라진다.
    * 현재의 applicationContext를 넣어준다.
    * baseUrl에 할당된 주소로 연결하고
      GsonConverter를 통해서 Json 형식의 데이터를 사용할 수 있게끔 GsonConverterFactory를 추가해서 사용한다.
      그리고 build()로 마무리
      NetworkService를 기반으로 하는 retrofit을 만든다.
      그리고 그것이 baseUrl 주소를 기반으로 네트워크 통신을 진행한다.
    * */




    var networkService: NetworkService? = null
    val baseUrl = "https://lang-service.cf"
    var appContext: Context? = null
    override fun onCreate() {
        super.onCreate()
        globalApplicationContext = this
        KakaoSDK.init(KaKaoSDKAdapter())
        appContext = applicationContext
        instance=this
        buildNetwork()
    }

    fun buildNetwork() {
        val builder = Retrofit.Builder()
        val retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        networkService = retrofit.create(NetworkService::class.java)
    }

    fun makeToast(message: String) {
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        @Volatile
        var globalApplicationContext: GlobalApplication? = null
            private set
        // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
        @Volatile
        var currentActivity: Activity? = null
        var instance: GlobalApplication? = null
    }


}
