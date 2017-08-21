package net.halawata.artich.model.menu

import android.content.res.Resources
import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.enum.Media

object MediaMenuFactory {

    fun create(mediaType: Media, helper: SQLiteOpenHelper, resources: Resources): MediaMenuInterface =
            when (mediaType) {
                Media.COMMON -> CommonMenu(helper, resources)
                Media.HATENA -> HatenaMenu(helper, resources)
                Media.QIITA -> QiitaMenu(helper, resources)
                Media.GNEWS -> GNewsMenu(helper, resources)
            }
}
