package com.sopt.lang.MyPage.MyLounge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.MyPage.YourLounge.YourLoungeAdapter
import com.sopt.lang.Network.MyPage.OtherLoungeListData
import com.sopt.lang.Network.MyPage.OtherLoungeListResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_your_lounge.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class YourLoungeActivity : AppCompatActivity(), View.OnClickListener {

    private var networkService: NetworkService? = null
    private var requestManager : RequestManager? = null

//    private var token: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"

    private var your_lounge_datas : List<OtherLoungeListData> ?= null
    private  var adapter : YourLoungeAdapter?=null

    override fun onStart() {
        super.onStart()
        SharedPreferencesService.instance!!.load(this)
        networkService = GlobalApplication.instance!!.networkService

        var otherUserId : String? = null
        otherUserId = getIntent().getStringExtra("key")

        var otherUserName : String? = null
        otherUserName = getIntent().getStringExtra("user_name")
        your_lounge_list!!.layoutManager = LinearLayoutManager(this!!)

        mYourLounge(otherUserId, otherUserName)
    }

    var position : Int ?=null

//    var imgs1 : Array<Int> = arrayOf(R.drawable.pic1, R.drawable.pic2)
//    var imgs2 : Array<Int>? = null
//    var imgs3 : Array<Int> = arrayOf(R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_lounge)

        SharedPreferencesService.instance!!.load(this)
        networkService = GlobalApplication.instance!!.networkService

        var otherUserId : String? = null
        otherUserId = getIntent().getStringExtra("key")

        var otherUserName : String? = null
        otherUserName = getIntent().getStringExtra("user_name")

//        tab_host = findViewById(R.id.lounge_tabhost) as TabHost
//        tab_host!!.setup()

//
//        ts1 = tab_host!!.newTabSpec("tab1")
//        ts1!!.setIndicator("라운지")
//        ts1!!.setContent(R.id.tab1)
//        tab_host!!.addTab(ts1)
//
//        ts2 = tab_host!!.newTabSpec("tab2")
//        ts2!!.setIndicator("팔로잉")
//        ts2!!.setContent(R.id.tab2)
//        tab_host!!.addTab(ts2)
//
//        tab_host!!.setCurrentTabByTag("tab1")
//        which_tab = 1   // 디폴트 tab을 라운지로 지정

        /*if(which_tab == 1){ // 선택된 tab이 라운지일 경우

        }
        else{   // 선택죈 tab이 팔로잉일 경우

        }*/

        your_lounge_list!!.layoutManager = LinearLayoutManager(this!!)

        mYourLounge(otherUserId, otherUserName)
        // loungeDatas = List<OtherLoungeListData>()

        /*  loungeDatas!!.add(MyLoungeData("1", R.drawable.complete,"David Keum", "Korean", "English", "하ㅏㅎ 즐겁다~", "10분전", 13, 3,1))
          loungeDatas!!.add(MyLoungeData("2", R.drawable.complete,"David Keum", "Korean", "Chinese", "얄루~", "11분전", 12, 4,0))
          loungeDatas!!.add(MyLoungeData("3", R.drawable.complete,"David Keum", "British", "Korean", "hi this is david", "12분전", 30, 50,0))
          loungeDatas!!.add(MyLoungeData("4", R.drawable.complete,"David Keum", "Chinese", "English", "Hi im Chinese. i like korea soooo much", "20분전", 20, 5,0))
  */
        /* adapter = YourLoungeAdapter(loungeDatas, this)
         adapter!!.setOnItemClickListener(this)
         loungeList!!.adapter = adapter*/
    }


    fun mYourLounge(otherUserId : String, otherUserName : String){
        val yourLoungeListResponse : Call<OtherLoungeListResponse> = networkService!!.postOtherLoungeList(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, otherUserId)
        yourLoungeListResponse.enqueue(object : Callback<OtherLoungeListResponse>
        {
            override fun onFailure(call: Call<OtherLoungeListResponse>?, t: Throwable?) {
                Toast.makeText(this@YourLoungeActivity, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<OtherLoungeListResponse>?, response: Response<OtherLoungeListResponse>?) {
                if(response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)){
                    your_lounge_title.setText(otherUserName + "의 라운지")
                    your_lounge_subtitle.setText(otherUserName + "의 라운지")

                    your_lounge_datas = response!!.body().data
                    adapter = YourLoungeAdapter(your_lounge_datas!!,this@YourLoungeActivity)
                    your_lounge_list!!.adapter = adapter
                }else{
                    Toast.makeText(this@YourLoungeActivity, "실패", Toast.LENGTH_SHORT).show()

                }
            }

        })
    }

    override fun onClick(v: View?) {


    }
}