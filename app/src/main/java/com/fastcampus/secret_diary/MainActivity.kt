package com.fastcampus.secret_diary

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }

    //비번 바꾸기 누르고 오픈버튼 누르면 오류 걸릴 수 있으니 이런 불린 값으로 조정
    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker1
        numberPicker2
        numberPicker3


        openButton.setOnClickListener{

            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            //패스워드 성공
            if(passwordPreferences.getString("password", "000").equals(passwordFromUser))
            {
                //Todo 다이어리 작성 후 넘겨주어야 함
                //startActivity()
            }

            //패스워드 실패
            else
            {
                showErrorAlertDialog()
            }
        }//오픈 버튼



        changePasswordButton.setOnClickListener {

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (changePasswordMode)
            {
                //번호를 저장하는 기능
                passwordPreferences.edit{
                    //여기에 1. commit기능과 2. apply기능이 있는데
                    //1. commit은 파일을 다 저장할 때까지 UI를 다 멈추고 기다리는 기능
                    //2. apply기능은 저장할 기록을 남겨 놨다 이렇게 말하면서 비동기적으로 저장하는 방식
                    //여기선 제대로 저장이 될 때까지 block할거라 commit 쓸거임
                    putString("password", passwordFromUser)
                    commit()
                    //commit()이거 빼고 위에 edit를 -> edit(true)로 해도 똑같음
                }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)
            }
            else
            {
                //changePasswordMode가 활성화 -> 비밀번호가 맞는지를 체크 -> 비밀번호가 맞는 경우 체인지가 활성화가 되면 안되니까ㅋ

                //현재 패스워드 확인 성공
                if(passwordPreferences.getString("password", "000").equals(passwordFromUser))
                {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()

                    changePasswordButton.setBackgroundColor(Color.RED)
                }

                //패스워드 실패
                else
                {
                    showErrorAlertDialog()
                }

            }

        }

    }



    private fun showErrorAlertDialog() {
        //AlertDialog.Builder로 만드는 경우 .setTitle이런거 붙여서 덕지덕지 생성 가능
        // 그 후 create로 생성 후 .show로 보여줌
        AlertDialog.Builder(this)
            .setTitle("실패")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ ->
            }
            .create()
            .show()
        //1
        // .setPositiveButton("확인") { _, _ ->  이 부분
        //포지티브 버튼 들어가보면 인자가 2개라서
        //dialog랑 which 두 개라서 넣음
        //그랬더니 dialog랑 which가 사용 안된다고 밑줄쳐지고 _로 바꾸는걸 추천해줘서 바꿈
        //2.
        // 확인 버튼을 누르면 다이얼로그가 바로 꺼지기 때문에 딱히 동작은 정의 안 함
    }

}