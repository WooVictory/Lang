package com.sopt.lang.MyPage.YourProfile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.MyPage.Follow.YourFollow.YourProfileFollowerActivity
import com.sopt.lang.MyPage.Follow.YourFollow.YourProfileFollowingActivity
import com.sopt.lang.MyPage.MyLounge.YourLoungeActivity
import com.sopt.lang.MyPage.MyMeeting.YourMeetingActivity
import com.sopt.lang.Network.MyPage.OtherMyPageData
import com.sopt.lang.Network.MyPage.OtherMyPageResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.Network.followResponse
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_your_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.sopt.lang.Chatting.Room.ChattingRoomData
import java.util.ArrayList


class YourProfileActivity : AppCompatActivity(), View.OnClickListener {

    private var networkService: NetworkService? = null
    private var requestManager: RequestManager? = null
    var user_name : String? = null
    var otherUserId : String? = null


    //lateinit var otherUserId : String

//    private var token: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_profile)

        SharedPreferencesService.instance!!.load(this)
        networkService = GlobalApplication.instance!!.networkService
        otherUserId = getIntent().getStringExtra("key")

        mOtherMyPage(otherUserId!!)

        profile_follower!!.setOnClickListener(this)
        profile_following!!.setOnClickListener(this)
        cv_block!!.setOnClickListener(this)
        cv_repport!!.setOnClickListener(this)


        profile_follow_btn!!.setOnClickListener{
            //팔로우 신청 취소
            follow(otherUserId!!)
        }
        profile_msg_send_btn!!.setOnClickListener(this)

        your_profile_to_lounge!!.setOnClickListener{
            toYourLounge(otherUserId!!)
        }

        your_profile_to_meeting!!.setOnClickListener{
            toYourMeeting(otherUserId!!)
        }

        profile_follower_box!!.setOnClickListener{
            toYourFollower(otherUserId!!)
        }

