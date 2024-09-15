package com.daominh.quickmem.ui.activities.learn

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.daominh.quickmem.R
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ActivityQuizBinding
import com.daominh.quickmem.databinding.DialogCorrectBinding
import com.daominh.quickmem.databinding.DialogWrongBinding
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    private val cardDAO by lazy {
        CardDAO(this)
    }

    private var progress = 0
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
        val max = cardDAO.getCardByIsLearned(id, 0).size
        binding.timelineProgress.max = max
    }

    private fun checkAnswer(selectedAnswer: String, cardId: String): Boolean {
        return if (selectedAnswer == correctAnswer) {
            correctDialog(correctAnswer)
            CoroutineScope(Dispatchers.IO).launch {
                cardDAO.updateIsLearnedCardById(cardId, 1)
            }
            setNextQuestion()
            progress++
            setUpProgressBar()
            true
        } else {
            wrongDialog(correctAnswer, binding.tvQuestion.text.toString(), selectedAnswer)
            setNextQuestion()
            false
        }
    }

    private fun setUpProgressBar() {
        binding.timelineProgress.progress = progress
    }


    private fun setNextQuestion() {
        scope.launch {
            val cards = cardDAO.getCardByIsLearned(id, 0) // get a list of cards that are not learned
            val randomCard = cardDAO.getAllCardByFlashCardId(id) // get all cards

            if (cards.isEmpty()) {
                finishQuiz()
                return@launch

            }

            val correctCard = cards.random() // get a random card from a list of cards that are not learned
            randomCard.remove(correctCard) // remove the correct card from a list of all cards

            val incorrectCards = randomCard.shuffled().take(3) // get 3 random cards from list of all cards

            val allCards = (listOf(correctCard) + incorrectCards).shuffled() // shuffle 4 cards
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

    private fun finishQuiz() { //1 quiz, 2 learn
        runOnUiThread {

            PopupDialog.getInstance(this)
                .setStyle(Styles.SUCCESS)
                .setHeading(getString(R.string.finish))
                .setDescription(getString(R.string.finish_quiz))
                .setDismissButtonText(getString(R.string.ok))
                .setNegativeButtonText(getString(R.string.cancel))
                .setPositiveButtonText(getString(R.string.ok))
                .setCancelable(true)
                .showDialog(object : OnDialogButtonClickListener() {
                    override fun onDismissClicked(dialog: Dialog?) {
                        super.onDismissClicked(dialog)
                        dialog?.dismiss()
                        finish()
                    }
                })
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
            // startAnimations()
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
            //startAnimations()
        }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}