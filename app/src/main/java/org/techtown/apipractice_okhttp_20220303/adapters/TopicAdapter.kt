package org.techtown.apipractice_okhttp_20220303.adapters

import android.content.Context
import android.widget.ArrayAdapter
import org.techtown.apipractice_okhttp_20220303.datas.TopicData

class TopicAdapter(
    val mContext : Context,
    val resId : Int,
    val mList: List<TopicData>
): ArrayAdapter<TopicData>(mContext, resId, mList) {


}