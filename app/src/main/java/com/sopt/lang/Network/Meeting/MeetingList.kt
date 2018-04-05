package com.sopt.lang.Network.Meeting

import com.google.gson.annotations.SerializedName
import com.sopt.lang.Network.Base.BaseModel

/**
 * Created by sec on 2018-01-08.
 */
data class MeetingList(
        @SerializedName("data")
        var data : List<MeetingListDataResponse>
) : BaseModel()
