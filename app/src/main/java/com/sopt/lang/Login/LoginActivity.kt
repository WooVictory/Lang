package com.sopt.lang.Login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.kakao.auth.ErrorCode
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger
import com.sopt.lang.Home.HomeActivity
import com.sopt.lang.Intro.Splash
import com.sopt.lang.Join.JoinActivity
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.Network.UserManagement.SignCheckDataResponse
import com.sopt.lang.Network.UserManagement.UserInfoPost
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class LoginActivity : AppCompatActivity() {
    private var callbackManager: CallbackManager? = null
    public var facebook_login_button: LoginButton? = null
    private var kakao_login_button: com.kakao.usermgmt.LoginButton? = null
    private lateinit var callback: SessionCallback
    var login_facebook: ImageView? = null
    var login_kakao: ImageView? = null
    public var face_token_value: String? = null

    private var type: Int = 100 // 전체보기
    var userInfo: UserInfoPost? = null
    var id: String? = null
    var email: String? = null
    var name: String? = null
    var loginData: String? = null

    var img : String ?=null
    private var networkService: NetworkService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(this.applicationContext)
        setContentView(R.layout.activity_login)
        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)
        /**fixme**/
        //    SharedPreferencesService.instance!!.setPrefData("token", token)
        SharedPreferencesService.instance!!.setPrefData("id", "")
        SharedPreferencesService.instance!!.setPrefData("email", "")
        SharedPreferencesService.instance!!.setPrefData("name", "")

        facebook_login_button = findViewById<LoginButton>(R.id.facebook_login_button) as LoginButton
        kakao_login_button = findViewById<LoginButton>(R.id.kakao_login_button) as com.kakao.usermgmt.LoginButton

        login_facebook = findViewById<ImageView>(R.id.login_facebook) as ImageView
        login_kakao = findViewById<ImageView>(R.id.login_kakao) as ImageView


        callback = SessionCallback()   //카카오
        Session.getCurrentSession().addCallback(callback)
        callbackManager = CallbackManager.Factory.create() //페북

        login_kakao!!.setOnClickListener {
            kakao_login_button!!.performClick()
        }
        /**fixme**/
        login_facebook!!.setOnClickListener(View.OnClickListener {
            //  facebook_login_button!!.setReadPermissions(Arrays.asList("public_profile", "email"))
            FacebookSdk.sdkInitialize(getApplicationContext())
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this,
                    Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val graphRequest = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                        face_token_value = loginResult.accessToken.token //토큰보내기
                        Log.v("result", `object`.toString())
                        Log.v("리스폰스", response.toString());

                        if (`object`.get("email").toString() != null)
                            email = `object`.get("email").toString()
                        else
                            email = "no"
                        id = loginResult.accessToken.userId
                        name = `object`.get("name").toString()
                        SharedPreferencesService.instance!!.setPrefData("email", email!!)
                        SharedPreferencesService.instance!!.setPrefData("name", name!!)
                        img ="http://graph.facebook.com/"+id+"/picture?type=large"
                        SharedPreferencesService.instance!!.setPrefData("profileimg", img!!)        //uri 스트링으로

                        sign()

                        Log.v("id", id)
                        Log.v("name", name)
                        Log.v("email", email)
                        //       SharedPreferencesService.instance!!.setPrefData("token",face_token_value!!)
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,gender,birthday,email")
                    graphRequest.parameters = parameters
                    graphRequest.executeAsync()

                    //    startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException) {
                    Log.e("LoginErr", error.toString())
                }
            })
        })

        //


        // startActivity(Intent(this@LoginActivity, JoinActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.v("Yg", "yg1")
            Log.d("카카오카카오", "카카오카카오")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)

    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            requestMe()
            Log.d("kakao", "==========================")
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Log.e("yg", exception.toString())
                Log.v("yg", "fail")
            }
            //   setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }
        // 로그인화면을 다시 불러옴
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected fun requestMe() { //유저의 정보를 받아오는 함수
        Log.v("Yg", "yg2")
        UserManagement.requestMe(object : MeResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                Log.v("Yg", "yg3")
                val message = "failed to get user info. msg=" + errorResult!!
                Logger.d(message)
                val result = ErrorCode.valueOf(errorResult.errorCode)
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    Log.v("Yg", "yg4")
                    finish()
                } else {
                    Log.v("Yg", "yg5")
                    redirectLoginActivity()
                }
            }
            override fun onSessionClosed(errorResult: ErrorResult) {
                Log.v("Yg", "yg6")
                redirectLoginActivity()
            }
            override fun onNotSignedUp() {
                Log.v("Yg", "yg7")
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함
            override fun onSuccess(userProfile: UserProfile) {  //성공 시 userProfile 형태로 반환
                var kakaoID :String = userProfile.id.toString()
                var kakaoimg :String = userProfile.profileImagePath
                var kakaoNickname :String = userProfile.nickname
                /**fixme**/
                SharedPreferencesService.instance!!.setPrefData("id",kakaoID)
                SharedPreferencesService.instance!!.setPrefData("email","")
                SharedPreferencesService.instance!!.setPrefData("name",kakaoNickname)
                Logger.d("UserProfile : " + userProfile);
                Log.d("kakao", "==========================");
                Log.d("kakao", "" + userProfile);
                Log.d("kakao", kakaoID);
                Log.d("kakao", kakaoNickname);
                Log.v("Yg", "yg8")
                Log.d("kakao", "==========================")
                sign()
                //   redirectMainActivity() // 로그인 성공시 MainActivity로
            }
        })
    }

    public fun onClickLogout() {
        UserManagement.requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                redirectLoginActivity()
            }
        })
    }

    private fun redirectMainActivity() {

        val join = Intent(this, JoinActivity::class.java)
        startActivity(join)
    }

    protected fun redirectLoginActivity() {
        val intent = Intent(this, Splash::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        finish()
    }

    fun sign() {
        //  startActivity(Intent(this@LoginActivity, JoinActivity::class.java))
        /**fixme**/
        Log.v("아이디",SharedPreferencesService.instance!!.getPrefStringData("id",""))
        Log.v("이메일",SharedPreferencesService.instance!!.getPrefStringData("email",""))

        userInfo = UserInfoPost(SharedPreferencesService.instance!!.getPrefStringData("id", "")!!
                , SharedPreferencesService.instance!!.getPrefStringData("email","")!!)
        Log.v("123456", "lllllllllllllllllllllllllll")
        val checkResponse = networkService!!.signCheck(userInfo!!)
        checkResponse.enqueue(object : Callback<SignCheckDataResponse> {
            override fun onResponse(call: Call<SignCheckDataResponse>?, response: Response<SignCheckDataResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("non exist id")) {
                        GlobalApplication.instance!!.makeToast("가입해도 된당당당")
                        Log.d("성공??", "성공?")
                        loginData = response!!.body().data
                        Log.v("가입체크",loginData)
                        SharedPreferencesService.instance!!.setPrefData("id", loginData!!)
//                        loginData = response!!.body().data
                        val goProfile = Intent(this@LoginActivity, JoinActivity::class.java)
                        startActivity(goProfile)
                    } else {
                        GlobalApplication.instance!!.makeToast("이미 가입~")
                        Log.v("토토토토큰ㅋ늨느",response.body().data)
                        SharedPreferencesService.instance!!.setPrefData("token", response.body().data)
                        val join_last = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(join_last)
                    }
                }else{
                    Log.d("저기요..","???")
                }
            }

            override fun onFailure(call: Call<SignCheckDataResponse>?, t: Throwable?) {
                Log.v("444", t.toString())
                Log.d("성공??", "성공????????")
                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })
    }


}
