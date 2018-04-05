package com.sopt.lang.Network

import com.sopt.lang.Network.Alarm.AlarmMeetingDataResponse
import com.sopt.lang.Network.Alarm.AlarmMyDataResponse
import com.sopt.lang.Network.Base.BaseModel
import com.sopt.lang.Network.Chatting.FriendRecommendResult
import com.sopt.lang.Network.Lounge.*
import com.sopt.lang.Network.ManagementMeeting.ManagementParticipantsResponse
import com.sopt.lang.Network.ManagementMeeting.ManagementWatingListDetailResponse
import com.sopt.lang.Network.Meeting.MeetingApplyResponse
import com.sopt.lang.Network.Meeting.MeetingDetailDataResponse
import com.sopt.lang.Network.Meeting.MeetingLikeResponse
import com.sopt.lang.Network.Meeting.MeetingListDataResponse
import com.sopt.lang.Network.MeetingReview.MeetingReviewListResponse
import com.sopt.lang.Network.MeetingReview.MeetingReviewRegisterResponse
import com.sopt.lang.Network.MyPage.*
import com.sopt.lang.Network.UserManagement.SignCheckDataResponse
import com.sopt.lang.Network.UserManagement.SignUpDataResponse
import com.sopt.lang.Network.UserManagement.SignupPost
import com.sopt.lang.Network.UserManagement.UserInfoPost
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by sec on 2018-01-09.
 */
interface NetworkService {
    //마이페이지
    // 나의 마이페이지
    @GET("/mypage")
    fun getMyMyPage(
            @Header("token") token : String
    ) : Call<MyMyPageResponse>

    // 남의 마이페이지
    @FormUrlEncoded
    @POST("/mypage/otherPage")
    fun postMyOthersPage(
            @Header("token") token : String,
            @Field("otherUserId") otherUserId : String
    ) : Call<OtherMyPageResponse>

    // 내 모임 리스트
    @GET("/mypage/myMeetingList")
    fun getMyMeetingList(
            @Header("token") token : String
    ) : Call<MyMeetingListResponse>

    // 남 모임 리스트
    @FormUrlEncoded
    @POST("/mypage/otherMeetingList")
    fun postOtherMeetingList(
            @Header("token") token : String,
            @Field("otherUserId") otherUserId : String
    ) : Call<OtherMeetingListResponse>

    // 5. 내 라운지 리스트
    @GET("/mypage/myLoungeList")
    fun getMyLoungeList(
            @Header("token") token : String
    ) : Call<MyLoungeListResponse>

    // 6. 남 라운지 리스트
    @FormUrlEncoded

    @POST("/mypage/otherLoungeList")
    fun postOtherLoungeList(
            @Header("token") token : String,
            @Field("otherUserId") otherUserId : String
    ) : Call<OtherLoungeListResponse>

    // 7. 내 팔로워 리스트
    @GET("/mypage/myFollowerList")
    fun getMyFollowerList(
            @Header("token") token : String
    ) : Call<MyFollowerListResponse>

    // 8. 내 팔로잉 리스트
    @GET("/mypage/myFollowingList")
    fun getMyFollowingList(
            @Header("token") token : String
    ) : Call<MyFollowingListResponse>

    // 9. 남 팔로워 리스트
    @FormUrlEncoded
    @POST("/mypage/otherFollowerList")
    fun postOtherFollowerList(
            @Header("token") token : String,
            @Field("otherUserId") otherUserId : String
    ) : Call<OtherFollowerListResponse>

    // 10. 남 팔로잉 리스트
    @FormUrlEncoded
    @POST("/mypage/otherFollowingList")
    fun postOtherFollowingList(
            @Header("token") token : String,
            @Field("otherUserId") otherUserId : String
    ) : Call<OtherFollowingListResponse>

    // 11. 팔로잉 신청 / 취소
    @FormUrlEncoded
    @PUT("/mypage/follow")
    fun putFollow(
            @Header("token") token: String,
            @Field("otherUserId") otherUserId: String
    ): Call<followResponse>

    // 12. 프로필 수정
    @Multipart
    @PUT("/mypage/edit")
    fun putEdit(
            @Header("token") token : String,
            @Part("user_name") user_name : RequestBody,
            @Part("user_introduce") ChangeProfile : RequestBody,
            @Part image : MultipartBody.Part?
    ) : Call<ChangeProfileResponse>


    // 모임리뷰
    // 1. 모임 리뷰 리스트
    @GET("/reviews/{id}")
    fun getReviews(
            @Header("token") token : String,
            @Path("id") id : Int
    ) : Call<MeetingReviewListResponse>

    // 2. 모임 리뷰 등록
    @Multipart
    @POST("/reviews/register/{id}")
    fun postRegisterReview(
            @Header("token") token : String,
            @Path("id") id : Int,

            @Part("rating") rating : RequestBody,
            @Part("content") content : RequestBody,
            @Part image : Array<MultipartBody.Part?>?
    ): Call<MeetingReviewRegisterResponse>

