package com.sopt.lang.Lounge.LoungeDetail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Network.Lounge.LoungeDetailComment
import com.sopt.lang.R

/**
 * Created by sec on 2018-01-07.
 */
class LoungeCommentAdapter(var dataList : List<LoungeDetailComment>?, var ctx : Context?) : RecyclerView.Adapter<LoungeCommentViewHolder>() {

    var mLoungeCommentViewHolder : LoungeCommentViewHolder? = null


    private var onItemClick : View.OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LoungeCommentViewHolder {
        val lounge_comment_view : View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_lounge_detail_comment, parent, false)

        mLoungeCommentViewHolder = LoungeCommentViewHolder(lounge_comment_view)


        return LoungeCommentViewHolder(lounge_comment_view)

    }

    override fun onBindViewHolder(holder: LoungeCommentViewHolder?, position: Int) {
        Log.v("test0331", "comment : " + position + dataList!!.get(position).user_name + dataList!!.get(position).comment_content)
        Glide.with(ctx).load(dataList!!.get(position).user_image).apply(RequestOptions.circleCropTransform()).into(holder!!.writer_pic)
        //holder!!.writer_pic.setImageResource(dataList!!.get(position).writer_pic)



        holder!!.writer_pic.setOnClickListener{
            mLoungeCommentViewHolder!!.toProfileActivity(dataList!!.get(position).user_id, ctx)
        }

        holder!!.writer_name.setText(dataList!!.get(position).user_name)
        holder!!.uploaded_time.setText(dataList!!.get(position).comment_time)
        holder!!.comment_text.setText(dataList!!.get(position).comment_content)
    }

    override fun getItemCount() : Int = dataList!!.size
    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l
    }

    fun refreshAdapter(dataList :List<LoungeDetailComment>){
        this.dataList = dataList
        notifyDataSetChanged()
    }
}