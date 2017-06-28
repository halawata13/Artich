package net.halawata.artich.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(val context: Context): SQLiteOpenHelper(context, "hoge", null, 1) {

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE t_menu_common (id INTEGER PRIMARY KEY, name TEXT)")
        p0?.execSQL("INSERT INTO t_menu_common(id, name) VALUES(1, 'Kotlin')")
        p0?.execSQL("INSERT INTO t_menu_common(id, name) VALUES(2, 'Swift')")
        p0?.execSQL("INSERT INTO t_menu_common(id, name) VALUES(3, 'JavaScript')")

        p0?.execSQL("CREATE TABLE t_menu_hatena (id INTEGER PRIMARY KEY, name TEXT)")
        p0?.execSQL("CREATE TABLE t_menu_qiita (id INTEGER PRIMARY KEY, name TEXT)")
        p0?.execSQL("CREATE TABLE t_menu_gnews (id INTEGER PRIMARY KEY, name TEXT)")

        p0?.execSQL("CREATE TABLE t_mute_hatena (id INTEGER PRIMARY KEY, name TEXT)")
        p0?.execSQL("CREATE TABLE t_mute_qiita (id INTEGER PRIMARY KEY, name TEXT)")
        p0?.execSQL("CREATE TABLE t_mute_gnews (id INTEGER PRIMARY KEY, name TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS t_menu_common")
        p0?.execSQL("DROP TABLE IF EXISTS t_menu_hatena")
        p0?.execSQL("DROP TABLE IF EXISTS t_menu_qiita")
        p0?.execSQL("DROP TABLE IF EXISTS t_menu_gnews")

        p0?.execSQL("DROP TABLE IF EXISTS t_mute_hatena")
        p0?.execSQL("DROP TABLE IF EXISTS t_mute_qiita")
        p0?.execSQL("DROP TABLE IF EXISTS t_mute_gnews")

        onCreate(p0)
    }
}
