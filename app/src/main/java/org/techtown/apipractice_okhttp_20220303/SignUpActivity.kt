package org.techtown.apipractice_okhttp_20220303

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.json.JSONObject
import org.techtown.apipractice_okhttp_20220303.databinding.ActivitySignUpBinding
import org.techtown.apipractice_okhttp_20220303.utils.ServerUtil

class SignUpActivity : BaseActivity() {

    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.btnSignUp.setOnClickListener {

            val inputEmail = binding.edtEmail.text.toString()
            val inputPw = binding.edtPassword.text.toString()
            val inputNickname = binding.edtNickname.text.toString()

            ServerUtil.putRequestSignUp(
                inputEmail,
                inputPw,
                inputNickname,
                object : ServerUtil.JsonResponseHandler{
                    override fun onResponse(jsonObj: JSONObject) {



                    }

                }
            )

        }

    }

    override fun setValues() {

    }
}
