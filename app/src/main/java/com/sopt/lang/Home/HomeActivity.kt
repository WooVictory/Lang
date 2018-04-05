package com.sopt.lang.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.sopt.lang.HomeFragment.*
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Lounge.LoungeWrite.LoungeWriteActivity
import com.sopt.lang.Meeting.MeetingAdapter
import com.sopt.lang.Meeting.MeetingCreate.MeetingCreateActivity1
import com.sopt.lang.Meeting.MeetingData
import com.sopt.lang.Meeting.MeetingDetailActivity
import com.sopt.lang.Network.MyInfoData
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.Network.myInfoResponse
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity(), View.OnClickListener {


/*    internal var adapter: ViewPagerAdapter?=null
    internal var chat_fragment: ChatFragment?=null
    internal var home_fragment: MeetingFragment?=null
    internal var lounge_fragment: LoungeFragment?=null*/

    private var networkService: NetworkService? = null
    private val FRAGMENT1 = 1
    private val FRAGMENT2 = 2
    private var meeting_data: ArrayList<MeetingData>? = null
    private var adapter: MeetingAdapter? = null
    private var type: Int = 0
    private var homeContext: Context? = null

    var myData : MyInfoData ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)

        Log.v("토토토큰ㅋ늨느",SharedPreferencesService.instance!!.getPrefStringData("token")!!)

        val userResponse = networkService!!.myInfo(SharedPreferencesService.instance!!.getPrefStringData("token")!!)
        userResponse.enqueue(object : Callback<myInfoResponse> {
            override fun onResponse(call: Call<myInfoResponse>?, response: Response<myInfoResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("success")) {
//                        GlobalApplication.instance!!.makeToast("성공")
                        myData=response!!.body().data
                        Log.v("아이디ㅋㅋㅋ",myData!!.user_id)

                        SharedPreferencesService.instance!!.setPrefData("id", myData!!.user_id)
                        SharedPreferencesService.instance!!.setPrefData("name", myData!!.user_name)

                        if( myData!!.user_image !=null){
                            SharedPreferencesService.instance!!.setPrefData("img", myData!!.user_image)
                        }

                        Log.v("아이디?????",SharedPreferencesService.instance!!.getPrefStringData("id"))
                        Log.v("이미지?????",SharedPreferencesService.instance!!.getPrefStringData("img"))
                        Log.v("이름?????",SharedPreferencesService.instance!!.getPrefStringData("name"))

                    } else {
                        GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                    }
                }
            }

            override fun onFailure(call: Call<myInfoResponse>?, t: Throwable?) {
                Log.v("444", t.toString())
                GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
            }
        })

        FirebaseMessaging.getInstance().subscribeToTopic("news")
        FirebaseInstanceId.getInstance().getToken()


//        try {
//            var info: PackageInfo = getPackageManager().getPackageInfo("com.sopt.lang", PackageManager.GET_SIGNATURES);
//            for (signature: android.content.pm.Signature in info.signatures) {
//                var md: MessageDigest = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace();
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace();
//        }

        homeContext = this

        tab.addTab(tab.newTab().setIcon(R.drawable.tab_home_selector))
        tab.addTab(tab.newTab().setIcon(R.drawable.tab_lounge_selector))
        tab.addTab(tab.newTab().setIcon(R.drawable.tab_chat_selector))
        tab.addTab(tab.newTab().setIcon(R.drawable.tab_alarm_selector))
        tab.addTab(tab.newTab().setIcon(R.drawable.tab_my_selector))


        home_filter!!.setOnClickListener {
            home_filter.isSelected = !home_filter.isSelected
            if (home_filter.isSelected) {
                home_filter.setImageResource(R.drawable.home_filter_on)
                filter_bar.visibility = View.VISIBLE
                all_showing!!.setImageResource(R.drawable.home_filter_all_on)
            } else {
                home_filter.setImageResource(R.drawable.home_filter_off)
                filter_bar.visibility = View.GONE
            }
        }

        all_showing!!.setOnClickListener {
            all_showing.isSelected = !all_showing.isSelected
            if (all_showing.isSelected) {
                all_showing!!.setImageResource(R.drawable.home_filter_all_on)
                lang_exchange!!.setImageResource(R.drawable.home_filter_languageexchange_off)
                party!!.setImageResource(R.drawable.home_filter_party_off)
                lang_exchange!!.isClickable = false
                party!!.isClickable = false
                var meeting_fragment = MeetingFragment()
                val bundle = Bundle()
                bundle!!.putInt("type", 100)
                meeting_fragment!!.arguments = bundle
                Log.v("type:", bundle.get("type").toString())
                replaceFragment(meeting_fragment, "Home")
                lang_exchange!!.isClickable = true
                party!!.isClickable = true
                all_showing.isSelected = !all_showing.isSelected
            }
        }

        lang_exchange!!.setOnClickListener {
            lang_exchange.isSelected = !all_showing.isSelected
            if (lang_exchange.isSelected) {
                lang_exchange!!.setImageResource(R.drawable.home_filter_languageexchange_on)
                all_showing!!.setImageResource(R.drawable.home_filter_all_off)
                party!!.setImageResource(R.drawable.home_filter_party_off)
                all_showing!!.isClickable = false
                party!!.isClickable = false
                var meeting_fragment = MeetingFragment()
                val bundle = Bundle()
                bundle!!.putInt("type", 101)
                meeting_fragment!!.arguments = bundle
                Log.v("type:", bundle.get("type").toString())
                replaceFragment(meeting_fragment, "Home")
                all_showing!!.isClickable = true
                party!!.isClickable = true
            }
        }

        party!!.setOnClickListener {
            party.isSelected = !all_showing.isSelected
            if (party.isSelected) {
                party!!.setImageResource(R.drawable.home_filter_party_on)
                all_showing!!.setImageResource(R.drawable.home_filter_all_off)
                lang_exchange!!.setImageResource(R.drawable.home_filter_languageexchange_off)
                all_showing!!.isClickable = false
                lang_exchange!!.isClickable = false
                var meeting_fragment = MeetingFragment()
                val bundle = Bundle()
                bundle!!.putInt("type", 102)
                meeting_fragment!!.arguments = bundle
                Log.v("type:", bundle.get("type").toString())
                replaceFragment(meeting_fragment, "Home")
                all_showing!!.isClickable = true
                lang_exchange!!.isClickable = true
            }
        }

        addFragment(MeetingFragment(), "Home")
        home_add_btn!!.setOnClickListener {
            startActivity(Intent(homeContext, MeetingCreateActivity1::class.java))
        }

        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
