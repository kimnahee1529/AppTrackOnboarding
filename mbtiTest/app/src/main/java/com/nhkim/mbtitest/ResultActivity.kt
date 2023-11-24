package com.nhkim.mbtitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val results = intent.getIntegerArrayListExtra("results") ?: arrayListOf()

        // 선택된 값에 따른 결과 알파벳 할당
        val resultTypes = listOf(
            listOf("E", "I"),
            listOf("N", "S"),
            listOf("T", "F"),
            listOf("J", "P")
        )

        var resultString = ""
        for (i in results.indices) {
            // 선택된 값(1 또는 2)에 따른 알파벳을 결과 문자열에 추가
            resultString += resultTypes[i][results[i] - 1]
        }

        // 결과 TextView 업데이트
        val tvResValue: TextView = findViewById(R.id.tv_resValue)
        tvResValue.text = resultString

        // 알파벳 결과에 따른 이미지 설정
        val ivResImg: ImageView = findViewById(R.id.iv_resImg)
        val imageResource = resources.getIdentifier("ic_${resultString.toLowerCase(Locale.ROOT)}", "drawable", packageName)
        ivResImg.setImageResource(imageResource)

        val btnRetry: Button = findViewById(R.id.btn_res_retry)
        btnRetry.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}