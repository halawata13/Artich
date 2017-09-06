package net.halawata.artich.model.mute

import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.entity.ListItem
import net.halawata.artich.enum.Media

class GNewsMute(helper: SQLiteOpenHelper): MediaMute(helper) {

    override fun get(): ArrayList<ListItem> = fetch(Media.GNEWS)

    override fun add(name: String) {
        insert(Media.GNEWS, name)
    }

    override fun remove(id: Int) {
        delete(Media.GNEWS, id)
    }

    override fun save(data: ArrayList<ListItem>) {
        update(Media.GNEWS, data)
    }
}
