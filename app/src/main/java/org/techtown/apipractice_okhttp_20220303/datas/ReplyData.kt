package org.techtown.apipractice_okhttp_20220303.datas

import org.json.JSONObject

class ReplyData(
    var id : Int,
    var content: String,
) {
    
//    보조 생성자 추가 연습 : 파라미터 X
    constructor() : this( 0, "내용없음")


    companion object{

        fun getReplyDataFromJson(jsonObj : JSONObject) : ReplyData {

            val replyData = ReplyData()

                return replyData

        }
    }

}