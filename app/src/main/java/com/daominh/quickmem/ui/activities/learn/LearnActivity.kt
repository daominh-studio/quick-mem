package com.daominh.quickmem.ui.activities.learn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.daominh.quickmem.R
import com.daominh.quickmem.adapter.CardLeanAdapter
import com.daominh.quickmem.data.dao.CardDAO
import com.daominh.quickmem.data.model.Card
import com.daominh.quickmem.databinding.ActivityLearnBinding
import com.daominh.quickmem.utils.CardDiffCallback
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

        setupCardStackView()
        setupButton()

        binding.skipButton.setOnClickListener {
            binding.cardStackView.swipe()
        }

        binding.rewindButton.setOnClickListener {
            binding.cardStackView.rewind()
        }

        binding.likeButton.setOnClickListener {
            binding.cardStackView.swipe()
        }
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = $direction, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction?) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
        }
        if (manager.topPosition == adapter.itemCount) {
            addLast(1)
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        Log.d("CardStackView", "onCardAppeared: ($position)")
        binding.skipButton.visibility = View.VISIBLE
        binding.rewindButton.visibility = View.VISIBLE
        binding.likeButton.visibility = View.VISIBLE
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        Log.d("CardStackView", "onCardDisappeared: ($position)")
        binding.skipButton.visibility = View.GONE
        binding.rewindButton.visibility = View.GONE
        binding.likeButton.visibility = View.GONE
    }

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
        val id: String = intent.getStringExtra("id").toString()
        return cardDAO.getCardsByFlashCardId(id)
    }

    private fun creatCard(): Card {
        return createCards().first()
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

    private fun paginate() {
        val old = adapter.getCards()
        val new = old.plus(createCards())
        val callback = CardDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun reload() {
        val old = adapter.getCards()
        val new = createCards()
        val callback = CardDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addFirst(size: Int) {
        val old = adapter.getCards()
        val new = mutableListOf<Card>().apply {
            addAll(old)
            for (i in 0 until size) {
                add(manager.topPosition, creatCard())
            }
        }
        val callback = CardDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addLast(size: Int) {
        val old = adapter.getCards()
        val new = mutableListOf<Card>().apply {
            addAll(old)
            addAll(List(size) { creatCard() })
        }
        val callback = CardDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeFirst(size: Int) {
        if (adapter.getCards().isEmpty()) {
            return
        }

        val old = adapter.getCards()
        val new = mutableListOf<Card>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(manager.topPosition)
            }
        }
        val callback = CardDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeLast(size: Int) {
        if (adapter.getCards().isEmpty()) {
            return
        }

        val old = adapter.getCards()
        val new = mutableListOf<Card>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(this.size - 1)
            }
        }
        val callback = CardDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun replace() {
        val old = adapter.getCards()
        val new = mutableListOf<Card>().apply {
            addAll(old)
            removeAt(manager.topPosition)
            add(manager.topPosition, creatCard())
        }
        adapter.setCards(new)
        adapter.notifyItemChanged(manager.topPosition)
    }

    private fun swap() {
        val old = adapter.getCards()
        val new = mutableListOf<Card>().apply {
            addAll(old)
            val first = removeAt(manager.topPosition)
            val last = removeAt(this.size - 1)
            add(manager.topPosition, last)
            add(first)
        }
        val callback = CardDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setCards(new)
        result.dispatchUpdatesTo(adapter)
    }

}