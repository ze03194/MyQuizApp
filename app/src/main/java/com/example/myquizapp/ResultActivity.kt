package com.example.myquizapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myquizapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // implement data binding
        val binding = DataBindingUtil.setContentView<ActivityResultBinding>(this, R.layout.activity_result)
        // retrieve needed values through intent from previous the previous activity
        val correctAnswers = intent.getStringExtra(Constants.CORRECT_ANSWERS)
        val totalQuestions = intent.getStringExtra(Constants.TOTAL_QUESTIONS)
        val wrongAnswersID = intent.getIntegerArrayListExtra(Constants.WRONG_ANSWERS)
        val userName = intent.getStringExtra(Constants.USER_NAME)

        binding.tvInputName.text = "$userName!"
        binding.tvInputResult.text = "${correctAnswers.toString()}/${totalQuestions.toString()}"

        if (correctAnswers == totalQuestions)
            binding.tvCheckAnswers.visibility = View.GONE

        // onClickListener if user would like to view correct answers for the question(s) they've got wrong
        // if clicked, user is directed to the WrongAnswersActivity
        binding.tvCheckAnswers.setOnClickListener {
            val intent = Intent(this, WrongAnswersActivity::class.java)
            intent.putExtra(Constants.WRONG_ANSWERS, wrongAnswersID)
            startActivity(intent)
            finish()
        }

        binding.btnFinish.setOnClickListener {
            finish()
        }
    }
}