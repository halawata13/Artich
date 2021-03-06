package net.halawata.artich

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Toast
import net.halawata.artich.entity.Article
import net.halawata.artich.entity.QiitaArticle
import net.halawata.artich.enum.Media
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.mute.GNewsMute
import net.halawata.artich.model.mute.HatenaMute
import net.halawata.artich.model.mute.QiitaMute
import java.net.URL

class ArticleDialogFragment : DialogFragment() {

    lateinit var mediaType: Media
    lateinit var title: String
    lateinit var article: Article

    private val menuItems: Array<String>
        get() = when (mediaType) {
            Media.COMMON -> arrayOf()
            Media.HATENA -> arrayOf(getString(R.string.mute_site))
            Media.QIITA -> arrayOf(getString(R.string.mute_user))
            Media.GNEWS -> arrayOf(getString(R.string.mute_site))
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(title)
                .setPositiveButton("ミュートする", { dialog, which ->
                    val helper = DatabaseHelper(activity!!)

                    when (mediaType) {
                        Media.COMMON -> {}
                        Media.HATENA -> {
                            val mute = HatenaMute(helper)
                            val url = URL(article.url)
                            mute.add(url.host)
                        }
                        Media.QIITA -> {
                            val mute = QiitaMute(helper)
                            val user = (article as QiitaArticle).user
                            mute.add(user)
                        }
                        Media.GNEWS -> {
                            val mute = GNewsMute(helper)
                            val url = URL(article.url)
                            mute.add(url.host)
                        }
                    }

                    targetFragment?.onActivityResult(0, mediaType.id, null)
                    Toast.makeText(activity, getString(R.string.mute_done), Toast.LENGTH_LONG).show()
                })
                .setNegativeButton(getString(R.string.cancel), null)

        return builder.create()
    }
}
