package com.myapps.usergithubsub3.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.COLUMN_ID_USER
import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion.TABLE_NAME
import java.sql.SQLException
//import com.myapps.usergithubsub3.db.DatabaseContract.GithubColumns.Companion._ID
import kotlin.jvm.Throws

class UserHelper (context: Context){

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database : SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: UserHelper? = null

        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: UserHelper(context).apply {
                    INSTANCE = this
                }
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll() : Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_ID_USER ASC",
            null
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                "$COLUMN_ID_USER = ?",
                arrayOf(id),
                null,
                null,
                null,
                null)
    }

    fun insert(values: ContentValues?) : Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$COLUMN_ID_USER = '$id' ", null)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$COLUMN_ID_USER = ?", arrayOf(id))
    }
}