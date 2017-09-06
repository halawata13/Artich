package net.halawata.artich.model.mute

import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.entity.ListItem
import net.halawata.artich.enum.Media

class HatenaMute(helper: SQLiteOpenHelper): MediaMute(helper) {

    override fun get(): ArrayList<ListItem> = fetch(Media.HATENA)

    override fun add(name: String) {
        insert(Media.HATENA, name)
    }

    override fun remove(id: Int) {
        delete(Media.HATENA, id)
    }

    override fun save(data: ArrayList<ListItem>) {
        update(Media.HATENA, data)
    }
}