//                        appBar.visibility = View.VISIBLE
                        home_filter!!.visibility = View.VISIBLE
                        home_filter!!.setImageResource(R.drawable.home_filter_off)
                        home_add_btn!!.visibility = View.VISIBLE
                        home_add_btn!!.setImageResource(R.drawable.home_plus)
                        home_add_btn!!.setOnClickListener {
                            startActivity(Intent(homeContext, MeetingCreateActivity1::class.java))
                        }
                        replaceFragment(MeetingFragment(), "Home")
                        Log.v("YG", "0")
                    }
                    1 -> {
//                        home_add_btn!!.visibility = View.GONE
                        home_filter!!.visibility = View.INVISIBLE
                        filter_bar.visibility = View.GONE
                        home_add_btn!!.setImageResource(R.drawable.review_write)
                        home_add_btn!!.visibility = View.VISIBLE
                        home_add_btn!!.setOnClickListener {
                            startActivity(Intent(homeContext, LoungeWriteActivity::class.java))
                        }
                        replaceFragment(LoungeFragment(), "Lounge")
                        Log.v("YG", "1")

                    }
                    2 -> {
                        home_filter!!.visibility = View.VISIBLE
                        filter_bar.visibility = View.GONE
                        home_filter!!.setImageResource(R.drawable.chatting_edit)
                        home_filter!!.isEnabled = false
                        home_add_btn!!.visibility = View.INVISIBLE
                        replaceFragment(ChattingFragment(), "Chatting")
                        Log.v("YG", "2")

                    }
                    3 -> {
                        home_filter!!.visibility = View.INVISIBLE
                        filter_bar.visibility = View.GONE
                        home_add_btn!!.visibility = View.INVISIBLE
                        replaceFragment(AlarmFragment(), "Alarm")
                        Log.v("YG", "3")

                    }
                    4 -> {
                        home_filter!!.visibility = View.INVISIBLE
                        filter_bar.visibility = View.GONE
                        home_add_btn!!.visibility = View.GONE
                        replaceFragment(MyPageFragment(), "MyProfile")
                        Log.v("YG", "4")

                    }
                }
            }
        })
        if (intent.getStringExtra("intent") != null) {
            if (intent.getStringExtra("intent").equals("lounge")) {
                Log.v("intent","라운지")
                home_filter!!.visibility = View.INVISIBLE
                filter_bar.visibility = View.GONE
                home_add_btn!!.setImageResource(R.drawable.review_write)
                home_add_btn!!.visibility = View.VISIBLE
                home_add_btn!!.setOnClickListener {
                    startActivity(Intent(homeContext, LoungeWriteActivity::class.java))
                }
                replaceFragment(LoungeFragment(), "Lounge")
                Log.v("YG", "라운지")

            } else if (intent.getStringExtra("intent").equals("profile")) {
                Log.v("intent","프로필")

                home_filter!!.visibility = View.INVISIBLE
                filter_bar.visibility = View.GONE
                home_add_btn!!.visibility = View.GONE
                replaceFragment(MyPageFragment(), "MyProfile")
                Log.v("YG", "프로필")
            }
        }
    }

    override fun onClick(v: View) {
        val intent: Intent = Intent(applicationContext, MeetingDetailActivity::class.java)
        /*       val idx : Int = home_list!!.getChildAdapterPosition(v) // 내가 선택한 리스트가 몇 번째의 리스트인지 인덱스를 나타냄
val name : String? = meeting_data!!.get(idx).groupHost // 이것은 이름이 무엇인가??
val type : String? = meeting_data!!.get(idx).pokemonType
intent.putExtra("idx",idx)
intent.putExtra("name",name)
intent.putExtra("type",type)*/
        startActivity(intent)
        // Toast.makeText(this, name+"\n"+type, Toast.LENGTH_LONG).show()
        // 간단한 토스트 메시지
    }

    fun addFragment(fragemnt: Fragment, tag: String) {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.add(fragment_container.id, fragemnt, tag)
        //transaction.addToBackStack(null)
        transaction.commit()
    }

    fun replaceFragment(fragemnt: Fragment, tag: String) {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(fragment_container.id, fragemnt, tag)
        //transaction.addToBackStack(null)
        transaction.commit()
    }
}