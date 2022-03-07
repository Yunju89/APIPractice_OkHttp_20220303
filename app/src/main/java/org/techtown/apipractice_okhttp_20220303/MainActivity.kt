package org.techtown.apipractice_okhttp_20220303

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import org.json.JSONObject
import org.techtown.apipractice_okhttp_20220303.databinding.ActivityMainBinding
import org.techtown.apipractice_okhttp_20220303.datas.TopicData
import org.techtown.apipractice_okhttp_20220303.utils.ServerUtil

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding

//    실제로 서버가 내려주는 주제 목록을 담을 그릇
    val mTopicList = ArrayList<TopicData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

    }

    override fun setValues() {

//        메인 화면 정보 가져오기 => API 호출 / 응답 처리
        getTopicListFromServer()


    }

    fun getTopicListFromServer(){

        ServerUtil.getRequestMainInfo(mContext,object : ServerUtil.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {
//                서버가 주는 토론 주제 목록 파싱 => mTopicList 에 추가

                val dataObj = jsonObj.getJSONObject("data")

                val topicsArr = dataObj.getJSONArray("topics")

//                topicsArr 내부 하나씩 추출 (JSONObject { } => TopicData() 로 변환

//                JSONArray for-each 문법 지원 X. (차후 : ArrayList  for-each 활용 예정)
//                JAVA : for(int i=0, i<배열.length, i++)와 완전히 동일한 문법

                for(i in 0 until topicsArr.length()){
//                    [  ] => {},{},{},{}...순서에 맞는 {} 변수에 담기
//                    JSON 파싱의 {} => (JSONArray 에서) JSONObject 추출

                    val topicObj = topicsArr.getJSONObject( i )

                    Log.d("받아낸 주제", topicObj.toString())

                }
            }

        })

    }


}