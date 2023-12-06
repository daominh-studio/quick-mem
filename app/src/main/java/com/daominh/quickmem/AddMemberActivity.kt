package com.daominh.quickmem

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.daominh.quickmem.adapter.user.UserClassAdapter
import com.daominh.quickmem.data.dao.UserDAO
import com.daominh.quickmem.data.model.User
import com.daominh.quickmem.databinding.ActivityAddMemberBinding

class AddMemberActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddMemberBinding.inflate(layoutInflater) }
    private val userDAO by lazy { UserDAO(this) }
    private lateinit var userAdapter: UserClassAdapter
    private lateinit var users: ArrayList<User>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        users = userDAO.getAllUserJustNeed()
        userAdapter = UserClassAdapter(users)
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
        } else if (item.itemId == R.id.done) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}