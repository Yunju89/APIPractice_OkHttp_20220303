package org.techtown.apipractice_okhttp_20220303

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.techtown.apipractice_okhttp_20220303.databinding.ActivityEditReplyBinding
import org.techtown.apipractice_okhttp_20220303.datas.TopicData

class EditReplyActivity : BaseActivity() {

    lateinit var binding : ActivityEditReplyBinding

//    어느 토론 주제에서 온건지 + 무조건 선택한 진영도 있다.
    lateinit var mTopicData : TopicData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_reply)

        mTopicData = intent.getSerializableExtra("topic")as TopicData

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

    }

    override fun setValues() {

        binding.txtTopicTitle.text = mTopicData.title
        binding.txtSideTitle.text = mTopicData.mySelectedSide!!.title

    }
}