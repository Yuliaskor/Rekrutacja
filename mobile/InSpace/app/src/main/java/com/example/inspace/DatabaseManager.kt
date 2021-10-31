package com.example.inspace

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

const val DATA_BASE_NAME = "News 1.0"
const val TABLE_NAME = "like_news"
const val COL_TITLE = "title"
const val COL_DESCRIPTION = "description"
const val COL_IMG = "img"
const val COL_DATA = "data"

class DataBaseManager(private val context: Context) : SQLiteOpenHelper(context, DATA_BASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME ($COL_TITLE VARCHAR(256),$COL_DESCRIPTION VARCHAR(256),$COL_IMG VARCHAR(256),$COL_DATA VARCHAR(256))"
        db?.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertData(news: News) {
        val db = writableDatabase
        val cv = ContentValues()
        if (check(news.title)){
            cv.put(COL_TITLE, news.title)
            cv.put(COL_DESCRIPTION, news.description)
            cv.put(COL_IMG, news.img)
            cv.put(COL_DATA, news.data)
            val result = db.insert(TABLE_NAME, null, cv)

            if (result == (0).toLong()) { //0 to -1
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun readData(): MutableList<News> {
        val list: MutableList<News> = ArrayList()

        val db = readableDatabase
        val query = "Select * from $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val title = result.getString(result.getColumnIndex(COL_TITLE))
                val description = result.getString(result.getColumnIndex(COL_DESCRIPTION))
                val img = result.getString(result.getColumnIndex(COL_IMG))
                val data = result.getString(result.getColumnIndex(COL_DATA))
                val news = News(title, description, img, data)
                list.add(news)

            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun deleteDate() {
        val db = writableDatabase
        val whereClause = "isDone = 1"
        val whereArgs = arrayOf<String>(1.toString())
        db.delete(TABLE_NAME, null, null)
        db.close()
    }

    private fun check(key: String): Boolean {
        var count =0
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME  WHERE $COL_TITLE like '%$key%'"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                count++
            } while (result.moveToNext())
        }

        result.close()
        return count==0
    }
}