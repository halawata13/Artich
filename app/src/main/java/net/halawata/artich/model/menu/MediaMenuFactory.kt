package net.halawata.artich.model.menu

import android.content.res.Resources
import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.enum.Media

class MediaMenuFactory {

    companion object {
        fun create(mediaType: Media, helper: SQLiteOpenHelper, resources: Resources): MediaMenuInterface {
            when (mediaType) {
                Media.COMMON -> return CommonMenu(helper, resources)
                Media.HATENA -> return HatenaMenu(helper, resources)
                Media.QIITA -> return QiitaMenu(helper, resources)
                Media.GNEWS -> return GNewsMenu(helper, resources)
            }
        }
    }
}
