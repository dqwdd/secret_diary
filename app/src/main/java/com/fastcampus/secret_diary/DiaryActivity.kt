package com.fastcampus.secret_diary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class DiaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)
        diaryEditText.setText(detailPreferences.getString("detail", "메모를 입력해 주세요"))

        //내용이 수정이 될 때마다 저장이 되도록 기능 구현

    }
}