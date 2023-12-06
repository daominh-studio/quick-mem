package com.daominh.quickmem.ui.activities.group

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.daominh.quickmem.R
import com.daominh.quickmem.adapter.group.ClassSelectAdapter
import com.daominh.quickmem.data.dao.GroupDAO
import com.daominh.quickmem.databinding.ActivityAddToClassBinding
import com.daominh.quickmem.preferen.UserSharePreferences
import com.daominh.quickmem.ui.activities.create.CreateClassActivity

class AddToClassActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddToClassBinding.inflate(layoutInflater)
    }
    private val classDAO by lazy {
        GroupDAO(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupNewClassIntent()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupNewClassIntent() {
        binding.createNewClassTv.setOnClickListener {
            Intent(this, CreateClassActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun setupRecyclerView() {
        val userSharePreferences = UserSharePreferences(this)
        val classList = classDAO.getClassesOwnedByUser(userSharePreferences.id)
        val adapter = ClassSelectAdapter(classList, intent.getStringExtra("flashcard_id")!!)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.classRv.layoutManager = linearLayoutManager
        binding.classRv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tick, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                onBackPressedDispatcher.onBackPressed()
                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

}