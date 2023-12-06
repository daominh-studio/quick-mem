package com.daominh.quickmem.ui.activities.learn

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.daominh.quickmem.adapter.card.CardLeanAdapter
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

    private lateinit var size: String


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (createCards().isEmpty()) {
            showHide()
            Toast.makeText(this, "No card to learn", Toast.LENGTH_SHORT).show()
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        getSize()
        binding.cardsLeftTv.text = "Cards left: $size"

        setupCardStackView()
        setupButton()

        binding.keepLearnBtn.setOnClickListener {
            if (createCards().isEmpty()) {
                Toast.makeText(this, "No card to learn", Toast.LENGTH_SHORT).show()
            } else {
                showContainer()
                binding.cardsLeftTv.text = "Cards left: ${size.toInt() - 1}"
                adapter.setCards(createCards())
                adapter.notifyDataSetChanged()
                getSize()
                binding.cardsLeftTv.text = "Cards left: $size"
            }
        }

        binding.resetLearnBtn.setOnClickListener {
            cardDAO.resetStatusCardByFlashCardId(intent.getStringExtra("id"))
            showContainer()
            adapter.setCards(createCards())
            adapter.notifyDataSetChanged()
            getSize()
            binding.cardsLeftTv.text = "Cards left: $size"
        }
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
            size = size.toInt().minus(1).toString()
            binding.cardsLeftTv.text = "Cards left: ${size.toInt()}"
        } else if (direction == Direction.Left) {
            val learnValue = binding.studyTv.text.toString().toInt() + 1
            binding.studyTv.text = learnValue.toString()
            card.status = 2
            cardDAO.updateCardStatusById(card.id, card.status)
            size = size.toInt().minus(1).toString()
            binding.cardsLeftTv.text = "Cards left: ${size.toInt()}"
        }
        if (manager.topPosition == adapter.getCount()) {
            showHide()
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
        if (manager.topPosition < adapter.itemCount) {
            val card = adapter.getCards()[manager.topPosition]
            if (card.status == 1) {
                card.status = 0
                cardDAO.updateCardStatusById(card.id, card.status)
                if (binding.learnTv.text.toString().toInt() > 0) {
                    binding.learnTv.text = (binding.learnTv.text.toString().toInt() - 1).toString()
                }
            } else if (card.status == 2) {
                card.status = 0
                cardDAO.updateCardStatusById(card.id, card.status)
                if (binding.studyTv.text.toString().toInt() > 0) {
                    binding.studyTv.text = (binding.studyTv.text.toString().toInt() - 1).toString()
                }
            }
        } else {
            Toast.makeText(this, "No card to rewind", Toast.LENGTH_SHORT).show()
        }
        size = size.toInt().plus(1).toString()
        binding.cardsLeftTv.text = "Cards left: ${size.toInt()}"
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

        }

        binding.rewindButton.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            binding.cardStackView.rewind()
        }

        binding.likeButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun createCards(): List<Card> {
        val id: String? = intent.getStringExtra("id")
        return id?.let { cardDAO.getAllCardByStatus(it) } ?: emptyList()
    }


    private fun initialize() {
        manager.setStackFrom(StackFrom.Bottom)
        manager.setVisibleCount(1)
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

        if (learn && cardSlack && button) {
            binding.leanLl.visibility = View.GONE
            binding.cardStackView.visibility = View.GONE
            binding.buttonContainer.visibility = View.GONE
            binding.reviewContainer.visibility = View.VISIBLE
            preview()
        }


    }

    private fun showContainer() {
        if (binding.cardStackView.visibility == View.GONE) {
            binding.cardStackView.visibility = View.VISIBLE
            binding.buttonContainer.visibility = View.VISIBLE
            binding.leanLl.visibility = View.VISIBLE
            binding.reviewContainer.visibility = View.GONE
            binding.learnTv.text = "0"
            binding.studyTv.text = "0"
        }
    }

    private fun preview() {
        binding.knowNumberTv.text = getCardStatus(1).toString()
        binding.stillLearnNumberTv.text = getCardStatus(2).toString()
        binding.termsLeftNumberTv.text = getCardStatus(0).toString()
        val sum =
            (getCardStatus(1).toFloat() / (getCardStatus(0).toFloat() + getCardStatus(1).toFloat() + getCardStatus(2))) * 100
        binding.reviewProgress.setSpinningBarLength(sum)
        binding.reviewProgress.isEnabled = false
        binding.reviewProgress.isFocusableInTouchMode = false
        binding.reviewProgress.setValueAnimated(sum, 1000)
    }

    private fun getSize(): Int {
        size = createCards().size.toString()
        return size.toInt()
    }

    private fun getCardStatus(status: Int): Int {
        val id = intent.getStringExtra("id")
        return cardDAO.getCardByStatus(id, status)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.daominh.quickmem.R.menu.menu_tick, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == com.daominh.quickmem.R.id.done) {
            showHide()
        }
        return super.onOptionsItemSelected(item)
    }

}

