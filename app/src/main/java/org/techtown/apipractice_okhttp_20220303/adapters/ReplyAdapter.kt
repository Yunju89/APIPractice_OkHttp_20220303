package org.techtown.apipractice_okhttp_20220303.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.techtown.apipractice_okhttp_20220303.R
import org.techtown.apipractice_okhttp_20220303.datas.ReplyData
import org.techtown.apipractice_okhttp_20220303.datas.TopicData
import java.util.*

class ReplyAdapter(
    val mContext : Context,
    val resId : Int,
    val mList: List<ReplyData>
): ArrayAdapter<ReplyData>(mContext, resId, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tmpRow = convertView
        if(tmpRow == null){
            tmpRow = LayoutInflater.from(mContext).inflate(R.layout.reply_list_item, null)
        }

        val row = tmpRow!!

        val data = mList[position]

        val txtSelectedSide = row.findViewById<TextView>(R.id.txtSelectedSide)
        val txtWriterNickname = row.findViewById<TextView>(R.id.txtWriterNickname)
        val txtReplyContent = row.findViewById<TextView>(R.id.txtReplyContent)
        val txtCreatedAt = row.findViewById<TextView>(R.id.txtCreatedAt)

        txtReplyContent.text = data.content
        txtWriterNickname.text = data.writer.nickname
        txtSelectedSide.text = "[${data.selectSide.title}]"

//        임시 - 작성일자만 "2022-03-10" 형태로 표현. => 연/ 월/ 일 데이터로 가공
//        월은 1 작게 나옴. +1로 보정
        txtCreatedAt.text = "${data.createdAt.get(Calendar.YEAR)}-${data.createdAt.get(Calendar.MONTH+1)}-${data.createdAt.get(
            java.util.Calendar.DAY_OF_MONTH)}"



        return row

    }

}