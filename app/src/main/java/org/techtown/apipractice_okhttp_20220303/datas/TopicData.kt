package org.techtown.apipractice_okhttp_20220303.datas

import android.view.WindowInsets
import org.json.JSONObject
import java.io.Serializable

class TopicData : Serializable {

    var id = 0  // id는 Int 라고 명시
    var title = ""  // title String
    var imageUrl = ""  // 서버 : img_url, 앱 imageUrl 변수명 다른 경우
    var replyCount = 0

//    하나의 토론 주제 : 여러개의 (목록) 선택 진영
    val sideList = ArrayList<SideData>()

//    내가 투표해둔 진영
    var mySelectedSide : SideData? = null   //null 로 생성.



    companion object{

//    주제 정보를 담고있는 JSONObject 가 들어오면 > TopicData 형태로 변환해주는 함수 => static 메쏘드

        fun getTopicDataFromJson(jsonObj : JSONObject) :TopicData{

//            기본 내용의 TopicData 생성
            val topicData = TopicData()

//            jsonObj 에서 데이터 추출 > 멤버변수 대입

            topicData.id = jsonObj.getInt("id")
            topicData.title = jsonObj.getString("title")
            topicData.imageUrl = jsonObj.getString("img_url")
            topicData.replyCount = jsonObj.getInt("reply_count")


//            sides 라는 JSONArray 가 들어있음.
//             => topicData  하위 정보로, 선택 진영 목록으로 저장.
//             => SJONArray > ArrayList

            val sidesArr = jsonObj.getJSONArray("sides")

            for(i in 0 until sidesArr.length()){

//                선택 진영 정보를 들고있는 JSONObject 추출
                val sideObj = sidesArr.getJSONObject(i)

//                sideObj 도, sideData 로 (선택진영) 변환
                val sideData = SideData.getSideDataFromJson(sideObj)

//                topicData 변수의 하위목록으로 등록
                topicData.sideList.add(sideData)

            }
//            투표해둔 진영이 있다면? 선택 진영 데이터도 파싱
//            진영이 없다면? => mySide 항목은 null 일수도 있다.

            if( !jsonObj.isNull("my_side") ){
//                null 아닐때만 파싱

                val mySideObj = jsonObj.getJSONObject("my_side")

//                선택 진영 jSON => mySelectedSide 변수 (sideData)
                topicData.mySelectedSide = SideData.getSideDataFromJson(mySideObj)

            }



//            완성된 TopicData 리턴
            return topicData

        }

    }




}