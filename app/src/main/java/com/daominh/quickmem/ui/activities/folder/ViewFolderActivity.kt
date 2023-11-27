package com.daominh.quickmem.ui.activities.folder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.daominh.quickmem.R
import com.daominh.quickmem.data.dao.FolderDAO
import com.daominh.quickmem.data.model.FlashCard
import com.daominh.quickmem.databinding.ActivityViewFolderBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kennyc.bottomsheet.BottomSheetListener
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment

class ViewFolderActivity : AppCompatActivity(), BottomSheetListener {
    private val binding by lazy { ActivityViewFolderBinding.inflate(layoutInflater) }
    private val folderDAO by lazy { FolderDAO(this) }
    private lateinit var flashcardList: ArrayList<FlashCard>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get folder id from intent
        val id = intent.getStringExtra("id")
        flashcardList = folderDAO.getAllFlashCardByFolderId(id)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.vew_folder_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu -> {
                BottomSheetMenuDialogFragment.Builder(
                    context = this,
                    sheet = R.menu.folder_menu,
                    title = "Folder Menu",
                    listener = this
                ).show(supportFragmentManager, "Menu")


            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSheetDismissed(bottomSheet: BottomSheetMenuDialogFragment, `object`: Any?, dismissEvent: Int) {
        TODO("Not yet implemented")
    }

    override fun onSheetItemSelected(bottomSheet: BottomSheetMenuDialogFragment, item: MenuItem, `object`: Any?) {
        when (item.itemId) {
            R.id.edit_set -> {

            }

            R.id.delete_set -> {

            }

            R.id.add_set -> {

            }

            R.id.share -> {

            }
        }
    }

    override fun onSheetShown(bottomSheet: BottomSheetMenuDialogFragment, `object`: Any?) {
       Log.d("BottomSheet", "onSheetShown")
    }


}