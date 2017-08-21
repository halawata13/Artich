package net.halawata.artich.model.mute

import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.entity.ListItem
import net.halawata.artich.enum.Media

class CommonMute(helper: SQLiteOpenHelper) : MediaMute(helper) {

    override fun get(): ArrayList<ListItem> = fetch(Media.COMMON)

    override fun add(name: String) {
        insert(Media.COMMON, name)
    }

    override fun remove(id: Int) {
        delete(Media.COMMON, id)
    }

    override fun save(data: ArrayList<ListItem>) {
        update(Media.COMMON, data)
    }
}
