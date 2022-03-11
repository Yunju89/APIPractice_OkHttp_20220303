package org.techtown.apipractice_okhttp_20220303

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import org.json.JSONObject
import org.techtown.apipractice_okhttp_20220303.adapters.ReplyAdapter
import org.techtown.apipractice_okhttp_20220303.databinding.ActivityViewTopicDetailBinding
import org.techtown.apipractice_okhttp_20220303.datas.ReplyData
import org.techtown.apipractice_okhttp_20220303.datas.TopicData
import org.techtown.apipractice_okhttp_20220303.utils.ServerUtil

class ViewTopicDetailActivity : BaseActivity() {
    lateinit var binding: ActivityViewTopicDetailBinding

    //    보여주게 될 토론 주제 데이터 > 이벤트처리, 데이터 표현 등 여러 함수에서 사용
//    화면에 들어와서 > Intent 통해 대입 (멤버변수에 대입을 늦게하고 싶다> 일반 멤버변수는 초기값 세팅을 꼭 해야하기때문에)
    lateinit var mTopicData: TopicData

    var mReplyList = ArrayList<ReplyData>()

    lateinit var mAdapter : ReplyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_topic_detail)

        mTopicData = intent.getSerializableExtra("topic") as TopicData

        setupEvents()
        setValues()


    }

    override fun setupEvents() {

//        btnVote1 클릭 => 첫 진영의 id 값을 찾아서, 거기에 투표
//        서버에 전달 => API 활용

        binding.btnVote1.setOnClickListener {

//            서버 투표 API 호출
            ServerUtil.postRequestVote(mContext, mTopicData.sideList[0].id, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {

//                    토스트로 서버가 알려준 현재 상황 (신규투표 or 재투표 or 취소 등)
                    val message = jsonObj.getString("message")

                    runOnUiThread {
                        Toast.makeText(mContext, message , Toast.LENGTH_SHORT).show()
                    }

//                    변경된 득표 현황을 다시 불러오자.
                    getTopicDetailFromServer()


                }

            })

//            투표 현황 새로고침(응답)

        }

        binding.btnVote2.setOnClickListener {

            ServerUtil.postRequestVote(mContext, mTopicData.sideList[1].id, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {

                    val message = jsonObj.getString("message")

                    runOnUiThread {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }

                    getTopicDetailFromServer()

                }

            })

        }

        binding.btnPostReply.setOnClickListener {

//            투표를 하지 않은 상태라면, 댓글 작성도 불가.

            if(mTopicData.mySelectedSide == null){

                Toast.makeText(mContext, "의견을 개진할 진영을 선택하셔야 합니다.", Toast.LENGTH_SHORT).show()

//                클릭 이벤트 자체를 강제 종료. (Intent 실행을 막자)
            }

            val myIntent = Intent(mContext, EditReplyActivity::class.java)

            myIntent.putExtra("topic", mTopicData)

            startActivity(myIntent)
        }


    }

    override fun setValues() {

        mAdapter = ReplyAdapter(mContext, R.layout.reply_list_item, mReplyList)
        binding.replyListView.adapter = mAdapter

        setTopicDataToUi()

//        어차피 onResume 에서 서버 연결 예정
//        getTopicDetailFromServer()

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

//        내가 선택한 진영이 있을 때, (투표 해놨을 때)
//        이미 투표한 진영은 문구를 변경하자 (enabled = "투표취소")

        if(mTopicData.mySelectedSide != null){

//            첫번째 진영을 투표했는지?
//            두번째 진영을 투표했는지?

            if(mTopicData.mySelectedSide!!.id == mTopicData.sideList[0].id){
//                첫 진영에 투표한 경우.
                binding.btnVote1.text = "투표 취소"
                binding.btnVote2.text = "다시 투표"
            }
            else {
//                두번째 진영에 투표
                binding.btnVote1.text = "다시 투표"
                binding.btnVote2.text = "투표 취소"
            }
        }
        else{
//            아무데도 투표하지 않은 경우.
            binding.btnVote1.text = "투표 하기"
            binding.btnVote2.text = "투표 하기"
        }

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

//                    변환된 객체를 mTopicData 로 다시 대입 => UI 반영도 다시 실행
                    mTopicData = topicData

                    runOnUiThread {
                        setTopicDataToUi()
                    }

//                    3/11 추가 mReplyList 에 댓글 목록이 추가 된다
//                    => 기존에 다른 댓글들이 들어있다면, 그 뒤에 이어서 추가 된다.
//                    => 기존 댓글 목록을 전부 삭제하고 나서, 추가하자.
                    mReplyList.clear()

//                    topicObj 내부에는 replies 라는 댓글 목록 JSONArray 도 들어있다.
//                    mReplyList 에 넣어주자.

                    val repliesArr = topicObj.getJSONArray("replies")

                    for (i in 0 until repliesArr.length()){
                        val replyObj = repliesArr.getJSONObject(i)

                        mReplyList.add( ReplyData.getReplyDataFromJson(replyObj) )

                    }

//                    서버의 동작이므로, 어댑터 세팅보다 늦게 끝날 수 있다. (notifyDataSetChanged)
                    runOnUiThread {
                        mAdapter.notifyDataSetChanged()
                    }

                }

            })
    }

    override fun onResume() {
        super.onResume()

        getTopicDetailFromServer()

    }

}






