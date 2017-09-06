package net.halawata.artich.model.mute

import android.database.sqlite.SQLiteOpenHelper
import net.halawata.artich.enum.Media

class MediaMuteFactory {

    companion object {
        fun create(mediaType: Media, helper: SQLiteOpenHelper): MediaMuteInterface =
                when (mediaType) {
                    Media.COMMON -> CommonMute(helper)
                    Media.HATENA -> HatenaMute(helper)
                    Media.QIITA -> QiitaMute(helper)
                    Media.GNEWS -> GNewsMute(helper)
                }
    }
}
