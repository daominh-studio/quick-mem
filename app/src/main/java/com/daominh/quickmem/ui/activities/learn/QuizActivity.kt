package com.daominh.quickmem.ui.activities.learn

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.dao.FlashCardDAO
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ActivityQuizBinding
import com.daominh.quickmem.databinding.DialogCorrectBinding
import com.daominh.quickmem.databinding.DialogWrongBinding
import kotlinx.coroutines.*

class QuizActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    private val cardDAO by lazy {
        CardDAO(this)
    }
    private val flashCardDAO by lazy {
        FlashCardDAO(this)
    }

    private lateinit var correctAnswer: String
    private val askedCards = mutableListOf<Card>()
    private lateinit var id: String
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        id = intent.getStringExtra("id") ?: ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setNextQuestion()
    }

    private fun checkAnswer(selectedAnswer: String, cardId: String): Boolean {
        return if (selectedAnswer == correctAnswer) {
            correctDialog(correctAnswer)
            GlobalScope.launch(Dispatchers.IO) {
                cardDAO.updateIsLearnedCardById(cardId, 1)
            }
            setNextQuestion()
            true
        } else {
            wrongDialog(correctAnswer, binding.tvQuestion.text.toString(), selectedAnswer)
            setNextQuestion()
            false
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun setNextQuestion() {
       scope.launch {
           val cards = cardDAO.getCardByIsLearned(id, 0)
           val randomCard = cardDAO.getAllCardByFlashCardId(id)

           if (cards.isEmpty()) {
               finishQuiz(1)
               return@launch

           }

           val correctCard = cards.random()
           randomCard.remove(correctCard)

           val incorrectCards = randomCard.shuffled().take(3)

           val allCards = (listOf(correctCard) + incorrectCards).shuffled()
           val question = correctCard.front
           correctAnswer = correctCard.back

           withContext(Dispatchers.Main) {
               binding.tvQuestion.text = question
               binding.optionOne.text = allCards[0].back
               binding.optionTwo.text = allCards[1].back
               binding.optionThree.text = allCards[2].back
               binding.optionFour.text = allCards[3].back

               binding.optionOne.setOnClickListener {
                   checkAnswer(binding.optionOne.text.toString(), correctCard.id)
               }

               binding.optionTwo.setOnClickListener {
                   checkAnswer(binding.optionTwo.text.toString(), correctCard.id)
               }

               binding.optionThree.setOnClickListener {
                   checkAnswer(binding.optionThree.text.toString(), correctCard.id)
               }

               binding.optionFour.setOnClickListener {
                   checkAnswer(binding.optionFour.text.toString(), correctCard.id)
               }

               askedCards.add(correctCard)


           }
       }
    }

    private fun finishQuiz(status: Int) { //1 quiz, 2 learn
        runOnUiThread {
            Toast.makeText(this, "Finish", Toast.LENGTH_SHORT).show()
        }

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
        val views =
            listOf(binding.optionOne, binding.optionTwo, binding.optionThree, binding.optionFour, binding.tvQuestion)
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}