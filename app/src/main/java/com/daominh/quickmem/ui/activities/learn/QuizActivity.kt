package com.daominh.quickmem.ui.activities.learn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.daominh.quickmem.R
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    private val cardDAO by lazy {
        CardDAO(this)
    }

    private lateinit var correctAnswer: String
    private val cards by lazy { cardDAO.getCardsByFlashCardId(intent.getStringExtra("id")) }

    private val askedCards = mutableListOf<Card>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setNextQuestion()
        binding.optionOne.setOnClickListener { checkAnswer(binding.optionOne.text.toString()) }
        binding.optionTwo.setOnClickListener { checkAnswer(binding.optionTwo.text.toString()) }
        binding.optionThree.setOnClickListener { checkAnswer(binding.optionThree.text.toString()) }
        binding.optionFour.setOnClickListener { checkAnswer(binding.optionFour.text.toString()) }
    }

    private fun checkAnswer(selectedAnswer: String) {
        if (selectedAnswer == correctAnswer) {
            // Show an alert saying the answer is correct
            AlertDialog.Builder(this)
                .setTitle("Correct!")
                .setMessage("Good job, that's the right answer!")
                .setPositiveButton("Next question") { _, _ ->
                    setNextQuestion()
                }
                .show()
        } else {
            // Show an alert with the correct answer
            AlertDialog.Builder(this)
                .setTitle("Incorrect!")
                .setMessage("The correct answer was $correctAnswer.")
                .setPositiveButton("Next question") { _, _ ->
                    setNextQuestion()
                }
                .show()
        }
    }

    private fun setNextQuestion() {
        // Lọc ra những câu chưa hỏi
        val unaskedCards = cards.filter { it !in askedCards }

        // Nếu không còn câu hỏi, kết thúc bài kiểm tra
        if (unaskedCards.isEmpty()) {
            finishQuiz()
            return
        }

        // Lấy 1 câu hỏi chưa hỏi
        val correctCard = unaskedCards.shuffled().take(1)
        askedCards.add(correctCard[0])

        // Lấy 3 câu trả lời sai từ những câu hỏi
        val wrongCards = cards.filter { it != correctCard[0] }.shuffled().take(3)
        correctAnswer = correctCard[0].back

        // Tạo danh sách chứa tất cả câu trả lời và xáo trộn nó
        val allAnswers = (correctCard + wrongCards).shuffled()

        // Đặt các câu trả lời vào các nút tương ứng
        binding.optionOne.text = allAnswers[0].back
        binding.optionTwo.text = allAnswers[1].back
        binding.optionThree.text = allAnswers[2].back
        binding.optionFour.text = allAnswers[3].back

        // Đặt câu hỏi
        binding.tvQuestion.text = correctCard[0].front
    }

    private fun finishQuiz() {
        // Xử lý khi hết câu hỏi
    }
}