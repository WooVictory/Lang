package com.sopt.lang.MyPage.MyLounge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.MyPage.MyLoungeListData
import com.sopt.lang.Network.MyPage.MyLoungeListResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyLoungeActivity : AppCompatActivity(), View.OnClickListener {

    var position: Int? = null
    private var loungeList: RecyclerView? = null
    private var loungeDatas: ArrayList<MyLoungeData>? = null
    private var adapter: MyLoungeAdapter? = null

    private var networkService: NetworkService? = null
    private var myLounge_data: List<MyLoungeListData>? = null

//    var imgs1 : Array<Int> = arrayOf(R.drawable.pic1, R.drawable.pic2)
//    var imgs2 : Array<Int>? = null
//    var imgs3 : Array<Int> = arrayOf(R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4)

    override fun onStart() {
        super.onStart()
        Log.v("test547", "in MyLoungeActivity onStart")
        mMyLoungeList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_lounge)

        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)

        loungeList = findViewById<View>(R.id.my_lounge) as RecyclerView
        loungeList!!.layoutManager = LinearLayoutManager(this!!)

        loungeDatas = ArrayList<MyLoungeData>()
    }

    fun mMyLoungeList(){
        val myLoungeResponse = networkService!!.getMyLoungeList(SharedPreferencesService.instance!!.getPrefStringData("token")!!)
        myLoungeResponse.enqueue(object : Callback<MyLoungeListResponse> {
            override fun onResponse(call: Call<MyLoungeListResponse>?, response: Response<MyLoungeListResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
//                        GlobalApplication.instance!!.makeToast("성공")
                        myLounge_data = response!!.body().data

                        if (myLounge_data != null) {
                            var size: Int = response!!.body().data!!.size
                            for (i in 1..size) {
                                loungeDatas!!.add(MyLoungeData(response!!.body().data!![i - 1].lounge_id,
                                        response!!.body().data!![i - 1].user_image,
                                        response!!.body().data!![i - 1].user_name,
                                        response!!.body().data!![i - 1].lounge_time,
                                        response!!.body().data!![i-1].lounge_image,
                                        response!!.body().data!![i - 1].lounge_content,
                                        response!!.body().data!![i - 1].native_lang,
                                        response!!.body().data!![i - 1].hope_lang,
                                        response!!.body().data!![i - 1].like_count,
                                        response!!.body().data!![i - 1].comment_count,
                                        response!!.body().data!![i - 1].isLike))
                            }
                        }
                        adapter = MyLoungeAdapter(loungeDatas, application)
                        adapter!!.setOnItemClickListener(this@MyLoungeActivity)
                        adapter!!.refreshAdapter(loungeDatas!!)
                        loungeList!!.adapter = adapter

                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<MyLoungeListResponse>?, t: Throwable?) {
                Log.v("444", t.toString())

                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })
    }

    override fun onClick(v: View?) {


    }
}