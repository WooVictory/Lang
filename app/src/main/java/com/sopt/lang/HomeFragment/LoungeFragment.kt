package com.sopt.lang.HomeFragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TabHost
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Lounge.LoungeAdapter
import com.sopt.lang.Network.Lounge.LoungeListData
import com.sopt.lang.Network.Lounge.LoungeListResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import kotlinx.android.synthetic.main.fragment_lounge.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by sec on 2017-12-31.
 */
class LoungeFragment : Fragment(), View.OnClickListener {

    private var networkService: NetworkService? = null
    private var requestManager: RequestManager? = null

    companion object {
        public var NETWORK_SUCCESS: String? = "success"
    }

    var mLoungeAdapter: LoungeAdapter? = null

    var v : View? = null

    var ts1: TabHost.TabSpec? = null
    var ts2: TabHost.TabSpec? = null
    var lounge_tabhost: TabHost? = null

    var which_tab: String? =null
    //    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"
    var vOnClick: View.OnClickListener? = null


    private var loungeDatas: List<LoungeListData>? = null
    private var adapter: LoungeAdapter? = null


    var lounge_tab: View? = null
    var following_tab: View? = null

    var write_in_lounge: ImageView? = null

    override fun onStart(){
        super.onStart()
        mLoungeList()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater!!.inflate(R.layout.fragment_lounge, container, false)

        v!!.lounge_tabhost
        v!!.lounge_tabhost!!.setup()

        Log.v("테스트1146", "check")

        SharedPreferencesService.instance!!.load(context)
        networkService = GlobalApplication.instance!!.networkService

        ts1 = v!!.lounge_tabhost!!.newTabSpec("tab1")
        ts1!!.setIndicator("라운지")
        ts1!!.setContent(R.id.tab1)
        v!!.lounge_tabhost!!.addTab(ts1)

        ts2 = v!!.lounge_tabhost!!.newTabSpec("tab2")
        ts2!!.setIndicator("팔로잉")
        ts2!!.setContent(R.id.tab2)
        v!!.lounge_tabhost!!.addTab(ts2)

        v!!.lounge_tabhost!!.setCurrentTabByTag("tab1")
        which_tab ="else"

        v!!.lounge_tab
        v!!.following_tab

        val tv = v!!.lounge_tabhost!!.tabWidget.getChildAt(v!!.lounge_tabhost!!.getCurrentTab()).findViewById<View>(android.R.id.title) as TextView
        tv.setTextColor(Color.parseColor("#111111"))
//        val tv = v!!.lounge_tabhost!!.tabWidget.getChildAt(v!!.lounge_tabhost!!.getCurrentTab()).findViewById<View>(android.R.id.title) as TextView
//        tv.setTextColor(Color.parseColor("#111111"))

        v!!.lounge_tabhost!!.setOnTabChangedListener(TabHost.OnTabChangeListener {
            for (i in 0 until v!!.lounge_tabhost!!.getTabWidget().getChildCount()) {
//                v.lounge_tabhost!!.getTabWidget().getChildAt(i).setBackgroundColor(this.getResources().getColor(R.color.another_tab))
                val tv = v!!.lounge_tabhost!!.tabWidget.getChildAt(i).findViewById<View>(android.R.id.title) as TextView
                tv.setTextColor(Color.parseColor("#bcbcbc"))
            }
//            v.lounge_tabhost!!.getTabWidget().getChildAt(v.lounge_tabhost!!.getCurrentTab()).setBackgroundColor(this.getResources().getColor(R.color.choosed_tab))
            val tv = v!!.lounge_tabhost!!.tabWidget.getChildAt(v!!.lounge_tabhost!!.getCurrentTab()).findViewById<View>(android.R.id.title) as TextView
            tv.setTextColor(Color.parseColor("#111111"))

            if (which_tab == "else") {
                which_tab = "following"
                v!!.lounge_tab!!.setBackgroundColor(this.getResources().getColor(R.color.another_tab))
                v!!.following_tab!!.setBackgroundColor(this.getResources().getColor(R.color.choosed_tab))
            } else {
                which_tab = "else"
                v!!.lounge_tab!!.setBackgroundColor(this.getResources().getColor(R.color.choosed_tab))
                v!!.following_tab!!.setBackgroundColor(this.getResources().getColor(R.color.another_tab))
            }
        })


       /* v!!.lounge_tabhost!!.setOnTabChangedListener {
            if (which_tab == "else") {
                which_tab = "following"
                v!!.lounge_tab!!.setBackgroundColor(this.getResources().getColor(R.color.another_tab))
                v!!.following_tab!!.setBackgroundColor(this.getResources().getColor(R.color.choosed_tab))
            } else {
                which_tab = "else"
                v!!.lounge_tab!!.setBackgroundColor(this.getResources().getColor(R.color.choosed_tab))
                v!!.following_tab!!.setBackgroundColor(this.getResources().getColor(R.color.another_tab))
            }
        }*/

        Log.v("냐ㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑ", SharedPreferencesService.instance!!.getPrefStringData("token"))
        mLoungeList()

        return v
    }

    fun mLoungeList() {
        val loungeListResponge: Call<LoungeListResponse> = networkService!!.getLoungeList(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, which_tab!!)
        loungeListResponge.enqueue(object : Callback<LoungeListResponse> {
            override fun onFailure(call: Call<LoungeListResponse>?, t: Throwable?) {
            }
            override fun onResponse(call: Call<LoungeListResponse>?, response: Response<LoungeListResponse>?) {
                if (response!!.body().status.equals(NETWORK_SUCCESS)) {
                    v!!.main_list!!.layoutManager = LinearLayoutManager(context)
                    if (response.body().data == null) {
                        Log.v("널널널","니ㅓㄹㄴ러널")
                    } else{
                        loungeDatas = response!!.body().data
                        adapter = LoungeAdapter(loungeDatas, context)
                        adapter!!.refreshAdapter(loungeDatas!!)
                        v!!.main_list!!.adapter = adapter
                    }
                    Log.v("성공", "1")
                }
            }
        })
    }

    override fun onClick(p0: View?) {

    }
}