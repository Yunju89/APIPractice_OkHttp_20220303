package org.techtown.apipractice_okhttp_20220303

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import org.techtown.apipractice_okhttp_20220303.utils.ContextUtil
import org.techtown.apipractice_okhttp_20220303.utils.ServerUtil

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupEvents()
        setValues()

    }


    override fun setupEvents() {


    }

    override fun setValues() {

//        2.5초가 지나기 전에, 미리 사용자 정보 조회 시도 (토큰 유효성 검증) (서버 응답이 2.5초보다 더 느릴 수 있기 때문에)

        var isTokenOk = false       // 검사를 통과하면 true 변경

        ServerUtil.getRequestMyInfo(mContext, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {
//                    내 정보를 잘 가져왔다면? => code 값 200으로 내려준다.
//                    200이 아니라면? 무슨이유던, 사용자 정보 로드 실패 (토큰 유효 x)

                val code = jsonObj.getInt("code")
                isTokenOk = (code == 200)
            }
        })

//        2.5초 지나면 -> 자동로그인을 해도 되는지? -> 상황에 맞는 화면으로 이동

        val myHandler = Handler(Looper.getMainLooper())
        myHandler.postDelayed({

//            자동로그인을 해도 되는가?
//            1) 사용자가 자동로그인 의사를 OK 했는지?
            val userAutoLogin = ContextUtil.getAutoLogin(mContext)

//            2) 로그인시에 받아낸 토큰값이 지금도 유효한지?


//            2-1) 저장된 토큰이 있는지? (임시방편)
//            2-2) 그 토큰이 사용자 정보를 불러내는지? (실제)
//              => 2.5초 전에 미리 물어본 상태. isTokenOk에 결과가 들어있다.

//            Intent 화면 이동 => 상황에 따라 다른 Intent 마든다.
            val myIntent : Intent

            if(userAutoLogin && isTokenOk){
//                둘다 OK 라면 바로 메인화면으로
                myIntent = Intent(mContext, MainActivity::class.java)
          }
            else{
//                아니라면 로그인 화면으로로
                myIntent = Intent(mContext, LoginActivity::class.java)
           }

            startActivity(myIntent)
            finish()

        }, 2500)

    }


}