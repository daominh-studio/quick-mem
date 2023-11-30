package com.daominh.quickmem.ui.activities.folder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.daominh.quickmem.R
import com.daominh.quickmem.adapter.flashcard.SetFolderViewAdapter
import com.daominh.quickmem.data.dao.FolderDAO
import com.daominh.quickmem.databinding.ActivityAddToFolderBinding
import com.daominh.quickmem.preferen.UserSharePreferences
import com.daominh.quickmem.ui.activities.create.CreateFolderActivity

class AddToFolderActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddToFolderBinding.inflate(layoutInflater) }
    private val folderDAO by lazy { FolderDAO(this) }
    private lateinit var adapter: SetFolderViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val userSharePreferences = UserSharePreferences(this)
        val folders = folderDAO.getAllFolderByUserId(userSharePreferences.id)



        binding.createNewFolderTv.setOnClickListener{
            startActivity(Intent(this, CreateFolderActivity::class.java))
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tick, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.done) {
            val flashCardId = intent.getStringExtra("flashcard_id")
        }
        return super.onOptionsItemSelected(item)
    }
}