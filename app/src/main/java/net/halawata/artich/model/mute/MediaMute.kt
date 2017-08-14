package net.halawata.artich.model.mute

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.entity.ListItem
import net.halawata.artich.enum.Media
import java.lang.Exception

abstract class MediaMute(val helper: SQLiteOpenHelper) : MediaMuteInterface {

    protected fun fetch(mediaType: Media): ArrayList<ListItem> {
        val result: ArrayList<ListItem> = arrayListOf()
        val db = helper.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.query(getTableName(mediaType), arrayOf("id", "name"), null, null, null, null, null, null)

            var eol = cursor.moveToFirst()

            while (eol) {
                result.add(ListItem(cursor.getLong(0), cursor.getString(1)))
                eol = cursor.moveToNext()
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex

        } finally {
            cursor?.close()
            db.close()
        }

        return result
    }

    protected fun update(mediaType: Media, data: ArrayList<ListItem>) {
        val db = helper.writableDatabase
        db.beginTransaction()

        try {
            db.delete(getTableName(mediaType), null, null)

            var id = 1
            data.forEach { item ->
                val values = ContentValues()
                values.put("id", id)
                values.put("name", item.title)
                db.insert(getTableName(mediaType), null, values)

                id += 1
            }

            db.setTransactionSuccessful()

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex

        } finally {
            db.endTransaction()
            db.close()
        }
    }

    protected fun insert(mediaType: Media, name: String) {
        val data = fetch(mediaType)
        val db = helper.writableDatabase
        db.beginTransaction()

        try {
            val values = ContentValues()
            val id = if (data.isEmpty()) 1 else data.last().id + 1
            values.put("id", id)
            values.put("name", name)
            db.insert(getTableName(mediaType), null, values)

            db.setTransactionSuccessful()

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex

        } finally {
            db.endTransaction()
            db.close()
        }
    }

    protected fun delete(mediaType: Media, id: Int) {
        val db = helper.writableDatabase
        db.beginTransaction()

        try {
            db.delete(getTableName(mediaType), "id = ?", arrayOf(id.toString()))

            db.setTransactionSuccessful()

        } catch (ex: Exception) {
            ex.printStackTrace()
            throw ex

        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getMuteList(): ArrayList<String> {
        val list = ArrayList<String>()

        get().forEach { item ->
            list.add(item.title)
        }

        return list
    }

    private fun getTableName(mediaType: Media): String {
        when (mediaType) {
            Media.COMMON -> return "t_mute_common"
            Media.HATENA -> return "t_mute_hatena"
            Media.QIITA -> return "t_mute_qiita"
            Media.GNEWS -> return "t_mute_gnews"
        }
    }
}
