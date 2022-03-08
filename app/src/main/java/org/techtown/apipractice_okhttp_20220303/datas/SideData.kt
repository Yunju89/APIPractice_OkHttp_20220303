package org.techtown.apipractice_okhttp_20220303.datas

import org.json.JSONObject

// 토론의 선택 진영(이름, id값 등등)을 표현


class SideData {
    var id = 0
    var title = ""
    var voteCount = 0


    constructor(){

        fun getSideDataFromJson(jsonObject: JSONObject) : SideData {

            val sideData = SideData()

            sideData.id = jsonObject.getInt("id")
            sideData.title = jsonObject.getString("title")
            sideData.voteCount = jsonObject.getInt("vote_count")

            return sideData
        }
    }


}