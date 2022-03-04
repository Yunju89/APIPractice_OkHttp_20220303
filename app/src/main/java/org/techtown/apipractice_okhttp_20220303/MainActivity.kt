package org.techtown.apipractice_okhttp_20220303

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.techtown.apipractice_okhttp_20220303.databinding.ActivityMainBinding
import org.techtown.apipractice_okhttp_20220303.utils.ServerUtil

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding              // xml 에 <layout> 그리고 자동완성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        setupEvents()
        setValues()
    }

    fun setupEvents(){

        binding.bntLogin.setOnClickListener {

//            id/pw 추출
            val inputId = binding.edtId.text.toString()
            val inputPw = binding.edtPassword.text.toString()
//            API 서버에 아이디/비번을 보내서 실제로 회원인지 검사 => 로그인시도




        }


    }

    fun setValues(){

    }
}