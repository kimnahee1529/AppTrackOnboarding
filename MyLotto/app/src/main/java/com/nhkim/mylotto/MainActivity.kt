package com.nhkim.mylotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    //사용자에게 액션을 받을 버튼 4가지 선언
    private val clearButton by lazy {findViewById<Button>(R.id.btn_clear)}
    private val addButton by lazy {findViewById<Button>(R.id.btn_add)}
    private val runButton by lazy {findViewById<Button>(R.id.btn_run)}
    private val numPick by lazy {findViewById<NumberPicker>(R.id.np_num)}

    //6개의 공을 리스트에 넣어놓고 꺼내쓸 수 있게 만드는 작업
    private val numTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6)
        )
    }

    //현재 run 상태인지 확인하는 변수
    // true일 때는 번호가 꽉 차있는 상태. 번호를 추가하지 못한다. 초기화 버튼을 누르면 false로 변경
    private var didRun = false

    //사용자가 선택한 숫자를 담아두는 변수
    private val pickNumSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numPick.minValue = 1
        numPick.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    //1. 번호가 꽉 차있을 때 추가하지 못하게 해야 함
    //2. 5개까지만 숫자가 추가되게 해야 함
    //3. 같은 숫자가 들어오지 못하게 해야 함
    private fun initAddButton() {
        addButton.setOnClickListener {
            when {
                didRun -> showToast("초기화 후에 시도해주세요.") // 1번의 상황
                pickNumSet.size >= 5 -> showToast("숫자는 최대 5개까지 선택할 수 있습니다.") // 2번의 상황
                pickNumSet.contains(numPick.value) -> showToast("이미 선택한 숫자입니다.") // 3번의 상황
                else -> { //위의 상황이 아니면 번호를 추가할 수 있음
                    val textView = numTextViewList[pickNumSet.size]
                    textView.isVisible = true
                    textView.text = numPick.value.toString()

                    setNumBack(numPick.value, textView)
                    pickNumSet.add(numPick.value)
                }
            }
        }
    }

    // 숫자별로 색 구분해주는 함수 ex) 1~10:노란색 / 11~20:파 / 21~30:빨 / 31~40:회 / 41~45:녹
    private fun setNumBack(number: Int, textView: TextView){
        val background = when(number){
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else-> R.drawable.circle_green
        }

        //TODO : ContextCompat 아직 이해 안 됨
        textView.background = ContextCompat.getDrawable(this, background)
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumSet.clear()
            numTextViewList.forEach { it.isVisible = false }
            didRun = false
            numPick.value = 1
        }
    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandom()
            didRun = true

            //공 그림을 보여줌
            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)
            }
        }
    }

    //랜덤 숫자를 추가한 번호와 합쳐 총 6개가 되도록 만드는 함수
    private fun getRandom(): List<Int> {
        val numbers = (1..45).filter { it !in pickNumSet }
        return (pickNumSet + numbers.shuffled().take(6 - pickNumSet.size)).sorted()
    }


    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



}