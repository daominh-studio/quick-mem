package com.daominh.quickmem.ui.activities.folder

import android.app.Dialog
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
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
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
        binding.termCountTv.text = folderDAO.getAllFlashCardByFolderId(id).size.toString() + " flashcards"

        adapter = SetFolderViewAdapter(folderDAO.getAllFlashCardByFolderId(id) as ArrayList<FlashCard>, false)
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
            R.id.edit_folder -> {

            }

            R.id.delete_folder -> {

                PopupDialog.getInstance(this)
                    .setStyle(Styles.STANDARD)
                    .setHeading("Delete Folder")
                    .setDescription("Are you sure you want to delete this folder?")
                    .setPopupDialogIcon(R.drawable.ic_delete)
                    .setCancelable(true)
                    .showDialog(
                        object : OnDialogButtonClickListener() {
                            override fun onPositiveClicked(dialog: Dialog?) {
                                super.onPositiveClicked(dialog)
                                if (folderDAO.deleteFolder(intent.getStringExtra("id"))> 0L) {
                                    PopupDialog.getInstance(this@ViewFolderActivity)
                                        .setStyle(Styles.SUCCESS)
                                        .setHeading(getString(R.string.success))
                                        .setDescription(getString(R.string.delete_set_success))
                                        .setCancelable(false)
                                        .setDismissButtonText(getString(R.string.ok))
                                        .showDialog(object : OnDialogButtonClickListener() {
                                            override fun onDismissClicked(dialog: Dialog) {
                                                super.onDismissClicked(dialog)
                                                finish()
                                            }
                                        })
                                } else {
                                    PopupDialog.getInstance(this@ViewFolderActivity)
                                        .setStyle(Styles.FAILED)
                                        .setHeading(getString(R.string.error))
                                        .setDescription(getString(R.string.delete_set_error))
                                        .setCancelable(true)
                                        .showDialog(object : OnDialogButtonClickListener() {
                                            override fun onPositiveClicked(dialog: Dialog) {
                                                super.onPositiveClicked(dialog)
                                            }
                                        })
                                }
                            }

                            override fun onNegativeClicked(dialog: Dialog?) {
                                super.onNegativeClicked(dialog)
                                dialog?.dismiss()
                            }
                        }
                    )
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

    override fun onResume() {
        super.onResume()

        //refresh data adapter
        val id = intent.getStringExtra("id")
        adapter = SetFolderViewAdapter(folderDAO.getAllFlashCardByFolderId(id) as ArrayList<FlashCard>, false)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.setRv.layoutManager = linearLayoutManager
        binding.setRv.adapter = adapter
        adapter.notifyDataSetChanged()
    }


}