package org.techtown.apipractice_okhttp_20220303.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class ServerUtil {

//    서버 유틸로 돌아온 응답을 => 액티비티에서 처리하도록 일처리 넘기기.
//    나에게 생긴 일 > 다른 클래스에게 처리 요청 : interFace 활용

    interface JsonResponseHandler {                    // 인터페이스는 전체가 abstract 라서 abstract 함수 안써도 됨
        fun onResponse(jsonObj: JSONObject)          //jsonObj 받아옴
    }

//    서버에 Request 를 날리는 역할.
//    함수를 만들려고 하는데, 어떤 객체가 실행해도 결과가 잘 나오면 그만인 함수.
//    코틀린에서 static 에 해당하는 개념? companion object {  } 에 만들자.


    companion object {

        //        서버에 컴퓨터 주소만 변수로 저장 (관리 일원화) => 외부 노출 X
        private val BASE_URL = "http://54.180.52.26"

//        로그인 기능 호출 함수
//        handler : 이 함수를 쓰는 화면에서, JSON 분석을 어떻게 / UI 에서 어떻게 활용할 지 방안.(인터페이스)
//          -처리 방안을 임시로 비워두려면, null 대입 허용 (handler 변수에 ? 직접 붙임)
//          -서버 통신 응답이 늦을 수 있기 때문에, 사용자가 끌 수도 있고, 액티비티가 종료될 수 있기때문에 null point Exception 막기 위해서

        fun postRequestLogin(id: String, pw: String, handler: JsonResponseHandler?) {

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

            Log.d("서버", "${request.url}")

//            종합한 Request 도 실제 호출을 해 줘야 API 호출이 실행 됨. (startActivity 같은 동작 필요)
//            실제 호출 : 앱이 클라이언트로써 동작 > OkHttpClient 클래스

            val client = OkHttpClient()
//            OkHttpClient 객체를 이용 > 서버에 로그인 기능 호출
//            호출 했으면, 서버가 수행한 결과를 받아서 처리.
//            => 서버에 다녀와서 할 일을 등록 : enqueue (Callback)

//            새로호출(request).enqueue(다녀와서할일)(인터페이스로 표현, 인터페이스 종류 : Callback)
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

//                    실패 : 서버 연결 자체를 실패 한 상황. 응답이 오지 않음.
//                    ex. 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
//                    ex. 비번 틀려서 로그인 실패 : 서버 연결 성공, 응답도 돌아왔는데 > 그 내용만 실패. (물리적 실패 X)

                }

                override fun onResponse(call: Call, response: Response) {

//                    어떤 내용이던, 응답 자체는 잘 돌아온 경우. (그 내용은 성공/실패 일 수 있다.)

//                    응답 : Response 변수 > 응답의 본문 (body) 만 보자.

                    val bodyString =
                        response.body!!.string()        // OkHttp toString() 아님! string() 기능은 1회용, 변수에 담아두고 이용

//                    응답의 본문을 String 으로 변환하면, JSON Encoding 적용 상태. (한글 깨짐)
//                    JASONObject 객체로 응답 본문 String 변환해주면, 한글이 복구 됨.
//                    => UI 에서도 JASONObject 이용해서 데이터 추출 / 실제 활용

                    val jsonObj = JSONObject(bodyString)
                    Log.d("서버응답", jsonObj.toString())

//                    실제 : handler 변수에, jsonObj 가지고 화면에서 어떻게 처리할지 계획이 들어와있다.
//                    (계획이 되어 있을때만, 널이 아닐때만)해당 계획을 실행하자.
                    handler?.onResponse(jsonObj)

                }

            })

        }

        fun putRequestSignUp(email: String, pw: String, nickname: String, handler: JsonResponseHandler?) {

            val urlString = "${BASE_URL}/user"

            val formData = FormBody.Builder()
                .add("email", email)
                .add("password", pw)
                .add("nick_name", nickname)
                .build()

            val request = Request.Builder()
                .url(urlString)
                .put(formData)
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val bodyString = response.body!!.string()
                    val jsonObj = JSONObject(bodyString)
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)

                }

            })

        }

//        이메일 or 닉네임 중복 검사 함수
        fun getRequestDuplicatedCheck(type: String, inputValue: String, handler: JsonResponseHandler?) {
//            1) 어느 주소로 가야하는가? + 어떤 파라미터를 첨부 하는가? 주소에 같이 포함
//             => 라이브러리의 도움 받자. httpUrl 클래스 (OkHttp 소속)

            val urlBuilder = "${BASE_URL}/user_check".toHttpUrlOrNull()!!.newBuilder()
                .addEncodedQueryParameter("type", type)
                .addEncodedQueryParameter("value", inputValue)
                .build()

            val urlString = urlBuilder.toString()

//            2) 요청 정보 정리 > Request 생성.

            val request = Request.Builder()
                .url(urlString)
                .get()              // rulBuilder 만들 때 정보가 이미 들어 감.
                .build()

//            3) Request 완성 > 서버에 호출, 응답 화면에 넘기자
            val client = OkHttpClient()

            client.newCall(request).enqueue( object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()
                    val jsonObj = JSONObject (bodyString)         // JSONObject 로 만들어야 사용 가능, string 넣어 다른 클래스 만드는것 -> json 파싱
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)                // 핸들러가 할일이 있는지? 있다면 onResponse 실행 요청

                }
            })
        }

//       연습 : 내 정보 불러오기 (/user_info - GET)
//        토큰은, ContextUtil 클래스에서 getToken 함수로 꺼내올 수  있다.
//        토큰 값 자체는 파라미터로 받아올 필요 없다 => ContextUtil 불러다 사용하자.
//        메모장에 접근할 수 있게 , Context 변수 하나를 미리 받아두자.

        fun getRequestMyInfo( context: Context, handler: JsonResponseHandler? ) {       // user_info

            val urlBuilder = "${BASE_URL}/user_info".toHttpUrlOrNull()!!.newBuilder()
                .build()  // 쿼리 파라미터를 담을게 없다. 바로 build 로 마무리.

            val urlString = urlBuilder.toString()

            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token", ContextUtil.getToken(context))  // ContextUtil 통해 저장된 토큰 받아서 첨부.
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)
                }
            })
        }

        fun getRequestMainInfo( context: Context, handler: JsonResponseHandler? ) {         //v2/main_info

            val urlBuilder = "${BASE_URL}/v2/main_info".toHttpUrlOrNull()!!.newBuilder()
                .build()  // 쿼리 파라미터를 담을게 없다. 바로 build 로 마무리.

            val urlString = urlBuilder.toString()

            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token", ContextUtil.getToken(context))  // ContextUtil 통해 저장된 토큰 받아서 첨부.
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)
                }
            })
        }

        fun getRequestTopicDetail( context: Context, topicId : Int, handler: JsonResponseHandler? ) {         //v2/main_info

            val urlBuilder = "${BASE_URL}/topic/${topicId}".toHttpUrlOrNull()!!.newBuilder() // 주소 .addPathSegment(topicId.toString()) 으로도가능
                .build()  // 쿼리 파라미터를 담을게 없다. 바로 build 로 마무리.

            val urlString = urlBuilder.toString()

            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token", ContextUtil.getToken(context))  // ContextUtil 통해 저장된 토큰 받아서 첨부.
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    Log.d("서버응답", jsonObj.toString())

                    handler?.onResponse(jsonObj)
                }
            })
        }

    }

}