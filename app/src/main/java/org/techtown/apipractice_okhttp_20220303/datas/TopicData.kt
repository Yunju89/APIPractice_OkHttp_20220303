package org.techtown.apipractice_okhttp_20220303.datas

import java.io.Serializable

class TopicData : Serializable {

    var id = 0  // id는 Int 라고 명시
    var title = ""  // title String
    var imageUrl = ""  // 서버 : img_url, 앱 imageUrl 변수명 다른 경우
    var replyCount = 0



}