package org.techtown.apipractice_okhttp_20220303.utils

import android.content.Context

class ContextUtil {

    companion object {

//        메모장 파일 이름처럼, SharedPreferences 의 이름 설정.
        private val prefName = "OkHttpPracticePref"

//        저장할 데이터의 항목명도 변수로 만들어 두자.

        private val TOKEN = "TOKEN"

        private val AUTO_LOGIN = "AUTO_LOGIN"

//        데이터 저장 함수(setter) / 조회 함수(getter) 별개로 작성.
//        TOKEN 항목에 저장 => token 항목 조회? 데이터 인식 X, 대소문자 동일해야 함.
//        오타를 줄이고 코딩 편하게 하는 조치.

        fun setToken( context: Context, token : String){

//            메모장 파일을 열자. (공식처럼 사용), context Activity 상위버젼 -> 그 내부 시스템을 말하는느낌??
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
//            입력 들어온 token 내용 (TOKEN 항목에) 저장.
            pref.edit().putString(TOKEN, token).apply()
        }

        fun getToken (context: Context) : String {

            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(TOKEN, "")!!
        }


        fun setAutoLogin(context: Context, isAuto : Boolean){
            val prefName = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            prefName.edit().putBoolean(AUTO_LOGIN, isAuto).apply()
        }

        fun getAutoLogin(context: Context) : Boolean {
            val prefName = context.getSharedPreferences(Companion.prefName, Context.MODE_PRIVATE)
            return prefName.getBoolean(AUTO_LOGIN, true)
        }


    }
}