package com.sopt.lang

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by kor on 2018-01-07.
 */
class LoungeImageAdapter : PagerAdapter {

    var con : Context
    var path : ArrayList<String> // 서버에서 받아와야함
    lateinit var inflator : LayoutInflater

    constructor(con : Context, path : ArrayList<String>) : super(){
        this.con = con
        this.path = path    // 서버
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return path!!.size
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        var img : ImageView
        inflator = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var rv : View = inflator.inflate(R.layout.item_lounge_image, container, false)
        img = rv.findViewById<ImageView>(R.id.child_image) as ImageView
        //img.setImageResource(path[position])
        Glide.with(con).load(path!![position]).apply(RequestOptions().centerCrop()).into(img);
        container!!.addView(rv)
        img.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(con, "You Click Image : " + (position+1), Toast.LENGTH_LONG).show()
            }
        })
        return rv
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        /*container!!.removeView('object' as View)*/
    }

}