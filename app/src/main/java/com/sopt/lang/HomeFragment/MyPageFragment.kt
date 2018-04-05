package com.sopt.lang.HomeFragment

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.login.LoginManager
import com.sopt.lang.Login.LoginActivity
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.MyPage.Follow.ProfileFollowerActivity
import com.sopt.lang.MyPage.Follow.ProfileFollowingActivity
import com.sopt.lang.MyPage.MyLounge.MyLoungeActivity
import com.sopt.lang.MyPage.MyMeeting.MyMeetingActivity
import com.sopt.lang.MyPage.MyProfile.MyProfileChangeActivity
import com.sopt.lang.Network.MyPage.MyMyPageData
import com.sopt.lang.Network.MyPage.MyMyPageResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




/**
 * Created by sec on 2018-01-08.
 */
class MyPageFragment : Fragment() {

    private var networkService: NetworkService? = null
    private var myprofile_data: MyMyPageData? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_my_profile, container, false)
        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(context)

        v.layout_meeting
        v.layout_lounge
        v.layout_info

        v.logout_btn
        v.profile_change
        v.profile_follower
        v.profile_following


        Log.v("프로필 토큰",SharedPreferencesService.instance!!.getPrefStringData("token"))
        val mypageResponse = networkService!!.getMyMyPage(SharedPreferencesService.instance!!.getPrefStringData("token")!!)
        mypageResponse.enqueue(object : Callback<MyMyPageResponse> {
            override fun onResponse(call: Call<MyMyPageResponse>?, response: Response<MyMyPageResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
                        GlobalApplication.instance!!.makeToast("성공")
                        myprofile_data = response!!.body().data
                        Log.v("ddddd", myprofile_data!!.user_name)
                        profile_user_name!!.setText(myprofile_data!!.user_name)
                        profile_my_lang!!.setText(myprofile_data!!.native_lang)
                        profile_wish_lang!!.setText(myprofile_data!!.hope_lang)
                        my_follower!!.setText(myprofile_data!!.follower_count.toString())
                        my_following.setText(myprofile_data!!.following_count.toString())
                        intro_text!!.setText(myprofile_data!!.user_intro)
                        my_meeting_count!!.setText("내 모임  (" + myprofile_data!!.meetingNum.toString() + ")")
                        my_lounge_count!!.setText("내 라운지  (" + myprofile_data!!.loungeNum.toString() + ")")


                        if(myprofile_data!!.user_image !=null){
                            //  val decodedString = Base64.decode(myprofile_data!!.user_image, Base64.URL_SAFE)
                            //  val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                            Glide.with(context).load(myprofile_data!!.user_image).apply(RequestOptions.circleCropTransform())
                                    .into(profile_user_image)
                        }else{
                            Glide.with(context).load(R.drawable.profile_default).apply(RequestOptions.circleCropTransform())
                                    .into(profile_user_image)
                            myprofile_data!!.user_image = "0"
                        }
                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<MyMyPageResponse>?, t: Throwable?) {
                Log.v("444", t.toString())

                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })

        v.profile_change.setOnClickListener {
            val change_Activity = Intent(context, MyProfileChangeActivity::class.java)
            change_Activity.putExtra("img",myprofile_data!!.user_image)
            change_Activity.putExtra("name",myprofile_data!!.user_name)
            change_Activity.putExtra("intro",myprofile_data!!.user_intro)
            startActivity(change_Activity)
        }

        v.layout_meeting.setOnClickListener {
            val meeting_Activity = Intent(context, MyMeetingActivity::class.java)
            startActivity(meeting_Activity)
        }
        v.layout_lounge.setOnClickListener {
            val mylounge_Activity = Intent(context, MyLoungeActivity::class.java)
            startActivity(mylounge_Activity)
        }
        v.logout_btn.setOnClickListener {
            if (com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen()) {
                com.kakao.auth.Session.getCurrentSession().close()
            }
            if (SharedPreferencesService.instance!!.getPrefStringData("token", "") != "") {
                SharedPreferencesService.instance!!.setPrefData("token", "")
                LoginManager.getInstance().logOut()
                // facebook_login_button.performClick();
            }
            val i = Intent(context, LoginActivity::class.java)
            i.flags = FLAG_ACTIVITY_NO_ANIMATION
            startActivity(i)
        }
        v.profile_follower.setOnClickListener {
            val follower_activity = Intent(context, ProfileFollowerActivity::class.java)
            startActivity(follower_activity)
        }
        v.profile_following.setOnClickListener {
            val following_activity = Intent(context, ProfileFollowingActivity::class.java)
            startActivity(following_activity)
        }

        return v

    }

}