package com.stepa0751.mynotebook.db

import android.provider.BaseColumns

object MyDbNameClass : BaseColumns {
    val TABLE_NAME = "my_table"
    val COLUMN_NAME_TITLE = "title"
    val COLUMN_NAME_CONTENT = "content"
    val COLUMN_NAME_TIME = "time"
    val DATABASE_VERSION = 2
    val DATABASE_NAME = "MyDb.db"

    val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY,$COLUMN_NAME_TITLE TEXT,$COLUMN_NAME_CONTENT TEXT, " +
            "$COLUMN_NAME_TIME TEXT)"

    val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}