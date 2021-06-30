package com.myapps.usergithubsub3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.COLUMN_ID_USER
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.COLUMN_IMAGE_USER
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.COLUMN_USERNAME
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_favorite_user"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAVORITE_USER = "CREATE TABLE $TABLE_NAME" +
                " ($COLUMN_ID_USER INTEGER PRIMARY KEY, " +
                " $COLUMN_USERNAME TEXT NOT NULL, " +
                " $COLUMN_IMAGE_USER TEXT NOT NULL)"
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}