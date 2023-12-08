package com.daominh.quickmem.ui.activities.group

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.daominh.quickmem.R
import com.daominh.quickmem.data.dao.GroupDAO
import com.daominh.quickmem.databinding.ActivityEditClassBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class EditClassActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditClassBinding.inflate(layoutInflater) }
    private val groupDAO by lazy { GroupDAO(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        initializeClassDetails()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initializeClassDetails() {
        val classId = intent.getStringExtra("id")
        val group = groupDAO.getGroupById(classId)
        binding.classEt.setText(group.name)
        binding.descriptionEt.setText(group.description)
        binding.privateSt.isChecked = group.status == 1
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tick, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.done) {
            updateClass()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateClass() {
        val name = binding.classEt.text.toString().trim()
        if (name.isEmpty()) {
            binding.classEt.error = "Please enter class name"
            return
        }
        val description = binding.descriptionEt.text.toString().trim()
        val id = intent.getStringExtra("id")
        val status = if (binding.privateSt.isChecked) 1 else 0
        val group = groupDAO.getGroupById(id)
        group.name = name
        group.description = description
        group.status = status
        group.updated_at = getCurrentDate()

        if (groupDAO.updateClass(group) > 0L) {
            onBackPressedDispatcher.onBackPressed()
            Toast.makeText(this, "Update class successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Update class failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getCurrentDateNewApi()
        } else {
            getCurrentDateOldApi()
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun getCurrentDateNewApi(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return currentDate.format(formatter)
    }

    private fun getCurrentDateOldApi(): String {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("dd/MM/yyyy")
        return sdf.format(Date())
    }
}