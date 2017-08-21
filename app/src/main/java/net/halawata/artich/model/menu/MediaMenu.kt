package net.halawata.artich.model.menu

import android.content.ContentValues
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.enum.Media
import net.halawata.artich.model.Log
import java.lang.Exception

abstract class MediaMenu(val helper: SQLiteOpenHelper, val resources: Resources) : MediaMenuInterface {

    protected fun fetch(mediaType: Media): ArrayList<String> {
        val result: ArrayList<String> = arrayListOf()
        val db = helper.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.query(getTableName(mediaType), arrayOf("name"), null, null, null, null, null, null)

            var eol = cursor.moveToFirst()

            while (eol) {
                result.add(cursor.getString(0))
                eol = cursor.moveToNext()
            }

        } catch (ex: Exception) {
            Log.e(ex.message)
            throw ex

        } finally {
            cursor?.close()
            db.close()
        }

        return result
    }

    protected fun update(mediaType: Media, data: ArrayList<String>) {
        val db = helper.writableDatabase
        db.beginTransaction()

        try {
            db.delete(getTableName(mediaType), null, null)

            var id = 1
            data.forEach { name ->
                val values = ContentValues()
                values.put("id", id)
                values.put("name", name)
                db.insert(getTableName(mediaType), null, values)

                id += 1
            }

            db.setTransactionSuccessful()

        } catch (ex: Exception) {
            Log.e(ex.message)
            throw ex

        } finally {
            db.endTransaction()
            db.close()
        }
    }

    private fun getTableName(mediaType: Media): String {
        when (mediaType) {
            Media.COMMON -> return "t_menu_common"
            Media.HATENA -> return "t_menu_hatena"
            Media.QIITA -> return "t_menu_qiita"
            Media.GNEWS -> return "t_menu_gnews"
        }
    }
}
