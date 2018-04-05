package com.sopt.lang.MyPage.MyProfile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Home.HomeActivity
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.MyPage.ChangeProfileResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_my_profile_change.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

class MyProfileChangeActivity : AppCompatActivity() {

    private var networkService: NetworkService? = null
    var changeImgString: String? = null
    private var data: Uri? = null
    var GET_PICTURE_URI = 1111
    private var image: MultipartBody.Part? = null
    private val REQ_CODE_SELECT_IMAGE = 100
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile_change)

        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(this)

        val content = SpannableString("프로필 사진 수정")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        profile_change.text = content

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        emptyImageSet()

        if (intent.getStringExtra("img")==null ||intent.getStringExtra("img").equals("0")) {
            profile_user_change_image.setImageResource(R.drawable.profile_default)
        } else {
            Glide.with(application).load(intent.getStringExtra("img")).apply(RequestOptions.circleCropTransform())
                    .into(profile_user_change_image)
        }
        changeImgString = intent.getStringExtra("img")
        profile_user_change_name.setText(intent.getStringExtra("name"))
        profile_user_change_about.setText(intent.getStringExtra("intro"))

        profile_change.setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(this@MyProfileChangeActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this@MyProfileChangeActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                // 권한 없음
            }
            changeImage()
        }
        profile_save.setOnClickListener {

            val name = RequestBody.create(MediaType.parse("text/plain"), profile_user_change_name.text.toString())
            val intro = RequestBody.create(MediaType.parse("text/plain"), profile_user_change_about.text.toString())

            val changeResponse = networkService!!.putEdit(SharedPreferencesService.instance!!.getPrefStringData("token")!!, name,intro,image)
            changeResponse.enqueue(object : Callback<ChangeProfileResponse> {
                override fun onResponse(call: Call<ChangeProfileResponse>?, response: Response<ChangeProfileResponse>?) {
                    if (response!!.isSuccessful) {
                        if (response!!.body().status.equals("success")) {
//                            GlobalApplication.instance!!.makeToast("성공")
                            val goProfile = Intent(this@MyProfileChangeActivity, HomeActivity::class.java)
                            goProfile.putExtra("intent","profile")
                            startActivity(goProfile)

                        } else {
                            GlobalApplication.instance!!.makeToast("정보를 확인해주세요")
                        }
                    }
                }

                override fun onFailure(call: Call<ChangeProfileResponse>?, t: Throwable?) {
                    Log.v("444", t.toString())

                    GlobalApplication.instance!!.makeToast("통신 상태를 확인해주세요")
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    this.data = data!!.data
                    Log.v("이미지", this.data.toString())

                    val options = BitmapFactory.Options()

                    var input: InputStream? = null // here, you need to get your context.
                    try {
                        input = contentResolver.openInputStream(this.data)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    val bitmap = BitmapFactory.decodeStream(input, null, options) // InputStream 으로부터 Bitmap 을 만들어 준다.
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                    val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())
                    val photo = File(this.data.toString()) // 가져온 파일의 이름을 알아내려고 사용합니다

                    image = MultipartBody.Part.createFormData("image", photo.name, photoBody)

                    Glide.with(this)
                            .load(data.data)
                            .apply(RequestOptions.circleCropTransform())
                            .into(profile_user_change_image)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun changeImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE)
    }
    fun emptyImageSet(){
        try {

            val options = BitmapFactory.Options()

            var input: InputStream? = null // here, you need to get your context.
            try {
                input = contentResolver.openInputStream(Uri.parse(""))
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            val bitmap = BitmapFactory.decodeStream(input, null, options) // InputStream 으로부터 Bitmap 을 만들어 준다.
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
            val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())

            image = MultipartBody.Part.createFormData("image", "", photoBody)
            Log.v("이미지", image.toString())

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}