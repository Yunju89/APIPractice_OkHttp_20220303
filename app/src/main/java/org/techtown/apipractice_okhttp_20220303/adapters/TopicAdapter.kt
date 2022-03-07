package org.techtown.apipractice_okhttp_20220303.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

        return row

    }

}