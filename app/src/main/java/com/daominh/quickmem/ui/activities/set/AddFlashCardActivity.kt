package com.daominh.quickmem.ui.activities.set

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.daominh.quickmem.R
import com.daominh.quickmem.adapter.flashcard.SetFolderViewAdapter
import com.daominh.quickmem.data.dao.FlashCardDAO
import com.daominh.quickmem.data.model.FlashCard
import com.daominh.quickmem.databinding.ActivityAddFlashCardBinding
import com.daominh.quickmem.preferen.UserSharePreferences

class AddFlashCardActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddFlashCardBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: SetFolderViewAdapter
    private val userSharePreferences by lazy {
        UserSharePreferences(this)
    }
    private lateinit var flashCardDAO: FlashCardDAO
    private lateinit var flashCardList: ArrayList<FlashCard>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecyclerView() {
        flashCardDAO = FlashCardDAO(this)
        flashCardList = flashCardDAO.getAllFlashCardByUserId(userSharePreferences.id)
        adapter = SetFolderViewAdapter(flashCardList, true, intent.getStringExtra("id_folder")!!)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.flashcardRv.layoutManager = linearLayoutManager
        binding.flashcardRv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tick, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done -> {
                onBackPressedDispatcher.onBackPressed()
                Toast.makeText(this, "Added to folder", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }


}