    // 라운지
    // 1. 라운지 리스트
    @GET("/lounge/loungePid/{filter}")
    fun getLoungeList(
            @Header("token") token : String,
            @Path("filter") filter : String
    ) : Call<LoungeListResponse>

    // 2. 라운지 피드 작성
    @Multipart
    @POST("/lounge/loungePosting")
    fun postLoungePosting(
            @Header("token") token : String,
            @Part("lounge_content") lounge_content : RequestBody,
            @Part("isPublic") isPublic : RequestBody,
            @Part image : Array<MultipartBody.Part?>?
    ) : Call<LoungePostingResponse>

    // 3. 라운지 좋아요
    @PUT("/lounge/loungeLike/{lounge_id}")
    fun putLoungeLike(
            @Header("token") token : String,
            @Path("lounge_id") lounge_id : Int
    ) : Call<LoungeLikeResponse>

    // 4. 라운지 댓글 작성
    @FormUrlEncoded
    @POST("/lounge/loungePosting/comment/{id}")
    fun postLoungeCommentPosting(
            @Header("token") token : String,
            @Path("id") id : Int,
            @Field("content") content : String
    ) : Call<LoungeCommentPostingResponse>

    // 5. 라운지 상세보기
    @GET("/lounge/loungeDetail/{lounge_id}")
    fun getLoungeDetail(
            @Header("token") token : String,
            @Path("lounge_id") lounge_id: Int
    ) : Call<LoungeDetailResponse>


    //모임관리
    // 1. 참여자 관리
    @GET("management/participants/{state}/{id}")
    fun getParticipants(
            @Header("token") token : String,
            @Path("state") state: Int,
            @Path("id") id: Int
    ): Call<ManagementParticipantsResponse>

    // 2. 모임 신청자 승인
    @PUT("/management/approval/{meetingid}")
    fun putApproval(
            @Header("token") token : String,
            @Path("meetingid") meetingid : Int,
            @Field("userId") userId: Int
    ): Call<BaseModel>

    // 3. 승인 대기 상세
    @GET("management/watinglist/{id}")
    fun getWaitingList(
            @Header("token") token : String,
            @Path("id") id: String // 참여자 리스트 내 id
    ): Call<ManagementWatingListDetailResponse>

    // 가입 체크
    @POST("/user/signup/check")
    fun signCheck(
            @Body user: UserInfoPost
    ): Call<SignCheckDataResponse>


    // 회원 가입
    @POST("/user/signup")
    fun signUp(
            @Body singup: SignupPost
    ): Call<SignUpDataResponse>

    @GET("/meeting/lists/{type}")
    fun getMeetingList(
            @Header("token") token : String,
            @Path("type") type : Int
    ) : Call<MeetingListDataResponse>

    // 모임 좋아요
    @PUT("/meeting/like/{id}")
    fun putMeetingLike(
            @Header("token") token : String,
            @Path("id") type : Int
    ) : Call<MeetingLikeResponse>

    // 모임 신청
    @FormUrlEncoded
    @POST("/meeting/apply/{id}")
    fun postMeetingApply(
            @Header("token") token : String,
            @Path("id") id : Int,
            @Field("intro") intro : String
    ) : Call<MeetingApplyResponse>


    // 모임 제거
    @DELETE("/meeting/delete/{id}")
    fun deleteMeeting(
            @Header("token") token : String,
            @Path("id") id : Int
    ) : Call<BaseModel>


    // 모임 상세보기
    @GET("/meeting/lists/detail/{id}")
    fun getMeetingDetail(
            @Header("token") token : String,
            @Path("id") id : Int // 모임의 id
    ) : Call<MeetingDetailDataResponse>

    // 모임 개설
    @Multipart
    @POST("/meeting/register")
    fun meetingCreate(
            @Header("token") token : String,
            @Part("title") title : RequestBody,
            @Part("language") language : RequestBody,
            @Part("introduce") intro : RequestBody,
            @Part("type") type : Int,
            @Part("date") date : RequestBody,
            @Part("startTime") startTime : Int,
            @Part("endTime") endTime : Int,
            @Part("lat") lat : RequestBody,  // 위도
            @Part("lng") lng : RequestBody, // 경도
            @Part("question") question : RequestBody,
            @Part image : MultipartBody.Part?
    ) : Call<BaseModel>

    // 친구 추천
    @GET("/chatting/user/recommendation/")
    fun friendRecommend(
            @Header("token") token : String
    ) : Call<FriendRecommendResult>

    //알림리
    // type  = 401
    @GET("/notification/lists/{type}")
    fun alarmListMeeting(
            @Header("token") token : String,
            @Path("type") type : Int
    ) : Call<AlarmMeetingDataResponse>

    // type = 400
    @GET("/notification/lists/{type}")
    fun alarmListMy(
            @Header("token") token : String,
            @Path("type") type : Int
    ) :  Call<AlarmMyDataResponse>

    @GET("/user/status/update")
    fun myInfo(
            @Header("token") token : String
    ) :  Call<myInfoResponse>
}