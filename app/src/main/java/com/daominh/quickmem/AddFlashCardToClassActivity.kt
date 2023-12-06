package com.daominh.quickmem

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.daominh.quickmem.adapter.flashcard.SetClassAdapter
import com.daominh.quickmem.data.dao.FlashCardDAO
import com.daominh.quickmem.data.dao.GroupDAO
import com.daominh.quickmem.databinding.ActivityAddFlashcardToClassBinding
import com.daominh.quickmem.preferen.UserSharePreferences

class AddFlashCardToClassActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddFlashcardToClassBinding.inflate(layoutInflater)
    }
    private val classDAO by lazy {
        GroupDAO(this)
    }
    private val flashCardDAO by lazy {
        FlashCardDAO(this)
    }
    private val userSharePreferences by lazy {
        UserSharePreferences(this)
    }
    private lateinit var adapter: SetClassAdapter
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val id = intent.getStringExtra("flashcard_id")
        val flashCards = flashCardDAO.getAllFlashCardByUserId(userSharePreferences.id)

        adapter = SetClassAdapter(flashCards, id!!)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.classRv.layoutManager = linearLayoutManager
        binding.classRv.adapter = adapter
        adapter.notifyDataSetChanged()



    }
}