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
import org.techtown.apipractice_okhttp_20220303.datas.TopicData

class TopicAdapter(
    val mContext : Context,
    val resId : Int,
    val mList: List<TopicData>
): ArrayAdapter<TopicData>(mContext, resId, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tmpRow = convertView
        if(tmpRow == null){
            tmpRow = LayoutInflater.from(mContext).inflate(R.layout.topic_list_item, null)
        }

        val row = tmpRow!!

        val data = mList[position]
        val txtTitle = row.findViewById<TextView>(R.id.txtTitle)
        val imgTopicBackground = row.findViewById<ImageView>(R.id.imgTopicBackground)


        txtTitle.text = data.title

//        data > 서버에서 준 주제 데이터
//        imgUrl 변수 파싱 => 이미지의 인터넷 주소.
//        웹에 있는 이미지 > 이미지뷰에 적용 > Glide 활용

        Glide.with(mContext).load(data.imageUrl).into(imgTopicBackground)



        val txtReplyCount = row.findViewById<TextView>(R.id.txtReplyCount)
        txtReplyCount.text = "${data.replyCount}명 참여중!"

        return row

    }

}