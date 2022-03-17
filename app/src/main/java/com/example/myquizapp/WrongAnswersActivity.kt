package com.example.myquizapp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myquizapp.databinding.ActivityWrongAnswersBinding

// WrongAnswersActivity to display the correct answer(s) for questions the user got wrong
class WrongAnswersActivity : AppCompatActivity() {
    private val questionsList = Constants.getQuestions()
    private var currentPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // implement data binding
        val binding = DataBindingUtil.setContentView<ActivityWrongAnswersBinding>(this, R.layout.activity_wrong_answers)
        val wrongAnswersIdList = intent.getIntegerArrayListExtra(Constants.WRONG_ANSWERS)

        // call getQuestion() to retrieve question(s) the user got wrong
        getQuestion(wrongAnswersIdList!![currentPosition], binding, wrongAnswersIdList)

        // onClickListener for btnNext, which ensures currentPosition is less than the size of the wrongAnswersIdList
        // call getQuestion() method
        binding.btnNext.setOnClickListener {
            resetQuestionBorder(binding)
            if (currentPosition < wrongAnswersIdList.size) {
                getQuestion(wrongAnswersIdList[currentPosition], binding, wrongAnswersIdList)
            }

            // if currentPosition is at the last index for list of wrong questions, change button to display "finished"
            // and close the activity
            if (currentPosition == wrongAnswersIdList.size) {
                binding.btnNext.text = "finished"
                binding.btnNext.setOnClickListener {
                    this.finish()
                }
            }
        }
    }

    // getQuestion() method to retrieve the current question which the user got wrong
    private fun getQuestion(id: Int, binding: ActivityWrongAnswersBinding, wrongAnswersIdList: ArrayList<Int>) {
        if (wrongAnswersIdList.contains(id)) {
            binding.ivQuestionImage.setImageResource(questionsList[id - 1].image)
            binding.tvOptionOne.text = questionsList[id - 1].optionOne
            binding.tvOptionTwo.text = questionsList[id - 1].optionTwo
            binding.tvOptionThree.text = questionsList[id - 1].optionThree
            binding.tvOptionFour.text = questionsList[id - 1].optionFour
            when (questionsList[id - 1].correctAnswer) {
                1 -> binding.tvOptionOne.setBackgroundColor(Color.parseColor("#00FF00"))
                2 -> binding.tvOptionTwo.setBackgroundColor(Color.parseColor("#00FF00"))
                3 -> binding.tvOptionThree.setBackgroundColor(Color.parseColor("#00FF00"))
                4 -> binding.tvOptionFour.setBackgroundColor(Color.parseColor("#00FF00"))
            }
            currentPosition++
        }
    }

    // resetQuestionBorder() method to reset the borders of the previous TextViews
    private fun resetQuestionBorder(binding: ActivityWrongAnswersBinding) {
        binding.tvOptionOne.setBackgroundResource(R.drawable.text_view_border)
        binding.tvOptionTwo.setBackgroundResource(R.drawable.text_view_border)
        binding.tvOptionThree.setBackgroundResource(R.drawable.text_view_border)
        binding.tvOptionFour.setBackgroundResource(R.drawable.text_view_border)
    }
}