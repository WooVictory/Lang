package com.sopt.lang

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_lounge_image.*

/**
 * Created by kor on 2018-01-06.
 */
class LoungeImageActivity : AppCompatActivity() {

    //lateinit var mPager : ViewPager
    //var path : IntArray = intArrayOf(R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4)    //임의로 넣어둔거임
    // 서버로부터 받아오세여
    //var total_img : TextView? = null
    //var current_img : TextView? = null
    var current_img_num : String? = null
    //var start_position : Int? = null
    // 선택한 이미지에 따라 숫자 다르게 줄 것

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lounge_image)

        SharedPreferencesService.instance!!.load(this)


        var path : ArrayList<String> = getIntent().getStringArrayListExtra("img_array")
        current_img_num = getIntent().getStringExtra("current_img_num")

        var start_position = current_img_num!!.toInt()


        total_img!!.setText(path!!.size.toString())
        current_img!!.setText((start_position+1).toString())
        // current_img!!.setText("1")


        //mPager = findViewById(R.id.pager) as ViewPager
        var adapter : PagerAdapter = LoungeImageAdapter(this, path)
        pager.adapter = adapter
        pager.setCurrentItem(start_position)
        pager.addOnPageChangeListener(object  : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                /*Toast.makeText(this@LoungeImageActivity, "이거지롱", Toast.LENGTH_SHORT).show()*/
                current_img!!.setText((position+1).toString())
            }

        })
    }
}