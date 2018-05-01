package com.sopt.lang.HomeFragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Meeting.MeetingAdapter
import com.sopt.lang.Meeting.MeetingDetailActivity
import com.sopt.lang.Network.Meeting.MeetingListData
import com.sopt.lang.Network.Meeting.MeetingListDataResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.fragment_meeting.*
import kotlinx.android.synthetic.main.fragment_meeting.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by sec on 2017-12-31.
 */
class MeetingFragment : Fragment(), View.OnClickListener {
    private var onItemClick : View.OnClickListener?=null

    override fun onClick(v: View?) {
        val intent = Intent(context, MeetingDetailActivity::class.java)
        val idx : Int = home_item_list!!.getChildAdapterPosition(v)
        val meeting_id : Int = meeting_data!!.get(idx).meeting_id
        Log.v("meeting_id",meeting_id.toString())
        intent.putExtra("meeting_id",meeting_id)
        startActivity(intent)
    }
    fun setOnItemClickListener(l:View.OnClickListener){
        onItemClick = l
    }

    private var meeting_data: List<MeetingListData>? = null
    private var adapter: MeetingAdapter? = null
    private var networkService: NetworkService? = null
//    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"
    private var requestManager : RequestManager? = null
    private var meetingAdapter : MeetingAdapter?=null
    var meeting_type : Int=100
    companion object {
        public var NETWORK_SERVICE : String="success"
    }
    var v : View? =null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater!!.inflate(R.layout.fragment_meeting, container, false)
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view!!.home_item_list!!.layoutManager = LinearLayoutManager(context)
        meeting_data = ArrayList<MeetingListData>()

        view.home_item_list!!.setNestedScrollingEnabled(false)
        sdf_fragment.findViewById<View>(R.id.sdf_fragment) as NestedScrollView
        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(context)
        // 네트워크 서비스 초기화
        if(this.arguments != null) {
            var bundle: Bundle = this.arguments
            meeting_type = bundle.getInt("type")
            Log.v("Null:", meeting_type.toString())
        }

        mMeetingList()

        view.imageView!!.setOnClickListener{
            Toast.makeText(context, "여기는 광고", Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.home_add, menu) // 메뉴 갖다 붙임
        super.onCreateOptionsMenu(menu, inflater)
    }
    fun  mMeetingList() {
        val meetingListResponse = networkService!!.getMeetingList(SharedPreferencesService.instance!!.getPrefStringData("token", "")
                !!, meeting_type) // type : 100
        meetingListResponse.enqueue(object : Callback<MeetingListDataResponse> {
            override fun onFailure(call: Call<MeetingListDataResponse>?, t: Throwable?) {
                Log.v("실패","2")
            }

            override fun onResponse(call: Call<MeetingListDataResponse>?, response: Response<MeetingListDataResponse>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals(NETWORK_SERVICE)) {
                        v!!.home_item_list!!.layoutManager = LinearLayoutManager(context)
                        meeting_data = response!!.body().data

                        var meetingAdapter = MeetingAdapter(meeting_data!!, context)
                        v!!.home_item_list!!.adapter = meetingAdapter
                        Log.v("type:",meeting_type.toString())
                        //Log.v("성공","1")
                    }
                }
            }

        })
    }

}