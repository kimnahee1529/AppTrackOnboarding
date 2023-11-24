package com.nhkim.mbtitest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

class QuestionFragment : Fragment() {
    private var questionType: Int = 0

    private val questionTitles = listOf(
        R.string.question1_title,
        R.string.question2_title,
        R.string.question3_title,
        R.string.question4_title
    )
    private val questionTexts = listOf(
        listOf(R.string.question1_1, R.string.question1_2, R.string.question1_3),
        listOf(R.string.question2_1, R.string.question2_2, R.string.question2_3),
        listOf(R.string.question3_1, R.string.question3_2, R.string.question3_3),
        listOf(R.string.question4_1, R.string.question4_2, R.string.question4_3),
    )
    private val questionAnswers = listOf(
        listOf(
            listOf(R.string.question1_1_answer1, R.string.question1_1_answer2),
            listOf(R.string.question1_2_answer1, R.string.question1_2_answer2),
            listOf(R.string.question1_3_answer1, R.string.question1_3_answer2)
        ),
        listOf(
            listOf(R.string.question2_1_answer1, R.string.question2_1_answer2),
            listOf(R.string.question2_2_answer1, R.string.question2_2_answer2),
            listOf(R.string.question2_3_answer1, R.string.question2_3_answer2)
        ),
        listOf(
            listOf(R.string.question3_1_answer1, R.string.question3_1_answer2),
            listOf(R.string.question3_2_answer1, R.string.question3_2_answer2),
            listOf(R.string.question3_3_answer1, R.string.question3_3_answer2)
        ),
        listOf(
            listOf(R.string.question4_1_answer1, R.string.question4_1_answer2),
            listOf(R.string.question4_2_answer1, R.string.question4_2_answer2),
            listOf(R.string.question4_3_answer1, R.string.question4_3_answer2)
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionType = it.getInt(ARG_QUESTION_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        val title: TextView = view.findViewById(R.id.tv_question_title)
        title.text = getString(questionTitles[questionType])

        val questionTextViews = listOf<TextView>(
            view.findViewById(R.id.tv_question_1),
            view.findViewById(R.id.tv_question_2),
            view.findViewById(R.id.tv_question_3)
        )

        val answerRadioGroups = listOf<RadioGroup>(
            view.findViewById(R.id.rg_answer_1),
            view.findViewById(R.id.rg_answer_2),
            view.findViewById(R.id.rg_answer_3)
        )

        for (i in questionTextViews.indices) {
            questionTextViews[i].text = getString(questionTexts[questionType][i])

            val radioButton1 = answerRadioGroups[i].getChildAt(0) as RadioButton
            val radioButton2 = answerRadioGroups[i].getChildAt(1) as RadioButton
            radioButton1.text = getString(questionAnswers[questionType][i][0])
            radioButton2.text = getString(questionAnswers[questionType][i][1])
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val answerRadioGroups = listOf<RadioGroup>(
            view.findViewById(R.id.rg_answer_1),
            view.findViewById(R.id.rg_answer_2),
            view.findViewById(R.id.rg_answer_3)
        )

        val btnNext: Button = view.findViewById(R.id.btn_next)
        btnNext.setOnClickListener {

            //모든 질문에 대한 응답이 완료되었는지 확인
            val isAllAnswered = answerRadioGroups.all { it.checkedRadioButtonId != -1 }

            if (isAllAnswered) {
                val responses = answerRadioGroups.map { radioGroup ->
                    val firstRadioButton = radioGroup.getChildAt(0) as RadioButton
                    if (firstRadioButton.isChecked) 1 else 2
                }

                (activity as? TestActivity)?.questionnaireResults?.addResponses(responses)
                (activity as? TestActivity)?.moveToNextQuestion()
            } else {
                Toast.makeText(context, "모든 질문에 답해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        if(questionType==3){
            btnNext.setText("결과 확인")
        }
    }


    companion object {
        private const val ARG_QUESTION_TYPE = "questionType"

        fun newInstance(questionType: Int): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()
            args.putInt(ARG_QUESTION_TYPE, questionType)
            fragment.arguments = args
            return fragment
        }
    }
}