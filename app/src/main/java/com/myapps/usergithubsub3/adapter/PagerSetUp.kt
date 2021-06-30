package com.myapps.usergithubsub3.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.myapps.usergithubsub3.R
import com.myapps.usergithubsub3.fragment.FollowerFragment
import com.myapps.usergithubsub3.fragment.FollowingFragment

class PagerSetUp (private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    var userName: String? = null
    companion object {
        private val TAB_TITLE = intArrayOf(R.string.following, R.string.follower)
    }

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowingFragment.newInstance(userName.toString())
            1 -> fragment = FollowerFragment.newInstance(userName.toString())
        }

        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLE[position])
    }
}