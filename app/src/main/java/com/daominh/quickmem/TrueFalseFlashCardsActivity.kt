package com.daominh.quickmem

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.dao.FlashCardDAO
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setUpQuestion()
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
            Log.d("TrueFalsee", "random: $random")
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
                    Log.d("TrueFalsee", "true1: ${randomCard.id}")
                    setUpQuestion()
                } else {
                    wrongDialog(randomCard.back, randomCard.front, incorrectAnswer[0].back)
                    Log.d("TrueFalsee", "false:1 ${randomCard.id}")
                    setUpQuestion()
                }
            }
            binding.falseBtn.setOnClickListener {
                if (random == 1) {
                    correctDialog(randomCard.back)
                    cardDAO.updateIsLearnedCardById(randomCard.id, 1)
                    Log.d("TrueFalsee", "true: ${randomCard.id}")
                    setUpQuestion()
                } else {
                    wrongDialog(randomCard.back, randomCard.front, incorrectAnswer[0].back)
                    Log.d("TrueFalsee", "false: ${randomCard.id}")
                    setUpQuestion()
                }
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