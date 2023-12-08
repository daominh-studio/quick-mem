package com.daominh.quickmem.ui.activities.learn

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.daominh.quickmem.R
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.dao.FlashCardDAO
import com.daominh.quickmem.data.dao.FolderDAO
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ActivityQuizBinding
import com.daominh.quickmem.databinding.ActivityQuizFolderBinding
import com.daominh.quickmem.databinding.DialogCorrectBinding
import com.daominh.quickmem.databinding.DialogWrongBinding
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import kotlinx.coroutines.*

class QuizFolderActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityQuizFolderBinding.inflate(layoutInflater)
    }
    private val cardDAO by lazy {
        CardDAO(this)
    }

    private lateinit var correctAnswer: String
    private var progress = 0
    private val askedCards = mutableListOf<Card>()
    private lateinit var id: String
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private val folderDAO by lazy {
        FolderDAO(this)
    }
    private var dialogCorrect: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setNextQuestion()
        setUpProgressBar()

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun checkAnswer(selectedAnswer: String, cardId: String): Boolean {
        return if (selectedAnswer == correctAnswer) {
            correctDialog(correctAnswer)
            GlobalScope.launch(Dispatchers.IO) {
                cardDAO.updateIsLearnedCardById(cardId, 1)
            }
            setNextQuestion()
            progress++
            increaseProgress()
            true
        } else {
            wrongDialog(correctAnswer, binding.tvQuestion.text.toString(), selectedAnswer)
            setNextQuestion()
            false
        }
    }

    private fun increaseProgress() {
        binding.timelineProgress.progress = progress
    }

    private fun setUpProgressBar(): Int {
        id = intent.getStringExtra("id") ?: "" // get folder id
        val randomCard = cardDAO.getAllCardByFlashCardId(id)
        for (folder in folderDAO.getAllFlashCardIdByFolderId(id)) {
            randomCard.addAll(cardDAO.getAllCardByFlashCardId(folder))
        }
        val max = randomCard.size
        binding.timelineProgress.max = max
        return max
    }

    private fun setNextQuestion() {
        scope.launch {
            id = intent.getStringExtra("id") ?: "" // get folder id
            val cards = cardDAO.getCardByIsLearned(id, 0) // get card by folder id
            val randomCard = cardDAO.getAllCardByFlashCardId(id) // get all card by folder id


            //
            for (folder in folderDAO.getAllFlashCardIdByFolderId(id)) {//ok
                cards.addAll(cardDAO.getCardByIsLearned(folder, 0))
                randomCard.addAll(cardDAO.getAllCardByFlashCardId(folder))
            }

            if (cards.isEmpty()) {
                finishQuiz(1)
                return@launch
            }

            val correctCard = cards.random()
            Log.d("TAG", "setNextQuestion: $correctCard")
            randomCard.remove(correctCard)

            val incorrectCards = randomCard.shuffled().take(3)

            val allCards = (listOf(correctCard) + incorrectCards).shuffled()
            Log.d("QuizFolderActivity", "allCards size: ${allCards.size}")
            allCards.forEach { card ->
                Log.d("QuizFolderActivity", "Card back: ${card.back}")
            }
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

        binding.timelineProgress.progress = setUpProgressBar()
        runOnUiThread {
            if (status == 1) {
                PopupDialog.getInstance(this)
                    .setStyle(Styles.SUCCESS)
                    .setHeading(getString(R.string.finish))
                    .setDescription(getString(R.string.finish_quiz))
                    .setDismissButtonText(getString(R.string.ok))
                    .setCancelable(true)
                    .showDialog(object : OnDialogButtonClickListener() {
                        override fun onDismissClicked(dialog: Dialog?) {
                            super.onDismissClicked(dialog)
                            dialog?.dismiss()
                            dialogCorrect?.create()?.dismiss()
                            finish()
                        }
                    })
            }
        }

    }

    private fun correctDialog(answer: String) {
        dialogCorrect = AlertDialog.Builder(this)
        val dialogBinding = DialogCorrectBinding.inflate(layoutInflater)
        dialogCorrect!!.setView(dialogBinding.root)
        dialogCorrect!!.setCancelable(true)
        val builder = dialogCorrect!!.create()
        dialogBinding.questionTv.text = answer
        dialogCorrect!!.setOnDismissListener {
            //  startAnimations()
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

    private fun startAnimations() {
        val views =
            listOf(
                binding.optionOne,
                binding.optionTwo,
                binding.optionThree,
                binding.optionFour,
                binding.tvQuestion
            )
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
        dialogCorrect?.create()?.dismiss()
    }

}