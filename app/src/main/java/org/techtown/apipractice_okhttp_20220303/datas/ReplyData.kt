package org.techtown.apipractice_okhttp_20220303.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ReplyData(
    var id : Int,
    var content: String,
) {

    var writer = UserData()     // 모든 댓글에는 작성자가 있다. null 가능성이 없다.

    var selectSide = SideData()     // 모든 댓글에는 선택한 진영이 있다. null 가능성 X

//    작성 일시를 담아둘 변수
//    일 / 시 데이터를 변경 => 내부의 숫자만 변경. 변수에 새 객체 대입 x => val로 써도 됨
    val createdAt = Calendar.getInstance()

    
//    보조 생성자 추가 연습 : 파라미터 X
    constructor() : this( 0, "내용없음")

//    각 댓글이 자신의 작성일시를, 핸드폰 시간에 맞게 보정 + 가공된 문구로 내보내기
    fun getFormattedCreatedAt() : String{

//    가공 양식 지정
        val sdf = SimpleDateFormat("M월 d일 a h시 m분")

//    시차 보정에 사용할 Calendar 변수 (원래 있는 createdAt 두고 별도로 추가) (내 지역 local 에 맞게)
        val localCal = Calendar.getInstance()
//    작성 일시의 일시값을 그대로 복사 (원래값 : 현재일시)
        localCal.time = this.createdAt.time

        return sdf.format(localCal.time)

    }

    companion object{

        fun getReplyDataFromJson(jsonObj : JSONObject) : ReplyData {

            val replyData = ReplyData()

//            JSON 정보 > 멤버변수 채우기

            replyData.id = jsonObj.getInt("id")
            replyData.content = jsonObj.getString("content")
            replyData.writer = UserData.getUserDataFromJson( jsonObj.getJSONObject("user") )


            replyData.selectSide = SideData.getSideDataFromJson(jsonObj.getJSONObject("selected_side"))

//            Calendar 로 되어있는 작성 일시 시간을, 서버가 알려주는 댓글 작성 일시로 맞춰줘야 함.

//            임시1) 2022-01-12 10:55:35 로 변경 (한번에 모두 변경)
//            replyData.createdAt.set(2022, Calendar.JANUARY, 12, 10, 55, 35)

//            임시2) 연도만 2021년으로 변경 (항목을 찍어서 변경)
//            replyData.createdAt.set( Calendar.YEAR, 2021)

//            실제) 서버가 주는 created_at 에 담긴 String => parse 해서 Calendar 형태로 변경
//            createdAt 변수의 일시 값으로 => parse 결과물 사용

//            서버가 주는 양식을 보고, 그대로 적자.
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

//            created_at 으로 내려오는 문구 (서버가 주는 String 임시 저장)
            val createdAtStr = jsonObj.getString("created_at")

//            createdAtStr 변수를 => Date 로 변경 (parse) => Calendar time 에 대입
            replyData.createdAt.time = sdf.parse( createdAtStr )



                return replyData

        }
    }

}