package com.daominh.quickmem.ui.activities.learn

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import at.grabner.circleprogress.UnitPosition
import com.daominh.quickmem.adapter.CardLeanAdapter
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ActivityLearnBinding
import com.yuyakaido.android.cardstackview.*

class LearnActivity : AppCompatActivity(), CardStackListener {
    private val binding: ActivityLearnBinding by lazy {
        ActivityLearnBinding.inflate(layoutInflater)
    }
    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val adapter by lazy { CardLeanAdapter(createCards()) }
    private val cardDAO by lazy { CardDAO(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (createCards().isEmpty()) {
            showHide()
            Toast.makeText(this, "No card to learn", Toast.LENGTH_SHORT).show()
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        binding.timelineProgress.max = createCards().size
        binding.timelineProgress.progress = 1


        setupCardStackView()
        setupButton()

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = $direction, r = $ratio")
    }

    @SuppressLint("SetTextI18n")
    override fun onCardSwiped(direction: Direction?) {
        val card = adapter.getCards()[manager.topPosition - 1]
        if (direction == Direction.Right) {
            val learnValue = binding.learnTv.text.toString().toInt() + 1
            binding.learnTv.text = learnValue.toString()
            card.status = 1
            cardDAO.updateCardStatusById(card.id, card.status)
        } else if (direction == Direction.Left) {
            val learnValue = binding.studyTv.text.toString().toInt() + 1
            binding.studyTv.text = learnValue.toString()
            card.status = 2
            cardDAO.updateCardStatusById(card.id, card.status)
        }
        if (manager.topPosition == adapter.getCount()) {
            showHide()
            Toast.makeText(this, "Finish", Toast.LENGTH_SHORT).show()
            // Update progress bar and toolbar title after each swipe
            binding.timelineProgress.progress = manager.topPosition + 1
            binding.toolbarTitle.text = "${manager.topPosition}/${adapter.itemCount}"
        }


    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
        if (manager.topPosition < adapter.itemCount) {
            val card = adapter.getCards()[manager.topPosition]
            if (card.status == 1) {
                val learnValue = binding.learnTv.text.toString().toInt() - 1
                binding.learnTv.text = learnValue.toString()
                card.status = 0
                cardDAO.updateCardStatusById(card.id, card.status)
            } else if (card.status == 2) {
                val studyValue = binding.studyTv.text.toString().toInt() - 1
                binding.studyTv.text = studyValue.toString()
                card.status = 0
                cardDAO.updateCardStatusById(card.id, card.status)
            }
        }
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    @SuppressLint("SetTextI18n")
    private fun setupButton() {
        binding.skipButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
            // Update progress bar and toolbar title after each swipe
            binding.timelineProgress.progress = manager.topPosition + 1
            binding.toolbarTitle.text = "${manager.topPosition}/${adapter.itemCount}"
        }

        binding.rewindButton.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            binding.cardStackView.rewind()
            // Update progress bar and toolbar title after each swipe
            binding.timelineProgress.progress = manager.topPosition + 1
            binding.toolbarTitle.text = "${manager.topPosition}/${adapter.itemCount}"
        }

        binding.likeButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
            // Update progress bar and toolbar title after each swipe
            binding.timelineProgress.progress = manager.topPosition + 1
            binding.toolbarTitle.text = "${manager.topPosition}/${adapter.itemCount}"
        }
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun createCards(): List<Card> {
        val id: String? = intent.getStringExtra("id")
        return id?.let { cardDAO.getAllCardByStatus(it) } ?: emptyList()
    }

    private fun createCard(): Card {
        //return random position
        val position = (1..createCards().size).random()
        return createCards()[position - 1]
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.Bottom)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = adapter
        binding.cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun showHide() {
        val learn = binding.learnTv.visibility == View.VISIBLE
        val cardSlack = binding.cardStackView.visibility == View.VISIBLE
        val button = binding.buttonContainer.visibility == View.VISIBLE
        val reviewContainer = binding.reviewContainer.visibility == View.VISIBLE

        if (learn && cardSlack && button) {
            binding.leanLl.visibility = View.GONE
            binding.cardStackView.visibility = View.GONE
            binding.buttonContainer.visibility = View.GONE
            binding.reviewContainer.visibility = View.VISIBLE
            preview()
        } else if (!learn && !cardSlack && !button && reviewContainer) {
            binding.learnTv.visibility = View.VISIBLE
            binding.cardStackView.visibility = View.VISIBLE
            binding.buttonContainer.visibility = View.VISIBLE
            binding.reviewContainer.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun preview() {
        binding.knowNumberTv.text = getCardStatus(1).toString()
        binding.stillLearnNumberTv.text = getCardStatus(2).toString()
        val sum = (getCardStatus(1).toFloat() / (getCardStatus(0).toFloat() + getCardStatus(1).toFloat() + getCardStatus(2))).toFloat() * 100
        binding.reviewProgress.setUnitPosition(UnitPosition.BOTTOM)
        binding.reviewProgress.setSpinningBarLength(sum)
        binding.reviewProgress.setValueAnimated(sum, 1000)
    }

    private fun getCardStatus(status: Int): Int {
        val id = intent.getStringExtra("id")
        return cardDAO.getCardByStatus(id, status)
    }
}
