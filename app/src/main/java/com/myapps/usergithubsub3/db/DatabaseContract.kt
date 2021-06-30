package com.myapps.usergithubsub3.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.myapps.usergithubsub3"
    const val SCHEME = "content"

    class GithubColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "favorite_user"

            const val COLUMN_ID_USER = "id_user"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_IMAGE_USER = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}