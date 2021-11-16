package com.fastcampus.secret_diary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper() )
    //getMainLooper를 넣어주면 이 핸들러는 메인 스레드에 연결된 핸들러가 된거임

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail", "메모를 입력해 주세요"))

        //스레드에서 일어나는 기능 구현
        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit{
                putString("detail", diaryEditText.text.toString())
            }//여기선 수시로 백그라운드에서 글을 저장하는 기능이기 때문에 commit을 통해 기다리지 않고
             //저장을 계속 해주세요~하는 비동기로 넘겨버리는 방벙브로 할거임
             //즉 commit을 false로 주고 apply를 사용할 예정
        }
        //밑에 addTextChangedListener로 글자를 적다가 갑자기 꺼져도 그 글자가 기록 안되는게 아니라
        //다시 들어가면 그대로 나오도록 바뀔 때마다 변경되게 하려고 하는데 그러면
        //너무 계속 저장하니까 이 스레드를 통해 몇 초 후 마다 저장하도록 하려고 함

        //내용이 수정이 될 때마다 저장이 되도록 기능 구현
        diaryEditText.addTextChangedListener {
            handler.removeCallbacks(runnable)//0.5초 이전에 아직 실행되지 않고 팬딩되어있는 runnable이 있다면 지워주기 위해 사용하는 함수임
            //이렇게 쓰면 제일 처음에 addTextChangedListener에 들어왔을 때 이 이전에 있는 runnable이 있다면 지우고 없으면 뭐 없으니까 냅둠
            //그러고 밑에 쓴 것 처럼 0.5초마다 runnable을 실행시키는 메시지를 핸들러에 전달
            handler.postDelayed(runnable, 500)//500=0.5초임
            //그러면 0.5초 이내에 리무브콜백이 실행되면 0.5초 전에 실행됬던 runnable은 삭제가 되고
            //계속 그렇게 삭제 삭제 삭제 이렇게 뒤로 팬딩이 되다가
            //마지막으로 0.5초 안에 새로운 텍스트 체인지가 일어나지 않으면
            //그제서야 이 runnable이 실행이 되서 editText에 저장이 되는 기능이 수행이 됨
        }

    }
}