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
import com.sopt.lang.Network.MyPage.MyFollowerListData
import com.sopt.lang.Network.MyPage.MyFollowerListResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFollowerActivity : AppCompatActivity(), View.OnClickListener {

    private var follower_list : RecyclerView? = null
    private var follower_datas : ArrayList<ProfileFollowerData>? = null
    private var follower_adapter : ProfileFollowerAdapter? = null

    private var networkService: NetworkService? = null
    private var myFollowerData: List<MyFollowerListData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_follower)

        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)
        follower_datas = ArrayList<ProfileFollowerData>()

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.x)
        toolbar.setNavigationOnClickListener { finish() }

        follower_list = findViewById<View>(R.id.follower_list) as RecyclerView
        follower_list!!.layoutManager = LinearLayoutManager(this)


        val myFollowerResponse = networkService!!.getMyFollowerList(SharedPreferencesService.instance!!.getPrefStringData("token")!!)
        myFollowerResponse.enqueue(object : Callback<MyFollowerListResponse> {
            override fun onResponse(call: Call<MyFollowerListResponse>?, response: Response<MyFollowerListResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
                        GlobalApplication.instance!!.makeToast("성공")
                        myFollowerData = response!!.body().data
                        follower_datas!!.clear()

                        var size: Int = response!!.body().data!!.size

                        for (i in 1..size) {
                            follower_datas!!.add(ProfileFollowerData(response!!.body().data!![i - 1].user_id,
                            response!!.body().data!![i - 1].user_image,
                            response!!.body().data!![i - 1].user_name))

                            follower_adapter = ProfileFollowerAdapter(follower_datas, application)
                            follower_list!!.adapter = follower_adapter
                        }

                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<MyFollowerListResponse>?, t: Throwable?) {
                Log.v("444", t.toString())

                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })


//
//        follower_datas!!.add(ProfileFollowerData("Kang Daniel", R.drawable.profile_default, "1"))
//        follower_datas!!.add(ProfileFollowerData("Lai Kuan Lin", R.drawable.profile_default, "2"))
//        follower_datas!!.add(ProfileFollowerData("Ong Seong Wu", R.drawable.profile_default, "3"))
//        follower_datas!!.add(ProfileFollowerData("Ha sung Woon", R.drawable.profile_default, "4"))
//        follower_datas!!.add(ProfileFollowerData("Yoon Ji Sung", R.drawable.profile_default, "5"))
//        follower_datas!!.add(ProfileFollowerData("Park Woo Jin", R.drawable.profile_default, "6"))
//        follower_datas!!.add(ProfileFollowerData("Lee Dae Whi", R.drawable.profile_default, "7"))
//        follower_datas!!.add(ProfileFollowerData("Kim Jae Hwan", R.drawable.profile_default, "8"))
//        follower_datas!!.add(ProfileFollowerData("Bae Jin Young", R.drawable.profile_default, "9"))
//        follower_datas!!.add(ProfileFollowerData("Hwang Min Hyun", R.drawable.profile_default, "10"))
//        follower_datas!!.add(ProfileFollowerData("Park Ji Hoon", R.drawable.profile_default, "11"))
//        follower_datas!!.add(ProfileFollowerData("Rim Su Jung", R.drawable.profile_default, "12"))
//        follower_datas!!.add(ProfileFollowerData("WannaOne is My Love", R.drawable.profile_default, "13"))


    }

    override fun onClick(v: View?) {
        val idx : Int = follower_list!!.getChildAdapterPosition(v)
        val key : String? = follower_datas!!.get(idx).follower_id

        //일단 각각의 리사이클러뷰 선택시 페이지로 이동하게 해놓았음
        val intent = Intent(this, YourProfileActivity::class.java)
        intent.putExtra("key", key)
        startActivity(intent)
    }
}