        profile_following_box!!.setOnClickListener{
            toYourFollowing(otherUserId!!)
        }
    }

    fun toYourFollowing(your_id: String) {
        val intent = Intent(this, YourProfileFollowingActivity::class.java)
        intent.putExtra("key", your_id)
        startActivity(intent)
    }

    fun toYourFollower(your_id: String) {
        val intent = Intent(this, YourProfileFollowerActivity::class.java)
        intent.putExtra("key", your_id)
        startActivity(intent)

    }

    fun toYourMeeting(your_id : String){
        val intent = Intent(this, YourMeetingActivity::class.java)
        intent.putExtra("key", your_id)
        intent.putExtra("user_name", user_name!!)
        startActivity(intent)
    }

    fun toYourLounge(your_id : String){
        val intent = Intent(this, YourLoungeActivity::class.java)
        intent.putExtra("key", your_id)

        intent.putExtra("user_name", user_name!!)
        startActivity(intent)
    }

    fun follow(id : String){
        val followResponse : Call<followResponse> = networkService!!.putFollow(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, id)
        followResponse.enqueue(object : Callback<followResponse>
        {
            override fun onFailure(call: Call<followResponse>?, t: Throwable?) {
                Log.d("실패하지마라..",t.toString());
                Toast.makeText(this@YourProfileActivity, "실패", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<followResponse>?, response: Response<followResponse>?) {
                if (response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)) {
                    //  setView(response!!.body().data
//                    Toast.makeText(this@YourProfileActivity, "성공", Toast.LENGTH_SHORT).show()
                    mOtherMyPage(id)
                } else {
                    Log.v("test1043", "onResponse(fail)")
                    Toast.makeText(this@YourProfileActivity, "실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun mOtherMyPage(id : String){
        val otherMyPageResponse : Call<OtherMyPageResponse> = networkService!!.postMyOthersPage(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, id)

        otherMyPageResponse.enqueue(object : Callback<OtherMyPageResponse>
        {
            override fun onFailure(call: Call<OtherMyPageResponse>?, t: Throwable?) {
                Log.v("test1043", "onFailure")
                Log.d("실패하지마라..",t.toString());
                Toast.makeText(this@YourProfileActivity, "실패", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<OtherMyPageResponse>?, response: Response<OtherMyPageResponse>?) {
                if (response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)) {
                    setView(response!!.body().data)
                    user_name = response!!.body().data.user_name
                } else {
                    Log.v("test1043", "onResponse(fail)")
                    Toast.makeText(this@YourProfileActivity, "실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    fun setView(response : OtherMyPageData){

        //팔로우 표시부분 확인하기
        if(response.isFollow){
            profile_follow_btn.setImageResource(R.drawable.profile_follow_on)
        }
        else{
            profile_follow_btn.setImageResource(R.drawable.profie_follow_off)
        }
        profile_user_name.setText(response.user_name)
        yourPF_user_intro.setText(response.user_intro)
        Glide.with(this).load(response!!.user_image).apply(RequestOptions.circleCropTransform()).into(profile_user_image)
        profile_my_lang.setText(response.native_lang)
        profile_wish_lang.setText(response.hope_lang)
        yourPF_following_count.setText(response.following_count.toString())
        yourPF_follower_count.setText(response.follower_count.toString())
        yourPF_user_meeting.setText("'"+response.user_name+"' 의 모임")
        yourPF_user_lounge.setText("'"+response.user_name+"' 의 라운지")
    }


    override fun onClick(v: View?) {
        when(v){
        //팔로워보기
            profile_follower->{
                startActivity(Intent(applicationContext, YourProfileFollowerActivity::class.java))
            }
        //팔로잉보기
            profile_following->{
                startActivity(Intent(applicationContext, YourProfileFollowerActivity::class.java))
            }
            cv_block->{
                val alertdialog = AlertDialog.Builder(this@YourProfileActivity)
                //다이얼로그의 내용을 설정합니다.
                alertdialog.setTitle("차단")
                alertdialog.setMessage("차단 하시겠습니까?")

                //확인 버튼
                alertdialog.setPositiveButton("확인") { dialog, which ->
                    //확인 버튼이 눌렸을 때 토스트를 띄워줍니다.
                    Toast.makeText(this@YourProfileActivity, "확인", Toast.LENGTH_SHORT).show()
                }

                //취소 버튼
                alertdialog.setNegativeButton("취소") { dialog, which ->
                    //취소 버튼이 눌렸을 때 토스트를 띄워줍니다.
                    Toast.makeText(this@YourProfileActivity, "취소", Toast.LENGTH_SHORT).show()
                }

                val alert = alertdialog.create()
                alert.show()


            }
            cv_repport->{
                val alertdialog = AlertDialog.Builder(this@YourProfileActivity)
                //다이얼로그의 내용을 설정합니다.
                alertdialog.setTitle("신고")
                alertdialog.setMessage("신고 하시겠습니까?")

                //확인 버튼
                alertdialog.setPositiveButton("확인") { dialog, which ->
                    //확인 버튼이 눌렸을 때 토스트를 띄워줍니다.
                    Toast.makeText(this@YourProfileActivity, "확인", Toast.LENGTH_SHORT).show()
                }

                //취소 버튼
                alertdialog.setNegativeButton("취소") { dialog, which ->
                    //취소 버튼이 눌렸을 때 토스트를 띄워줍니다.
                    Toast.makeText(this@YourProfileActivity, "취소", Toast.LENGTH_SHORT).show()
                }
                val alert = alertdialog.create()
                alert.show()
            }

//            profile_msg_send_btn->{
//                val roominfo = ChattingRoomData(SharedPreferencesService.instance!!.getPrefStringData("id")!!+otherUserId,null, null, "", "", 0)
//                val roomUpdate = HashMap<String, Any>()
//                roomUpdate.put(SharedPreferencesService.instance!!.getPrefStringData("id")!!+otherUserId, roominfo)
//
//                var reference : DatabaseReference = FirebaseDatabase.getInstance().getReference("roomList")
//                reference.updateChildren(roomUpdate)
//
//
//            }
        /*  profile_follow_btn->{
              if(profile_follow_btn.isSelected()){
                  profile_follow_btn.setImageResource(R.drawable.profile_follow_on)
              }else{
                  profile_follow_btn.setImageResource(R.drawable.profile_follow_btn)
              }
*/
        }
    }


}
