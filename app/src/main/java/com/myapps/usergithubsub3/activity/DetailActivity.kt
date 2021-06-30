package com.myapps.usergithubsub3.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.myapps.usergithubsub3.BuildConfig
import com.myapps.usergithubsub3.R
import com.myapps.usergithubsub3.adapter.PagerSetUp
import com.myapps.usergithubsub3.databinding.ActivityDetailBinding
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.COLUMN_ID_USER
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.COLUMN_IMAGE_USER
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.COLUMN_USERNAME
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.CONTENT_URI
import com.myapps.usergithubsub3.db.UserHelper
import com.myapps.usergithubsub3.helper.MappingHelper
import com.myapps.usergithubsub3.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var userHelper: UserHelper
    private var user: User? = null      //model
    private var position: Int = 0
//    private var del = false     //g usah

    private var statusFavorite = false

    private lateinit var uriWithId: Uri

    companion object {
        const val GETUSERNAME = "username"
        const val EXTRA_DATA = "extradata"

        const val EXTRA_POSITION = "extra_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(GETUSERNAME)

        supportActionBar?.title = "Detail User"

        getDetail(username)
        setTabLayoutAndPassData(username)
        setUp()
        checkIsFavoriteOrNotInDatabase()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.setting -> {
                startActivity(Intent(this@DetailActivity, SettingsActivity::class.java))
                true
            }

            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> true
        }
    }

    private fun checkIsFavoriteOrNotInDatabase(){
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user?.idUser)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        val myFavorite = MappingHelper.mapCursorToArrayList(cursor)
        for (data in myFavorite) {
            if (user?.idUser == data.idUser) {
                statusFavorite = true
                setIconFavorite(statusFavorite)
            }
        }
    }


    private fun setUp() {

        user = intent.getParcelableExtra(EXTRA_DATA)

        if (user != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)

            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user?.idUser)
            val cursor = contentResolver.query(uriWithId, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                user = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
        } else {
            user = User()
        }
    }

    private fun setTabLayoutAndPassData(username: String?) {
        val pagerSetUp = PagerSetUp(this, supportFragmentManager)
        pagerSetUp.userName = username

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = pagerSetUp

        val tabs: TabLayout = findViewById(R.id.tabLayout)
        tabs.setupWithViewPager(viewPager)
    }

    private fun getDetail(username: String?) {
        showLoading(true)

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", BuildConfig.API_KEY)
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                showLoading(false)
                val response = String(responseBody)

                try {
                    val getDataDetail = JSONObject(response)

                    val id = getDataDetail.getInt("id")
                    val userName = getDataDetail.getString("login")
                    val nameUser = getDataDetail.getString("name")
                    val imageUser = getDataDetail.getString("avatar_url")

                    val companyUser = getDataDetail.getString("company")
                    val locationUser = getDataDetail.getString("location")
                    val followingNumber = getDataDetail.getInt("following")
                    val followerNumber = getDataDetail.getInt("followers")

                    binding.tvNameUserDetail.text = nameUser
                    binding.tvUsernameDetail.text = userName
                    Glide.with(this@DetailActivity)
                        .load(imageUser)
                        .apply(RequestOptions.overrideOf(100, 100))
                        .into(binding.imageUserDetail)
                    binding.company.text = companyUser
                    binding.location.text = locationUser
                    binding.followingNumber.text = followingNumber.toString()
                    binding.followerNumber.text = followerNumber.toString()

                    binding.favoriteButton.setOnClickListener {

                        if (!statusFavorite) {
                            user?.userName = userName
                            user?.imageUser = imageUser

                            val intent = Intent()
                            intent.putExtra(EXTRA_DATA, user)
                            intent.putExtra(EXTRA_POSITION, position)

                            val values = ContentValues()
                            values.put(COLUMN_ID_USER, id)
                            values.put(COLUMN_USERNAME, userName)
                            values.put(COLUMN_IMAGE_USER, imageUser)

                            contentResolver.insert(CONTENT_URI, values)

                            statusFavorite = !statusFavorite
                            setIconFavorite(statusFavorite)
                            Toast.makeText(this@DetailActivity, resources.getString(R.string.success_add_favorite), Toast.LENGTH_SHORT).show()

                        } else {
                            contentResolver.delete(uriWithId, null, null)
                            statusFavorite = !statusFavorite
                            setIconFavorite(statusFavorite)
                            Toast.makeText(this@DetailActivity, resources.getString(R.string.remove_from_favorite), Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : ${resources.getString(R.string.bad_request)}"
                    403 -> "$statusCode : ${resources.getString(R.string.forbidden)}"
                    404 -> "$statusCode : ${resources.getString(R.string.not_found)}"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        })
    }

    private fun setIconFavorite(statusFavorite: Boolean) {

        if (statusFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24_red)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24_white)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}