package com.daominh.quickmem.ui.activities.learn

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.daominh.quickmem.R
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ActivityTrueFalseFlashCardsBinding
import com.daominh.quickmem.databinding.DialogCorrectBinding
import com.daominh.quickmem.databinding.DialogWrongBinding
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener

class TrueFalseFlashCardsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTrueFalseFlashCardsBinding.inflate(layoutInflater) }
    private val cardDAO by lazy { CardDAO(this) }
    private lateinit var cardList: ArrayList<Card>
    private var progress = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setUpQuestion()
        setUpProgressBar()
    }

    private fun setUpProgressBar(): Int {
        val id = intent.getStringExtra("id")
        cardList = cardDAO.getCardByIsLearned(id, 0)
        binding.timelineProgress.max = cardList.size
        return cardList.size
    }

    private fun setUpQuestion() {
        val id = intent.getStringExtra("id")
        cardList = cardDAO.getCardByIsLearned(id, 0)
        val cardListAll = cardDAO.getAllCardByFlashCardId(id)

        if (cardList.size == 0) {
            finishQuiz()
        }

        if (cardList.isNotEmpty()) {
            val randomCard = cardList.random()
            cardListAll.remove(randomCard)

            val incorrectAnswer = cardListAll.shuffled().take(1)

            val random = (0..1).random()
            if (random == 0) {
                binding.questionTv.text = randomCard.front
                binding.answerTv.text = randomCard.back
            } else {
                binding.questionTv.text = randomCard.front
                binding.answerTv.text = incorrectAnswer[0].back
            }

            binding.trueBtn.setOnClickListener {
                if (random == 0) {
                    correctDialog(randomCard.back)
                    cardDAO.updateIsLearnedCardById(randomCard.id, 1)
                    setUpQuestion()
                    progress++
                    increaseProgress()
                } else {
                    wrongDialog(randomCard.back, randomCard.front, incorrectAnswer[0].back)
                    setUpQuestion()
                }
            }
            binding.falseBtn.setOnClickListener {
                if (random == 1) {
                    correctDialog(randomCard.back)
                    cardDAO.updateIsLearnedCardById(randomCard.id, 1)
                    setUpQuestion()
                    progress++
                    increaseProgress()
                } else {
                    wrongDialog(randomCard.back, randomCard.front, incorrectAnswer[0].back)
                    setUpQuestion()
                }
            }
        }
    }

    private fun increaseProgress() {
        binding.timelineProgress.progress = progress
    }

    private fun finishQuiz() { //1 quiz, 2 learn
        binding.timelineProgress.progress = setUpProgressBar()
        runOnUiThread {

            PopupDialog.getInstance(this)
                .setStyle(Styles.SUCCESS)
                .setHeading(getString(R.string.finish))
                .setDescription(getString(R.string.finish_quiz))
                .setDismissButtonText(getString(R.string.ok))
                .setNegativeButtonText(getString(R.string.cancel))
                .setPositiveButtonText(getString(R.string.ok))
                .setCancelable(false)
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

        }
        builder.show()
    }
}