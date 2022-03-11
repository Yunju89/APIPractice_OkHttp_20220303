package org.techtown.apipractice_okhttp_20220303.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.json.JSONObject
import org.techtown.apipractice_okhttp_20220303.R
import org.techtown.apipractice_okhttp_20220303.ViewTopicDetailActivity
import org.techtown.apipractice_okhttp_20220303.datas.ReplyData
import org.techtown.apipractice_okhttp_20220303.utils.ServerUtil

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

        val txtReReplyCount = row.findViewById<TextView>(R.id.txtReReplyCount)
        val txtLikeCount = row.findViewById<TextView>(R.id.txtLikeCount)
        val txtHateCount = row.findViewById<TextView>(R.id.txtHateCount)

        txtReplyContent.text = data.content
        txtWriterNickname.text = data.writer.nickname
        txtSelectedSide.text = "[${data.selectSide.title}]"




//        임시 - 작성일자만 "2022-03-10" 형태로 표현. => 연/ 월/ 일 데이터로 가공
//        월은 1 작게 나옴. +1로 보정
//        txtCreatedAt.text = "${data.createdAt.get(Calendar.YEAR)}-${data.createdAt.get(Calendar.MONTH+1)}-${data.createdAt.get(data.createdAt.get(Calendar.DAY_OF_MONTH))}"

//        임시 2) - "2022-03-10" 형태로 표현 => SimpleDateFormat 활용

//        연습.
//        양식1) 2022년 3월 5일
//        양식2) 220305
//        양식3) 3월 5일 오전 2시 5분
//        양식4) 21년 3/5 (토) - 18:05
//        val sdf = SimpleDateFormat("yy년 M/d (E) - hh:mm")

//        sdf.format( Date 객체 ) => 지정해둔 양식의 String 으로 가공
//        createdAt : Calendar / format 파라미터 : Date => Calendar 내용물인 time 변수가 Date
        txtCreatedAt.text = data.getFormattedCreatedAt()

        txtReReplyCount.text = "답글${data.reReplyCount}"
        txtLikeCount.text = "좋아요${data.likeCount}"
        txtHateCount.text = "싫어요${data.hateCount}"

        txtLikeCount.setOnClickListener {

//            서버에 이 댓글에 좋아요 알림.
            ServerUtil.postRequestReplyLikeorHate(
                mContext, data.id, true, object : ServerUtil.JsonResponseHandler{
                    override fun onResponse(jsonObj: JSONObject) {

//                        무조건 댓글 목록 새로고침
//                        Adapter 코딩 => 액티비티의 기능 실행

//                        어댑터 객체화시, mContext 변수에 어느 화면에서 사용하는지 대입.
//                        mContext : Context 타입,   대입 객체 : ViewTopic 액티비티 객체 => 다형성

//                        부모 형태의 변수에 담긴 자식 객체는, 캐스팅을 통해 원상복구 가능.
//                        자식에서 만든 별두의 함수들을 다시 사용 가능.

                        (mContext as ViewTopicDetailActivity).getTopicDetailFromServer()

                    }
                }
            )
        }

//        [과제] 싫어요가 눌려도 처리 => 싫어요 API 호출 (기존 함수 활용) + 토론 상세화면 댓글 목록 새로고침

//        좋아요가 눌렸는지 아닌지 글씨 색상 변경 / 배경 drawable 설정

        if(data.isMyLike){
            txtLikeCount.setTextColor(ContextCompat.getColor(mContext, R.color.naver_red))
            txtLikeCount.setBackgroundResource(R.drawable.naver_red_border_box)
        }
        else{
            txtLikeCount.setTextColor(ContextCompat.getColor(mContext, R.color.deep_dark_gray))
            txtLikeCount.setBackgroundResource(R.drawable.dark_gray_border_box)
        }



        return row

    }

}