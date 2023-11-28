package com.daominh.quickmem.ui.activities.folder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.daominh.quickmem.AddFlashCardActivity
import com.daominh.quickmem.R
import com.daominh.quickmem.adapter.SetFolderViewAdapter
import com.daominh.quickmem.data.dao.FolderDAO
import com.daominh.quickmem.data.model.FlashCard
import com.daominh.quickmem.databinding.ActivityViewFolderBinding
import com.daominh.quickmem.preferen.UserSharePreferences
import com.kennyc.bottomsheet.BottomSheetListener
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment
import com.squareup.picasso.Picasso

class ViewFolderActivity : AppCompatActivity(), BottomSheetListener {
    private val binding by lazy { ActivityViewFolderBinding.inflate(layoutInflater) }
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

        val id = intent.getStringExtra("id")
        val userSharePreferences = UserSharePreferences(this)
        val folder = folderDAO.getFolderById(id)
        binding.folderNameTv.text = folder.name
        Picasso.get().load(userSharePreferences.avatar).into(binding.avatarIv)
        binding.userNameTv.text = userSharePreferences.userName
        binding.termCountTv.text = folderDAO.getAllFlashCardByFolderId(id).size.toString()

        adapter = SetFolderViewAdapter(folderDAO.getAllFlashCardByFolderId(id) as ArrayList<FlashCard>)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.setRv.layoutManager = linearLayoutManager
        binding.setRv.adapter = adapter
        adapter.notifyDataSetChanged()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.vew_folder_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu) {
            BottomSheetMenuDialogFragment.Builder(
                context = this,
                sheet = R.menu.folder_menu,
                title = "Folder Menu",
                listener = this
            ).show(supportFragmentManager, "Menu")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSheetDismissed(bottomSheet: BottomSheetMenuDialogFragment, `object`: Any?, dismissEvent: Int) {
        Log.d("TAG", "onSheetDismissed: ")
    }


    override fun onSheetItemSelected(bottomSheet: BottomSheetMenuDialogFragment, item: MenuItem, `object`: Any?) {
        when (item.itemId) {
            R.id.edit_set -> {

            }

            R.id.delete_set -> {

            }

            R.id.add_set -> {
                val id = intent.getStringExtra("id")
                Toast.makeText(this, "Add set$id", Toast.LENGTH_SHORT).show()
                val newIntent = Intent(this, AddFlashCardActivity::class.java)
                newIntent.putExtra("id_folder", id)
                startActivity(newIntent)

            }

            R.id.share -> {

            }
        }
    }

    override fun onSheetShown(bottomSheet: BottomSheetMenuDialogFragment, `object`: Any?) {
        Log.d("TAG", "onSheetShown: ")
    }


}