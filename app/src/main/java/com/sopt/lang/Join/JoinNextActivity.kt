package com.sopt.lang.Join

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.Network.UserManagement.SignUpDataResponse
import com.sopt.lang.Network.UserManagement.SignupPost
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinNextActivity : AppCompatActivity() {
    internal lateinit var my: RelativeLayout
    internal lateinit var wish: RelativeLayout
    internal lateinit var my_text: TextView
    internal lateinit var wish_text: TextView
    internal lateinit var complete: ImageView
    internal var my_value = ""
    internal var wish_value = ""
    internal var my_click = false
    internal var wish_click = false
    private var networkService: NetworkService? = null
    private var token: String? = null    //진짜 토큰
    private var image: MultipartBody.Part? = null
    internal var serverMy = ""
    internal var serverWish = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_next)

        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)


        my = findViewById<View>(R.id.my_language_layout) as RelativeLayout
        wish = findViewById<View>(R.id.wish_language_layout) as RelativeLayout

        my_text = findViewById<View>(R.id.my_language) as TextView
        wish_text = findViewById<View>(R.id.wish_language) as TextView
        complete = findViewById<View>(R.id.join_complete) as ImageView

        my.setOnClickListener {
            my_click = true
            dlg()
        }

        wish.setOnClickListener {
            wish_click = true
            dlg()
            if (wish_value != "" && my_value != "") {
                complete.setImageResource(R.drawable.join_next_on)
            }
        }

        complete.setOnClickListener {
            if (wish_value != "" && my_value != "") {
                var sign: SignupPost = SignupPost(SharedPreferencesService.instance!!.getPrefStringData("id")!!, SharedPreferencesService.instance!!.getPrefStringData("email")!!, SharedPreferencesService.instance!!.getPrefStringData("name")!!,
                        serverMy, serverWish, "devicetoken", SharedPreferencesService.instance!!.getPrefStringData("profileimg")!!)

                val signUpResponse = networkService!!.signUp(sign)
                signUpResponse.enqueue(object : Callback<SignUpDataResponse> {
                    override fun onResponse(call: Call<SignUpDataResponse>?, response: Response<SignUpDataResponse>?) {
                        if (response!!.isSuccessful) {
                            if (response!!.body().status.equals("success")) {
//                                GlobalApplication.instance!!.makeToast("성공")
                                token = response!!.body().data
                                SharedPreferencesService.instance!!.setPrefData("token", token!!)
                                Log.v("토큰큰크큰", token)
                                val join_last = Intent(this@JoinNextActivity, JoinLast::class.java)
                                startActivity(join_last)


                            } else {
                                GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                                Log.d("성공??", "ㄴㄴㄴㄴ")
                            }
                        }
                    }

                    override fun onFailure(call: Call<SignUpDataResponse>?, t: Throwable?) {
                        Log.v("444", t.toString())
                        Log.d("성공??", "성공????????")
                        GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
                    }
                })


            }
        }
    }

    fun dlg() {
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.language_dialog, null)
        val join_ok = dialogView.findViewById<View>(R.id.join_ok) as TextView
        val join_cancle = dialogView.findViewById<View>(R.id.join_cancle) as TextView
        val group = dialogView.findViewById<View>(R.id.language_radio) as RadioGroup

        //멤버의 세부내역 입력 Dialog 생성 및 보이기
        val buider = AlertDialog.Builder(this@JoinNextActivity) //AlertDialog.Builder 객체 생성
        if (my_click)
            buider.setTitle("My Language") //Dialog 제목
        else if (wish_click)
            buider.setTitle("Wish Language") //Dialog 제목
        buider.setView(dialogView) //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)

        //설정한 값으로 AlertDialog 객체 생성
        val dialog = buider.create()
        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false)//없어지지 않도록 설정
        //Dialog 보이기
        dialog.show()

        group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.kor -> if (my_click) {
                    my_value = "한국어"
                    serverMy = "korean"
                } else if (wish_click) {
                    wish_value = "한국어"
                    serverWish = "korean"
                }
                R.id.eng -> if (my_click) {
                    my_value = "English"
                    serverMy = "english"
                } else if (wish_click) {
                    wish_value = "English"
                    serverWish = "english"
                }
                R.id.jap -> if (my_click) {
                    my_value = "漢字"
                    serverMy = "chinese"
                } else if (wish_click) {
                    wish_value = "漢字"
                    serverWish = "chinese"
                }
                R.id.cha -> if (my_click) {
                    my_value = "漢語"
                    serverMy = "japanese"
                } else if (wish_click) {
                    wish_value = "漢語"
                    serverWish = "japanese"
                }
                R.id.esp -> if (my_click) {
                    my_value = "Español"
                    serverMy = "spanish"
                } else if (wish_click) {
                    wish_value = "Español"
                    serverWish = "spanish"
                }

            }
        }
        join_ok.setOnClickListener {
            if (my_click)
                my_text.text = my_value
            else if (wish_click)
                wish_text.text = wish_value

            my_click = false
            wish_click = false
            if (wish_value != "" && my_value != "") {
                complete.setImageResource(R.drawable.join_complete_on)
            }
            dialog.dismiss()
        }
        join_cancle.setOnClickListener {
            my_click = false
            wish_click = false
            if (wish_value != "" && my_value != "") {
                complete.setImageResource(R.drawable.join_complete_on)
            }
            dialog.dismiss()
        }
    }
}