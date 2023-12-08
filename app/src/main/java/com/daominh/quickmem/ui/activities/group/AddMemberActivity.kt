package com.daominh.quickmem.ui.activities.group

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.daominh.quickmem.R
import com.daominh.quickmem.adapter.user.UserClassAdapter
import com.daominh.quickmem.data.dao.GroupDAO
import com.daominh.quickmem.data.dao.UserDAO
import com.daominh.quickmem.data.model.User
import com.daominh.quickmem.databinding.ActivityAddMemberBinding
import com.daominh.quickmem.ui.activities.classes.ViewClassActivity

class AddMemberActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddMemberBinding.inflate(layoutInflater) }
    private val userDAO by lazy { UserDAO(this) }
    private lateinit var userAdapter: UserClassAdapter
    private lateinit var users: ArrayList<User>
    private val groupDAO by lazy { GroupDAO(this) }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val group = groupDAO.getGroupById(intent.getStringExtra("id")!!)

        users = userDAO.getAllUserJustNeed()
        users.removeIf(
            fun(user: User): Boolean {
                return user.id == group.user_id
            }
        )
        userAdapter = UserClassAdapter(users, true, intent.getStringExtra("id")!!)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.memberRv.layoutManager = layoutManager
        binding.memberRv.adapter = userAdapter
        binding.memberRv.setHasFixedSize(true)
        userAdapter.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            val searchView = item.actionView as androidx.appcompat.widget.SearchView
            searchView.queryHint = "Search"
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        //convert to lowercase
                        val searchQuery = newText.lowercase()
                        val userSearch = users.filter {
                            it.username.lowercase().contains(searchQuery)
                        }
                        userAdapter = UserClassAdapter(userSearch as ArrayList<User>)
                        binding.memberRv.adapter = userAdapter
                        userAdapter.notifyDataSetChanged()
                    }
                    return true
                }
            })
        }else if (item.itemId == R.id.done) {
            if (intent.hasExtra("id")) {
                val id = intent.getStringExtra("id")!!
                val intent = Intent(this, ViewClassActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
                finish()
            } else {
                // handle the case where the intent does not have an extra with the key "id"
                Toast.makeText(this, "Error: missing id", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}