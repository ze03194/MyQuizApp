package com.example.myquizapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myquizapp.databinding.ActivityQuizQuestionsBinding

// Following Udemy's The Complete Android 12 & Kotlin Development Masterclass
class QuizQuestionsActivity : AppCompatActivity(), java.io.Serializable {
    private var userName: String? = null
    private val wrongAnswersList = ArrayList<Int>()
    private var correctAnswers = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // implementing data binding
        val binding =
            DataBindingUtil.setContentView<ActivityQuizQuestionsBinding>(this, R.layout.activity_quiz_questions)
        val questionsList = Constants.getQuestions()
        var currentPosition = 0

        // call startQuiz() method to start the quiz
        startQuiz(questionsList, binding, currentPosition)
        userName = intent.getStringExtra(Constants.USER_NAME)
    }

    // recursive startQuiz() method
    private fun startQuiz(
        question: ArrayList<Question>,
        binding: ActivityQuizQuestionsBinding,
        currentPosition: Int
    ) {
        // call resetQuestions() method to reset the borders of each TextView question
        resetQuestions(binding)
        
        // selectedAnswer var to contain the user's selection
        var selectedAnswer: Int? = null

        // using data binding to retrieve layout views more efficiently, rather than findViewByID
        binding.tvOptionOne.text = question[currentPosition].optionOne
        binding.tvOptionTwo.text = question[currentPosition].optionTwo
        binding.tvOptionThree.text = question[currentPosition].optionThree
        binding.tvOptionFour.text = question[currentPosition].optionFour
        binding.ivQuestionImage.setImageResource(question[currentPosition].image)
        binding.pbQuizProgress.progress = currentPosition + 1
        binding.tvProgress.text = "${currentPosition + 1}/${binding.pbQuizProgress.max}"

        // individual onClickListeners for each option which calls the getSelectedOption() method to return
        // the user's selected option as an Int
        binding.tvOptionOne.setOnClickListener {
            selectedAnswer = getSelectedOption(binding.tvOptionOne, binding)
        }
        binding.tvOptionTwo.setOnClickListener {
            selectedAnswer = getSelectedOption(binding.tvOptionTwo, binding)
        }
        binding.tvOptionThree.setOnClickListener {
            selectedAnswer = getSelectedOption(binding.tvOptionThree, binding)
        }
        binding.tvOptionFour.setOnClickListener {
            selectedAnswer = getSelectedOption(binding.tvOptionFour, binding)
        }

        /* btnSubmit onClickListener to handle the user's input and call the checkAnswer() method
         if checkAnswer() returns true, recursively call startQuiz() to display the next question, else, change the background
         color of the selected input to red to signify an incorrect answer and wait for the correct answer before proceeding
         */
        binding.btnSubmit.setOnClickListener {
            // if currentPosition is at the last question, change btnSubmit text to "FINISH"
            if (currentPosition == question.size - 1) {
                binding.btnSubmit.text = getString(R.string.finish)
                selectedAnswer?.let { it -> checkAnswer(it, question, currentPosition, binding) }
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(Constants.USER_NAME, userName)
                intent.putExtra(Constants.CORRECT_ANSWERS, correctAnswers.toString())
                intent.putExtra(Constants.TOTAL_QUESTIONS, question.size.toString())
                intent.putExtra(Constants.WRONG_ANSWERS, wrongAnswersList as java.io.Serializable)
                startActivity(intent)
                finish()
            }
            selectedAnswer?.let { it -> checkAnswer(it, question, currentPosition, binding) }

            if (selectedAnswer == null)
                Toast.makeText(this, "Please select an answer!", Toast.LENGTH_SHORT).show()
        }
    }

    // checkAnswer() method to compare the user's selected input to the correct answer for the current question
    // in the question ArrayList
    private fun checkAnswer(
        num: Int,
        question: ArrayList<Question>,
        currentPosition: Int,
        binding: ActivityQuizQuestionsBinding
    ) {
        when (num) {
            1 -> processAnswer(num, question, currentPosition, binding)
            2 -> processAnswer(num, question, currentPosition, binding)
            3 -> processAnswer(num, question, currentPosition, binding)
            4 -> processAnswer(num, question, currentPosition, binding)
        }
    }

    // processAnswer() method to help reduce boilerplate code from checkAnswer() method
    private fun processAnswer(
        num: Int,
        question: ArrayList<Question>,
        currentPosition: Int,
        binding: ActivityQuizQuestionsBinding
    ) {
        if (num == question[currentPosition].correctAnswer) {
            correctAnswers++
            if (currentPosition < (question.size - 1))
                startQuiz(question, binding, currentPosition + 1)
        } else {
            wrongAnswersList.add(question[currentPosition].id)
            if (currentPosition < (question.size - 1)) {
                startQuiz(question, binding, currentPosition + 1)
            }
        }
    }

    // getSelectedOption() method to highlight the user's selected input and return the selected answer as an Int
    private fun getSelectedOption(v: View, binding: ActivityQuizQuestionsBinding): Int {
        when (v.id) {
            R.id.tvOptionOne -> {
                v.setBackgroundResource(R.drawable.selected_text_border)
                binding.tvOptionTwo.setBackgroundResource(R.drawable.text_view_border)
                binding.tvOptionThree.setBackgroundResource(R.drawable.text_view_border)
                binding.tvOptionFour.setBackgroundResource(R.drawable.text_view_border)
                return 1
            }
            R.id.tvOptionTwo -> {
                v.setBackgroundResource(R.drawable.selected_text_border)
                binding.tvOptionOne.setBackgroundResource(R.drawable.text_view_border)
                binding.tvOptionThree.setBackgroundResource(R.drawable.text_view_border)
                binding.tvOptionFour.setBackgroundResource(R.drawable.text_view_border)
                return 2
            }
            R.id.tvOptionThree -> {
                v.setBackgroundResource(R.drawable.selected_text_border)
                binding.tvOptionOne.setBackgroundResource(R.drawable.text_view_border)
                binding.tvOptionTwo.setBackgroundResource(R.drawable.text_view_border)
                binding.tvOptionFour.setBackgroundResource(R.drawable.text_view_border)
                return 3
            }
            R.id.tvOptionFour -> {
                v.setBackgroundResource(R.drawable.selected_text_border)
                binding.tvOptionOne.setBackgroundResource(R.drawable.text_view_border)
                binding.tvOptionTwo.setBackgroundResource(R.drawable.text_view_border)
                binding.tvOptionThree.setBackgroundResource(R.drawable.text_view_border)
                return 4
            }
        }
        return 0
    }

    // resetQuestions() method to reset the borders of each TextView question
    private fun resetQuestions(binding: ActivityQuizQuestionsBinding) {
        binding.tvOptionOne.setBackgroundResource(R.drawable.text_view_border)
        binding.tvOptionTwo.setBackgroundResource(R.drawable.text_view_border)
        binding.tvOptionThree.setBackgroundResource(R.drawable.text_view_border)
        binding.tvOptionFour.setBackgroundResource(R.drawable.text_view_border)
    }
}