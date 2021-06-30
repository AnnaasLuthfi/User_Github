package com.myapps.userfavorite.activity

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapps.userfavorite.R
import com.myapps.userfavorite.adapter.ListAdapter
import com.myapps.userfavorite.databinding.ActivityFavoriteBinding
import com.myapps.userfavorite.db.DatabaseContract.GithubColumns.Companion.CONTENT_URI
import com.myapps.userfavorite.helper.MappingHelper
import com.myapps.userfavorite.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var listAdapter: ListAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My Favorite"

        listAdapter = ListAdapter(this)

        viewRecyclerView()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavAsync()
        } else {
            savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)?.also { listAdapter.mData = it }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, listAdapter.mData)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                startActivity(Intent(this@FavoriteActivity, SettingsActivity::class.java))
                true
            }

            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> true
        }
    }

    private fun loadFavAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val defUser = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val user = defUser.await()
            if (user.size > 0) {
                listAdapter.mData = user
            } else {
                listAdapter.mData = ArrayList()
                Toast.makeText(this@FavoriteActivity, resources.getString(R.string.not_data), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun viewRecyclerView() {
        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFavorite.setHasFixedSize(true)
        listAdapter = ListAdapter(this)
        binding.recyclerViewFavorite.adapter = listAdapter
    }


}