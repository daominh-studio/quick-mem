package com.daominh.quickmem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.daominh.quickmem.adapter.SetFolderViewAdapter
import com.daominh.quickmem.data.dao.FlashCardDAO
import com.daominh.quickmem.data.dao.FolderDAO
import com.daominh.quickmem.data.model.FlashCard
import com.daominh.quickmem.data.model.Folder
import com.daominh.quickmem.databinding.ActivityAddFlashCardBinding
import com.daominh.quickmem.preferen.UserSharePreferences

class AddFlashCardActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddFlashCardBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: SetFolderViewAdapter
    private val folderDAO by lazy {
        FolderDAO(this)
    }
    private val userSharePreferences by lazy {
        UserSharePreferences(this)
    }
    private lateinit var folder: Folder
    private lateinit var flashCardDAO: FlashCardDAO
    private lateinit var flashCardList: ArrayList<FlashCard>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
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
                val id = intent.getStringExtra("id_folder")

                val selectedItems = adapter.getItemSelect()
                if (selectedItems.size == 0) {
                    Toast.makeText(this, "Please select at least one set", Toast.LENGTH_SHORT).show()
                } else {
                    if (id != null) {
                        folder = getFolderById(id)
                        for (flashCard in selectedItems) {
                            if (folderDAO.addFlashCardToFolder(id, flashCard.id) > 0L) {
                                Toast.makeText(this, "Add flashcard to folder successfully", Toast.LENGTH_SHORT).show()
                                onBackPressedDispatcher.onBackPressed()
                            } else {
                                Toast.makeText(this, "Add flashcard to folder failed", Toast.LENGTH_SHORT).show()

                            }
                        }
                    } else {
                        Toast.makeText(this, "Folder ID is null", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFolderById(id: String): Folder {
        return folderDAO.getFolderById(id)
    }


}