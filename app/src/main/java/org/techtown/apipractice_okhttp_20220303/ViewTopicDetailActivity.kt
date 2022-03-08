package org.techtown.apipractice_okhttp_20220303

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import org.json.JSONObject
import org.techtown.apipractice_okhttp_20220303.databinding.ActivityViewTopicDetailBinding
import org.techtown.apipractice_okhttp_20220303.datas.TopicData
import org.techtown.apipractice_okhttp_20220303.utils.ServerUtil

class ViewTopicDetailActivity : BaseActivity() {
    lateinit var binding: ActivityViewTopicDetailBinding

    //    보여주게 될 토론 주제 데이터 > 이벤트처리, 데이터 표현 등 여러 함수에서 사용
//    화면에 들어와서 > Intent 통해 대입 (멤버변수에 대입을 늦게하고 싶다> 일반 멤버변수는 초기값 세팅을 꼭 해야하기때문에)
    lateinit var mTopicData: TopicData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_topic_detail)

        mTopicData = intent.getSerializableExtra("topic") as TopicData
        Log.d("토픽데이터", mTopicData.id.toString())

        setupEvents()
        setValues()


    }

    override fun setupEvents() {

//        btnVote1 클릭 => 첫 진영의 id 값을 찾아서, 거기에 투표
//        서버에 전달 => API 활용

        binding.btnVote1.setOnClickListener {

//            서버 투표 API 호출

//            투표 현황 새로고침(응답)



        }

    }

    override fun setValues() {
        getTopicDetailFromServer()
        setTopicDataToUi()

    }

    fun setTopicDataToUi() {

//        토론 주제에 대한 데이터들을, UI에 반영하는 함수.
//        화면 초기 진입 실행 + 서버에서 다시 받아왔을때도 실행

        binding.txtTitle.text = mTopicData.title
        Glide.with(mContext).load(mTopicData.imageUrl).into(binding.imgTopicBackground)

//        1번진영 제목, 2번진영 제목
        binding.txtSide1.text = mTopicData.sideList[0].title
        binding.txtSide2.text = mTopicData.sideList[1].title

//        1번진영 득표수, 2번진영 득표수
        binding.txtVoteCount1.text = "${mTopicData.sideList[0].voteCount}표"
        binding.txtVoteCount2.text = "${mTopicData.sideList[1].voteCount}표"



    }


    fun getTopicDetailFromServer() {
        ServerUtil.getRequestTopicDetail(
            mContext,
            mTopicData.id,
            object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

                    val dataObj = jsonObj.getJSONObject("data")
                    val topicObj = dataObj.getJSONObject("topic")

//                    토론 정보 JSONObject (topicObj) => TopicData() 형태로 변환 (여러 화면에서 진행. 함수로 만들어두자)
                    val topicData = TopicData.getTopicDataFromJson(topicObj)

                    mTopicData = topicData

                    runOnUiThread {
                        setTopicDataToUi()
                    }


//                    변환된 객체를 mTopicData 로 다시 대입 => UI 반영도 다시 실행


                }

            })
    }

}






