package com.myapps.userfavorite.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.myapps.userfavorite.R
import com.myapps.userfavorite.adapter.FollowerAdapter
import com.myapps.userfavorite.adapter.FollowingAdapter
import com.myapps.userfavorite.databinding.FragmentFollowingBinding
import com.myapps.userfavorite.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingFragment : Fragment() {

    private lateinit var fragmentFollowingBinding : FragmentFollowingBinding
    private lateinit var adapterFollowing : FollowingAdapter

    private val listItemFollowing = ArrayList<User>()

    companion object {
        private val GETUSERNAME = "username"

        fun newInstance(username: String) : FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(GETUSERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFollowingBinding.bind(view)
        fragmentFollowingBinding = binding

        val username = arguments?.getString(GETUSERNAME)
        getFollowing(username)
        viewRecyclerView(binding)
    }

    private fun viewRecyclerView(binding: FragmentFollowingBinding) {
        adapterFollowing = FollowingAdapter()
        adapterFollowing.notifyDataSetChanged()

        binding.recyclerViewFollowing.setHasFixedSize(true)

        binding.recyclerViewFollowing.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewFollowing.adapter = adapterFollowing

    }

    private fun getFollowing(username: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization", "token c817bbe3150f8239cc18ca827282b0618d74e51f")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                val response = String(responseBody)

                try {

                    val pasFollowing = JSONArray(response)

                    for (i in 0 until pasFollowing.length()) {
                        val dataObject = pasFollowing.getJSONObject(i)
                        val data = User()

                        val usernameFollowing = dataObject.getString("login")
                        val imageUserFollowing = dataObject.getString("avatar_url")

                        data.userName = usernameFollowing
                        data.imageUser = imageUserFollowing

                        listItemFollowing.add(data)
                    }

                    adapterFollowing.setData(listItemFollowing)

                }catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                val errorMessage = when(statusCode){
                    401 -> "$statusCode : ${resources.getString(R.string.bad_request)}"
                    403 -> "$statusCode : ${resources.getString(R.string.forbidden)}"
                    404 -> "$statusCode : ${resources.getString(R.string.not_found)}"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        })
    }

}