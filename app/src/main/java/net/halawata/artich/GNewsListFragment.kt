package net.halawata.artich

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import net.halawata.artich.entity.GNewsArticle
import net.halawata.artich.enum.Media
import net.halawata.artich.model.ApiUrlString
import net.halawata.artich.model.ArticleListAdapter
import net.halawata.artich.model.Log
import net.halawata.artich.model.list.GNewsList
import java.net.HttpURLConnection
import java.net.URL

class GNewsListFragment : Fragment(), ListFragmentInterface {

    override val list = GNewsList()

    override var selectedTitle = "新着エントリー"
    override var selectedUrlString = ApiUrlString.GNews.newEntry

    private var listView: ListView? = null
    private var loadingView: RelativeLayout? = null
    private var loadingText: TextView? = null
    private var loadingProgress: ProgressBar? = null
    private var adapter: ArticleListAdapter<GNewsArticle>? = null

    private lateinit var listSwipeRefresh: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_list, container, false)
        listView = view.findViewById(R.id.list) as ListView
        loadingView = view.findViewById(R.id.loading_view) as RelativeLayout
        loadingText = view.findViewById(R.id.loading_text) as TextView

        loadingProgress = view.findViewById(R.id.loading_progress) as ProgressBar
        loadingProgress?.indeterminateDrawable?.setColorFilter(ContextCompat.getColor(context, R.color.gnews), PorterDuff.Mode.SRC_IN)

        val data = ArrayList<GNewsArticle>()
        adapter = ArticleListAdapter(context, data, R.layout.article_list_item)
        listView?.adapter = adapter

        listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            val urlString = (v.findViewById(R.id.url) as TextView).text as String
            launchUrl(activity, context, urlString, R.color.gnews)
        }

        listView?.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, v, position, id ->
            try {
                val article = adapter?.data?.get(position) as GNewsArticle
                val title = URL(article.url).host

                val dialog = ArticleDialogFragment()
                dialog.mediaType = Media.GNEWS
                dialog.title = title
                dialog.article = article

                dialog.setTargetFragment(this, 0)
                dialog.show(fragmentManager, "articleDialog")

            } catch (ex: Exception) {
                Log.e(ex.message)
            }

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
        loadingProgress?.visibility = View.VISIBLE

        request(urlString, title, { responseCode, content ->
            var articles = ArrayList<GNewsArticle>()

            content?.let {
                when (responseCode) {
                    HttpURLConnection.HTTP_OK -> {
                        list.parse(content)?.let {
                            articles = list.filter(it, activity)

                            if (articles.count() > 0) {
                                loadingView?.alpha = 0F
                            } else {
                                loadingText?.text = resources.getString(R.string.loading_not_found)
                            }
                        }
                    }
                    HttpURLConnection.HTTP_NOT_FOUND -> {
                        loadingText?.text = resources.getString(R.string.loading_not_found)
                    }
                    else -> {
                        loadingText?.text = resources.getString(R.string.loading_fail)
                    }
                }
            } ?: run {
                loadingText?.text = resources.getString(R.string.loading_fail)
            }

            if (articles.count() == 0) {
                loadingProgress?.visibility = View.INVISIBLE
            }

            adapter?.data = articles
            listView?.adapter = adapter

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
