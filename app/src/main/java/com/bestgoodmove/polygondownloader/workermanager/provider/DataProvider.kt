package com.bestgoodmove.polygondownloader.workermanager.provider


import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.bestgoodmove.polygondownloader.workermanager.provider.Constants.AUTHORITY

import java.sql.SQLException


class DataProvider : ContentProvider() {

    private var dbHelper: DBHelper? = null
    private val DATABASE_NAME = "table.db"
    private val DATABASE_VERSION = 1
    private val DATUM_TABLE_NAME = "t1"
    private var sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private val DATUM = 1
    private val DATUM_ID = 2
    private var projMap = mutableMapOf<String, String>()

    init {
        sUriMatcher.addURI(AUTHORITY, DATUM_TABLE_NAME, DATUM)
        sUriMatcher.addURI(AUTHORITY, DATUM_TABLE_NAME + "/#", DATUM_ID)
        projMap.put(Constants.ID, Constants.ID)
        projMap.put(Constants.TITLE, Constants.TITLE)
        projMap.put(Constants.PUB_DATE, Constants.PUB_DATE)
        projMap.put(Constants.UPDATED_DATE, Constants.UPDATED_DATE)
        projMap.put(Constants.CONTENT, Constants.CONTENT)
    }

    override fun insert(uri: Uri?, cv: ContentValues?): Uri? {
        if (sUriMatcher.match(uri) != DATUM)
            throw IllegalArgumentException("Unknown URI " + uri)
        /*val v: ContentValues
        if (cv != null)
            v = ContentValues(cv)
        else
            v = ContentValues()
*/
        val db = dbHelper?.writableDatabase
        val rId = db?.insert(DATUM_TABLE_NAME,null, cv)
        if (rId != null) {
            if (rId > 0) {
                val uri = ContentUris.withAppendedId(Constants.URL, rId)
                context.contentResolver.notifyChange(uri, null)
                return uri
            }
        } else {
            throw SQLException("Failed to insert row into " + uri)
        }
        return null
    }

    override fun query(uri: Uri?, p: Array<out String>?, s: String?, args: Array<out String>?, sort: String?): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = DATUM_TABLE_NAME
        qb.setProjectionMap(projMap)
        var s1 = s
        if (sUriMatcher.match(uri) != DATUM) {
            if (sUriMatcher.match(uri) == DATUM_ID)
                s1 = "_id = " + uri?.lastPathSegment
            else
                throw IllegalArgumentException("Unknown URI " + uri)
        }
        val db = dbHelper?.readableDatabase
        val c = qb.query(db, p, s1, args, null, null, sort)
        c.setNotificationUri(context.contentResolver, uri)
        return c
    }

    override fun onCreate(): Boolean {
        dbHelper = DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
        return true
    }

    override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 1
    }

    override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?): Int {
        val db = dbHelper?.writableDatabase
        db?.delete(DATUM_TABLE_NAME,null,null)
        return 1
    }

    override fun getType(uri: Uri?): String {
        if (sUriMatcher.match(uri) == DATUM)
            return Constants.CONTENT_TYPE
        else
            throw IllegalArgumentException("Unknown URI " + uri)
    }

    inner class DBHelper(context: Context?,
                         name: String?,
                         factory: SQLiteDatabase.CursorFactory?,
                         version: Int) : SQLiteOpenHelper(context, name, factory, version) {

        override fun onCreate(_db: SQLiteDatabase?) {
            _db?.execSQL("CREATE TABLE " +
                    DATUM_TABLE_NAME +
                    " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Constants.TITLE + " VARCHAR(20), "
                    + Constants.PUB_DATE +  " VARCHAR(30), "
                    + Constants.UPDATED_DATE +  " VARCHAR(30), "
                    + Constants.CONTENT +  " VARCHAR(200)"
                    + ")")
        }

        override fun onUpgrade(_db: SQLiteDatabase?, _oldVersion: Int, _newVersion: Int) {
            _db?.execSQL("DROP TABLE IF EXISTS " + DATUM_TABLE_NAME)
            onCreate(_db)
        }
    }
}
