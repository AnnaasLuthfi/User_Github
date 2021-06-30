package com.myapps.usergithubsub3.helper

import android.database.Cursor
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.COLUMN_ID_USER
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.COLUMN_IMAGE_USER
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.COLUMN_USERNAME
import com.myapps.usergithubsub3.model.User

object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor?) : ArrayList<User> {
        val list = ArrayList<User>()
        cursor?.apply {
            while (moveToNext()){
                val user = User()
                user.idUser = getInt(getColumnIndexOrThrow(COLUMN_ID_USER))
                user.userName = getString(getColumnIndexOrThrow(COLUMN_USERNAME))
                user.imageUser = getString(getColumnIndexOrThrow(COLUMN_IMAGE_USER))
                list.add(user)
            }
        }
        return list
    }

    fun mapCursorToObject(userCursor: Cursor?): User {
        var user = User()
        userCursor?.apply {
            moveToFirst()
            val id_user = getInt(getColumnIndexOrThrow(COLUMN_ID_USER))
            val userName = getString(getColumnIndexOrThrow(COLUMN_USERNAME))
            val imageUser = getString(getColumnIndexOrThrow(COLUMN_IMAGE_USER))
            user = User(id_user, userName, imageUser)
        }
        return user
    }
}