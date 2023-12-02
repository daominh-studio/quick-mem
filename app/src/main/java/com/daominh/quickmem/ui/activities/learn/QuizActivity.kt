package com.daominh.quickmem.ui.activities.learn

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ActivityQuizBinding
import com.daominh.quickmem.databinding.DialogCorrectBinding
import com.daominh.quickmem.databinding.DialogWrongBinding

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
            correctDialog(correctAnswer)
            setNextQuestion()
        } else {
            wrongDialog(correctAnswer, binding.tvQuestion.text.toString(), selectedAnswer)
            setNextQuestion()
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

    private fun correctDialog(answer: String) {
        val dialog = AlertDialog.Builder(this)
        val dialogBinding = DialogCorrectBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        val builder = dialog.create()
        dialogBinding.questionTv.text = answer
        dialog.setOnDismissListener {
            startAnimations()
        }

        //dismiss after 3s
        dialogBinding.root.postDelayed({
            builder.dismiss()
        }, 3000)

        builder.show()
    }

    private fun wrongDialog(answer: String, question: String, userAnswer: String) {
        val dialog = AlertDialog.Builder(this)
        val dialogBinding = DialogWrongBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        val builder = dialog.create()
        dialogBinding.questionTv.text = question
        dialogBinding.explanationTv.text = answer
        dialogBinding.yourExplanationTv.text = userAnswer
        dialogBinding.continueTv.setOnClickListener {
            builder.dismiss()
        }
        builder.setOnDismissListener {
            startAnimations()
        }
        builder.show()
    }
    private fun startAnimations() {
        val views = listOf(binding.optionOne, binding.optionTwo, binding.optionThree, binding.optionFour, binding.tvQuestion)
        val duration = 1000L
        val endValue = -binding.optionOne.width.toFloat()

        views.forEach { view ->
            val animator = ObjectAnimator.ofFloat(view, "translationX", 0f, endValue)
            animator.duration = duration
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.translationX = 0f
                    if (view == binding.optionFour) {
                        setNextQuestion()
                    }
                }
            })
            animator.start()
        }
    }
}