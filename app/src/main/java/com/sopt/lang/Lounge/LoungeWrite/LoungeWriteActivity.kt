package com.sopt.lang.Lounge.LoungeWrite

import android.Manifest
import android.app.Activity
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
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Home.HomeActivity
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.Lounge.LoungePostingResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_lounge_write.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class LoungeWriteActivity : AppCompatActivity() {
    private var networkService: NetworkService? = null
    private var requestManager: RequestManager? = null
//    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"

    var isInserted: Boolean = false
    var image_count = 0

    companion object {
        public var IS_PUBLIC: String = "300"
        public var NO_IMAGE: String = "no_image"
    }

    var GET_PICTURE_URI = 1111

    var imgs: Array<ImageView?>? = null
    var imgUrl: Array<String> = arrayOf(NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE, NO_IMAGE)

    var count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lounge_write)

        SharedPreferencesService.instance!!.load(this)

        var key: String? = null
        key = getIntent().getStringExtra("key")

        networkService = GlobalApplication.instance!!.networkService


        Glide.with(this).load(SharedPreferencesService.instance!!.getPrefStringData("img")).apply(RequestOptions().centerCrop()).into(lounge_update_writer_pic);
        lounge_update_writer_name.setText(SharedPreferencesService.instance!!.getPrefStringData("name"))

        imgs = arrayOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10)

        //  Glide.with(this).load(R.drawable.pic1).apply(RequestOptions.circleCropTransform()).into(lounge_update_writer_pic)
        // lounge_update_writer_name!!.setText("Setin Activity")8

        lounge_update_gallery!!.setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(this@LoungeWriteActivity, Manifest.permission.READ_EXTERNAL_STORAGE)

            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this@LoungeWriteActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                // 권한 없음
            }
            upload_pic_gallery()
        }
    }

    fun upload_pic_gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, GET_PICTURE_URI)
    }

    fun write_in_lounge(v: View) {
        mLoungePosting()
    }

    fun settingImage(): Array<MultipartBody.Part?>? {
        var body: Array<MultipartBody.Part?>? = null
        body = arrayOfNulls(image_count)
        var files = arrayOfNulls<File>(image_count)

        Log.v("test118", image_count.toString())
        for (i in 0..image_count - 1) {
            Log.v("test118", i.toString() + " : " + imgUrl[i])
            if (imgUrl!!.get(i) === NO_IMAGE) {
                body[i] = null
            } else {
                Log.d("imgurl", imgUrl!!.get(i).toString())
                val options = BitmapFactory.Options()

                var `in`: InputStream? = null
                try {
                    `in` = this@LoungeWriteActivity.getContentResolver().openInputStream(Uri.parse(imgUrl!!.get(i)))
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
        return body
    }

    fun mLoungePosting() {

        var content: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), lounge_update_type_text!!.getText().toString())
        var isPublic: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), IS_PUBLIC)

        var body: Array<MultipartBody.Part?>? = null//

        if (isInserted) {
            body = settingImage()
        }

        val loungePostingResponse: Call<LoungePostingResponse> = networkService!!.postLoungePosting(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, content, isPublic, body)
        loungePostingResponse.enqueue(object : Callback<LoungePostingResponse> {
            override fun onFailure(call: Call<LoungePostingResponse>?, t: Throwable?) {
                Toast.makeText(this@LoungeWriteActivity, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoungePostingResponse>?, response: Response<LoungePostingResponse>?) {
                Log.v("test0947", response!!.body().status)
                if (response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)) {
                    var intent = Intent(this@LoungeWriteActivity, HomeActivity::class.java)
                    intent.putExtra("intent","lounge")
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == GET_PICTURE_URI) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
                        count++

                        if (count == 11) {
                            Toast.makeText(this, "이미지는 10개까지 업로드 가능합니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            if (count == 1) {
                                horiozontal_scroll!!.setBackgroundColor(Color.parseColor("#ffffff"))
                                lounge_update_image_box!!.setPadding(dpTopx(16), 0, dpTopx(16), dpTopx(16))
                                isInserted = true
                            }
                            imgUrl[count - 1] = data.getData().toString()
                            Log.v("test118", data.getData().toString())
                            Log.v("test118", imgUrl[count - 1])
                            imgs!![count - 1]!!.layoutParams.width = dpTopx(90)
                            imgs!![count - 1]!!.setPadding(0, 0, dpTopx(8), 0)
                            imgs!![count - 1]!!.requestLayout()
                            Glide.with(this).load(data.getData()).apply(RequestOptions().centerCrop()).into(imgs!![count - 1]);

                            image_count++
                        }
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