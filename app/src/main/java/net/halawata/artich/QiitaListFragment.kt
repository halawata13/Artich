package net.halawata.artich

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import net.halawata.artich.entity.QiitaArticle
import net.halawata.artich.enum.Media
import net.halawata.artich.model.ApiUrlString
import net.halawata.artich.model.ArticleListAdapter
import net.halawata.artich.model.list.QiitaList
import java.net.HttpURLConnection

class QiitaListFragment : Fragment(), ListFragmentInterface {

    override val list = QiitaList()

    override var selectedTitle = "新着エントリー"
    override var selectedUrlString = ApiUrlString.Qiita.newEntry

    private var listView: ListView? = null
    private var loadingView: RelativeLayout? = null
    private var loadingText: TextView? = null
    private var adapter: ArticleListAdapter<QiitaArticle>? = null

    private lateinit var listSwipeRefresh: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_list, container, false)
        listView = view.findViewById(R.id.list) as ListView
        loadingView = view.findViewById(R.id.loading_view) as RelativeLayout
        loadingText = view.findViewById(R.id.loading_text) as TextView

        val loadingProgress = view.findViewById(R.id.loading_progress) as ProgressBar
        loadingProgress.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, R.color.qiita), PorterDuff.Mode.SRC_IN)

        val data = ArrayList<QiitaArticle>()
        adapter = ArticleListAdapter(context, data, R.layout.article_list_item)
        listView?.adapter = adapter

        listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            val text = (v.findViewById(R.id.url) as TextView).text as String

            Uri.parse(text)?.let {
                startActivity(Intent(Intent.ACTION_VIEW, it))
            }
        }

        listView?.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, v, position, id ->
            val article = adapter?.data?.get(position) as QiitaArticle
            val title = article.user

            val dialog = ArticleDialogFragment()
            dialog.mediaType = Media.QIITA
            dialog.title = title
            dialog.article = article

            dialog.setTargetFragment(this, 0)
            dialog.show(fragmentManager, "articleDialog")

            true
        }

        // SwipeRefreshLayout setup
        listSwipeRefresh = view.findViewById(R.id.list_swipe_refresh) as SwipeRefreshLayout
        listSwipeRefresh.setOnRefreshListener {
            reload(false)
        }

        reload()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        applyFilter()
    }

    override fun reserve(urlString: String, title: String) {
        selectedUrlString = urlString
        selectedTitle = title
    }

    override fun update(urlString: String, title: String, useCache: Boolean) {
        activity ?: return

        selectedTitle = title
        selectedUrlString = urlString

        loadingView?.alpha = 1F
        loadingText?.text = resources.getString(R.string.loading)

        request(urlString, title, { responseCode, content ->
            content?.let {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    list.parse(content)?.let {
                        val filtered = list.filter(it, activity)

                        if (filtered.count() > 0) {
                            adapter?.data = filtered

                            listView?.adapter = adapter
                            loadingView?.alpha = 0F
                        } else {
                            loadingText?.text = resources.getString(R.string.loading_not_found)
                        }
                    }
                } else {
                    loadingText?.text = resources.getString(R.string.loading_fail)
                }
            } ?: run {
                loadingText?.text = resources.getString(R.string.loading_fail)
            }

            listSwipeRefresh.isRefreshing = false
        }, useCache)
    }

    override fun applyFilter() {
        adapter?.let {
            adapter?.data = list.filter(it.data, activity)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun reload(useCache: Boolean) {
        update(selectedUrlString, selectedTitle, useCache)
    }
}
