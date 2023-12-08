package com.daominh.quickmem.ui.activities.folder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.daominh.quickmem.R
import com.daominh.quickmem.adapter.folder.FolderSelectAdapter
import com.daominh.quickmem.data.dao.FolderDAO
import com.daominh.quickmem.databinding.ActivityAddToFolderBinding
import com.daominh.quickmem.preferen.UserSharePreferences
import com.daominh.quickmem.ui.activities.create.CreateFolderActivity

class AddToFolderActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddToFolderBinding.inflate(layoutInflater) }
    private val folderDAO by lazy { FolderDAO(this) }
    private lateinit var adapter: FolderSelectAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupCreateNewFolder()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        val userSharePreferences = UserSharePreferences(this)
        val folders = folderDAO.getAllFolderByUserId(userSharePreferences.id)
        adapter = FolderSelectAdapter(folders, intent.getStringExtra("flashcard_id")!!)
        val linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.folderRv.layoutManager = linearLayoutManager
        binding.folderRv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setupCreateNewFolder() {
        binding.createNewFolderTv.setOnClickListener {
            startActivity(Intent(this, CreateFolderActivity::class.java))
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tick, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.done) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }
}