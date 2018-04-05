package com.sopt.lang.MyPage.Follow

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.MyPage.YourProfile.YourProfileActivity
import com.sopt.lang.Network.MyPage.MyFollowingListData
import com.sopt.lang.Network.MyPage.MyFollowingListResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFollowingActivity : AppCompatActivity(), View.OnClickListener {

    private var following_list : RecyclerView? = null
    private var following_datas : ArrayList<ProfileFollowingData>? = null
    private var following_adapter : ProfileFollowingAdapter? = null


    private var networkService: NetworkService? = null
    private var myFollowingData: List<MyFollowingListData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_following)


        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)
        myFollowingData = ArrayList<MyFollowingListData>()

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.x)
        toolbar.setNavigationOnClickListener { finish() }

        following_list = findViewById<View>(R.id.your_following_list) as RecyclerView
        following_list!!.layoutManager = LinearLayoutManager(this)

        following_datas = ArrayList<ProfileFollowingData>()


        val myFollowingResponse = networkService!!.getMyFollowingList(SharedPreferencesService.instance!!.getPrefStringData("token")!!)
        myFollowingResponse.enqueue(object : Callback<MyFollowingListResponse> {
            override fun onResponse(call: Call<MyFollowingListResponse>?, response: Response<MyFollowingListResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
                        GlobalApplication.instance!!.makeToast("성공")
                        myFollowingData = response!!.body().data
                     following_datas!!.clear()

                        var size: Int = response!!.body().data!!.size

                        for (i in 1..size) {
                            following_datas!!.add(ProfileFollowingData(response!!.body().data!![i - 1].user_id,
                                    response!!.body().data!![i - 1].user_image,
                                    response!!.body().data!![i - 1].user_name))

                            following_adapter = ProfileFollowingAdapter(following_datas, application)
                            following_list!!.adapter = following_adapter
                        }

                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<MyFollowingListResponse>?, t: Throwable?) {
                Log.v("444", t.toString())

                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })


    }

    override fun onClick(v: View?) {
        val idx : Int = following_list!!.getChildAdapterPosition(v)
        val key : String? = following_datas!!.get(idx).following_id

        //일단 각각의 리사이클러뷰 선택시 페이지로 이동하게 해놓았음
        val intent = Intent(this, YourProfileActivity::class.java)
        intent.putExtra("key", key)
        startActivity(intent)
    }
}
