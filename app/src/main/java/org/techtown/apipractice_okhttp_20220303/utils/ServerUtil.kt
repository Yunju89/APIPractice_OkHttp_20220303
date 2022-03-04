package org.techtown.apipractice_okhttp_20220303.utils

import android.util.Log
import okhttp3.*
import java.io.IOException

class ServerUtil {

//    서버에 Request 를 날리는 역할.
//    함수를 만들려고 하는데, 어떤 객체가 실행해도 결과가 잘 나오면 그만인 함수.
//    코틀린에서 static 에 해당하는 개념? companion object {  } 에 만들자.


    companion object {

//        서버에 컴퓨터 주소만 변수로 저장 (관리 일원화) => 외부 노출 X
        private  val BASE_URL = "http://54.180.52.26"

//        로그인 기능 호출 함수

        fun postRequestLogin( id : String, pw : String ){

//            Request 제작 -> 실제 호출 -> 서버의 응답을, 화면에 전달

//            제작1) 어느 주소(url) 로 접근할건지? => 서버 주소 + 기능 주소
            val urlString = "${BASE_URL}/user"

//            제작2) 파라미터 담아주기 => 어떤 이름표 / 어느 공간에
            val formData = FormBody.Builder()
                .add("email", id)               // 서버에서 원하는 이름표, 담을데이터
                .add("password", pw)
                .build()

//            제작 3) 모든 Request 정보를 종합한 객체 생성. (어느 주소로 + 어느 메쏘드로(어떤 파라미터를) )
            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .build()

//            종합한 Request 도 실제 호출을 해 줘야 API 호출이 실행 됨. (startActivity 같은 동작 필요)
//            실제 호출 : 앱이 클라이언트로써 동작 > OkHttpClient 클래스

            val client = OkHttpClient()
//            OkHttpClient 객체를 이용 > 서버에 로그인 기능 호출
//            호출 했으면, 서버가 수행한 결과를 받아서 처리.
//            => 서버에 다녀와서 할 일을 등록 : enqueue (Callback)

//            새로호출(request).enqueue(다녀와서할일)(인터페이스로 표현, 인터페이스 종류 : Callback)
            client.newCall(request).enqueue( object : Callback{
                override fun onFailure(call: Call, e: IOException) {

//                    실패 : 서버 연결 자체를 실패 한 상황. 응답이 오지 않음.
//                    ex. 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
//                    ex. 비번 틀려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 > 그 내용만 실패. (물리적 실패 X)

                }

                override fun onResponse(call: Call, response: Response) {

//                    어떤 내용이던, 응답 자체는 잘 돌아온 경우. (그 내용은 성공/실패 일 수 있다.)

//                    응답 : Response 변수 > 응답의 본문 (body) 만 보자.

                    val bodyString = response.body!!.string()        // OkHttp toString() 아님! string() 기능은 1회용, 변수에 담아두고 이용

                    Log.d("서버테스트", bodyString)

                }


            })


        }

    }

}