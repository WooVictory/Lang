package com.sopt.lang.Meeting.MeetingReview

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.MeetingReview.MeetingReviewRegisterResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_review_write.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class ReviewWriteActivity : AppCompatActivity() {
    private var networkService: NetworkService? = null
    private var requestManager : RequestManager? = null
    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"

    var isInserted : Boolean = false
    var image_count = 0

    companion object {
        public var IS_PUBLIC : String = "300"
        public var NO_IMAGE : String = "no_image"
    }

    var imgUrl : Array<String> = arrayOf(NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE)


    var img1: ImageView? = null
    var img2: ImageView? = null
    var img3: ImageView? = null
    var img4: ImageView? = null
    var img5: ImageView? = null
    var img6: ImageView? = null
    var img7: ImageView? = null
    var img8: ImageView? = null
    var img9: ImageView? = null
    var img10: ImageView? = null

    var count: Int = 0

    var imgs: Array<ImageView?>? = null
    var img_scroll_view: HorizontalScrollView? = null
    var img_box: LinearLayout? = null
    var toolbar: Toolbar? = null
    var gallery_img : ImageView? = null

    var rating_global : Float = 0.toFloat()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_write)

        Log.v("test618", "rating1 : "+ rating_global.toString())

        var key : Int? = null
        key = getIntent().getIntExtra("key", 0)

        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        var rb: RatingBar = findViewById<View>(R.id.review_write_rating) as RatingBar
        var rb_value = findViewById<View>(R.id.review_write_rating_num) as TextView

        img1 = findViewById<View>(R.id.img1) as ImageView
        img2 = findViewById<View>(R.id.img2) as ImageView
        img3 = findViewById<View>(R.id.img3) as ImageView
        img4 = findViewById<View>(R.id.img4) as ImageView
        img5 = findViewById<View>(R.id.img5) as ImageView
        img6 = findViewById<View>(R.id.img6) as ImageView
        img7 = findViewById<View>(R.id.img7) as ImageView
        img8 = findViewById<View>(R.id.img8) as ImageView
        img9 = findViewById<View>(R.id.img9) as ImageView
        img10 = findViewById<View>(R.id.img10) as ImageView

        imgs = arrayOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10)

        img_scroll_view = findViewById<View>(R.id.review_write_horiozontal_scroll_view) as HorizontalScrollView
        img_box = findViewById<View>(R.id.review_write_image_box) as LinearLayout

        gallery_img = findViewById<View>(R.id.review_write_gallery) as ImageView

        rb.setOnRatingBarChangeListener(RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            rb_value.setText(rating.toString())
            rating_global = rating
            Log.v("test618", "rating1 : "+ rating_global.toString())
        })

        val activity = findViewById<View>(R.id.review_write_box) as RelativeLayout

        activity.setOnClickListener {
            val edit = findViewById<View>(R.id.review_edit) as EditText
            if (!edit.performClick()) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(edit.windowToken, 0)
            }
        }

        gallery_img!!.setOnClickListener{
            val permissionCheck = ContextCompat.checkSelfPermission(this@ReviewWriteActivity, Manifest.permission.READ_EXTERNAL_STORAGE)

            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this@ReviewWriteActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                // 권한 없음
            }
            upload_pic_gallery()
        }

        write_review.setOnClickListener {
            mMeeitngReviewPosting(key!!)
        }
    }

    fun mMeeitngReviewPosting(id : Int){
        var rating_input : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),rating_global.toString())
        var content_input : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), review_edit.getText().toString())

        Log.v("test618", "token : "+ SharedPreferencesService.instance!!.getPrefStringData("token", ""))
        Log.v("test618", "id : "+ id.toString())
        Log.v("test618", "rating : "+ rating_global.toString())
        Log.v("test618", "content : "+ review_edit.getText())

        var body: Array<MultipartBody.Part?>? = null

        if(isInserted){
            body = settingImage()
        }
        /*else{
            imgUrl[0] = ""
            body = settingImage()
        }*/

        val meetingreviewResponse : Call<MeetingReviewRegisterResponse> = networkService!!.postRegisterReview(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, id, rating_input, content_input, body)
        meetingreviewResponse.enqueue(object : Callback<MeetingReviewRegisterResponse>
        {
            override fun onFailure(call: Call<MeetingReviewRegisterResponse>?, t: Throwable?) {
                Toast.makeText(this@ReviewWriteActivity, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MeetingReviewRegisterResponse>?, response: Response<MeetingReviewRegisterResponse>?) {
                if(response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)){
                    var intent = Intent(this@ReviewWriteActivity, MeetingReviewActivity::class.java)
                    intent.putExtra("key", id)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

        })
    }

    fun settingImage(): Array<MultipartBody.Part?>? {
        var body: Array<MultipartBody.Part?>? = null
        body = arrayOfNulls(image_count)
        var files = arrayOfNulls<File>(image_count)

        Log.v("test118", image_count.toString())
        if (image_count == 0) {
            val options = BitmapFactory.Options()

            var `in`: InputStream? = null
            try {
                `in` = this@ReviewWriteActivity.getContentResolver().openInputStream(Uri.parse(imgUrl!!.get(0)))
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            val bitmap = BitmapFactory.decodeStream(`in`, null, options)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)

            try {
                `in`!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())

            files[0] = File(imgUrl!!.get(0))

            body[0] = MultipartBody.Part.createFormData("image", files[0]!!.getName(), photoBody)
            bitmap.recycle()
        } else {
            for (i in 0..image_count - 1) {
                if (imgUrl!!.get(i) === NO_IMAGE) {
                    body[i] = null
                } else {
                    val options = BitmapFactory.Options()

                    var `in`: InputStream? = null
                    try {
                        `in` = this@ReviewWriteActivity.getContentResolver().openInputStream(Uri.parse(imgUrl!!.get(i)))
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    val bitmap = BitmapFactory.decodeStream(`in`, null, options)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)

                    try {
                        `in`!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())

                    files[i] = File(imgUrl!!.get(i))

                    body[i] = MultipartBody.Part.createFormData("image", files[i]!!.getName(), photoBody)
                    bitmap.recycle()
                }
            }
        }
        return body
    }

    fun upload_pic_gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, 20)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    count++
                    if (count == 1) {
                        img_scroll_view!!.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        img_box!!.setPadding(dpTopx(16), 0, 0, dpTopx(16))
                        isInserted = true
                    }

                    if (count == 11) {
                        Toast.makeText(this, "이미지는 10개까지 업로드 가능합니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        imgUrl[count - 1] = data.getData().toString()
                        imgs!![count - 1]!!.layoutParams.width = dpTopx(90)
                        imgs!![count - 1]!!.setPadding(0, 0, dpTopx(8), 0)
                        imgs!![count - 1]!!.requestLayout()
                        Glide.with(this).load(data.getData()).apply(RequestOptions().centerCrop()).into(imgs!![count-1]);
                        image_count++
                    }
                } catch (e: Exception) {
                    Log.e("test", e.message)
                }
            }
        }
    }

    fun dpTopx(dp: Int): Int {
        var density: Float = this.getResources().getDisplayMetrics().density
        return Math.round((dp.times(density)))
    }
}
