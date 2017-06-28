package net.halawata.artich.model.mute

import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.enum.Media

class MediaMuteFactory {

    companion object {
        fun create(mediaType: Media, helper: SQLiteOpenHelper): MediaMuteInterface {
            when (mediaType) {
                Media.COMMON -> return CommonMute(helper)
                Media.HATENA -> return HatenaMute(helper)
                Media.QIITA -> return QiitaMute(helper)
                Media.GNEWS -> return GNewsMute(helper)
            }
        }
    }
}
