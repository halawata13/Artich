package net.halawata.artich.model.mute

import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.entity.ListItem
import net.halawata.artich.enum.Media

class QiitaMute(helper: SQLiteOpenHelper): MediaMute(helper) {

    override fun get(): ArrayList<ListItem> {
        return fetch(Media.QIITA)
    }

    override fun add(name: String) {
        insert(Media.QIITA, name)
    }

    override fun remove(id: Int) {
        delete(Media.QIITA, id)
    }

    override fun save(data: ArrayList<ListItem>) {
        update(Media.QIITA, data)
    }
}
