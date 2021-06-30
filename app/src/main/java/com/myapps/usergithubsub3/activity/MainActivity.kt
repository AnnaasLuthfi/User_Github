package com.myapps.usergithubsub3.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.myapps.usergithubsub3.BuildConfig
import com.myapps.usergithubsub3.R
import com.myapps.usergithubsub3.adapter.ListAdapter
import com.myapps.usergithubsub3.databinding.ActivityMainBinding
import com.myapps.usergithubsub3.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var listAdapter: ListAdapter

    private val listItem = ArrayList<User>()
    private val listItemSearchView = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getListMyFollowing()
        viewRecyclerView()
        searchUser()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
                true
            }

            R.id.setting -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                true
            }

            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> true
        }
    }

    private fun searchUser() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.searchView.queryHint = resources.getString(R.string.input_username)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                listItemSearchView.clear()
                binding.recyclerViewMyFollowing.visibility = View.VISIBLE
                getSearchUsername(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun getSearchUsername(Username: String?) {
        showLoading(true)

        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$Username"
        client.addHeader("Authorization", BuildConfig.API_KEY)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                val response = String(responseBody)

                try {
                    val jsonObject = JSONObject(response)
                    val dataArray = jsonObject.getJSONArray("items")

                    for (i in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(i)
                        val data = User()

                        val id = dataObject.getInt("id")
                        val userName = dataObject.getString("login")
                        val imageUser = dataObject.getString("avatar_url")

                        data.idUser = id
                        data.userName = userName
                        data.imageUser = imageUser

                        listItemSearchView.add(data)
                    }

                    listAdapter.setData(listItemSearchView)
                    showLoading(false)

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                showLoading(false)
                val errorMessage = when(statusCode) {
                    401 -> "$statusCode : ${resources.getString(R.string.bad_request)}"
                    403 -> "$statusCode : ${resources.getString(R.string.forbidden)}"
                    404 -> "$statusCode : ${resources.getString(R.string.not_found)}"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        })
    }

    private fun viewRecyclerView() {
        binding.recyclerViewMyFollowing.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMyFollowing.setHasFixedSize(true)
        listAdapter = ListAdapter(this)
        binding.recyclerViewMyFollowing.adapter = listAdapter
    }

    private fun getListMyFollowing() {
        showLoading(true)

        val client = AsyncHttpClient()

        val url = "https://api.github.com/users/AnnaasLuthfi/following"
        client.addHeader("Authorization", BuildConfig.API_KEY)
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                val response = String(responseBody)

                try {
                    val getDataMyFollowing = JSONArray(response)

                    for (i in 0 until getDataMyFollowing.length()) {
                        val dataObject = getDataMyFollowing.getJSONObject(i)
                        val data = User()

                        val id = dataObject.getInt("id")
                        val username = dataObject.getString("login")
                        val imageUser = dataObject.getString("avatar_url")

                        data.idUser = id
                        data.userName = username
                        data.imageUser = imageUser

                        listItem.add(data)
                    }

                    listAdapter.setData(listItem)
                    showLoading(false)

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                showLoading(false)
                val errorMessage = when(statusCode) {
                    401 -> "$statusCode : ${resources.getString(R.string.bad_request)}"
                    403 -> "$statusCode : ${resources.getString(R.string.forbidden)}"
                    404 -> "$statusCode : ${resources.getString(R.string.not_found)}"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        })
    }


    private fun showLoading(state: Boolean) {
        if (state)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.INVISIBLE
    }
}