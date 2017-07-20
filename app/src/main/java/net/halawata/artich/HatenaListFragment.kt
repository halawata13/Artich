package net.halawata.artich

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import net.halawata.artich.entity.HatenaArticle
import net.halawata.artich.enum.Media
import net.halawata.artich.model.ApiUrlString
import net.halawata.artich.model.AsyncNetworkTask
import net.halawata.artich.model.ArticleListAdapter
import net.halawata.artich.model.list.HatenaList
import java.net.HttpURLConnection

class HatenaListFragment : Fragment(), ListFragmentInterface {

    override val list = HatenaList()

    lateinit var selectedTitle: String

    var listView: ListView? = null
    var loadingView: RelativeLayout? = null
    var loadingText: TextView? = null

    var adapter: ArticleListAdapter<HatenaArticle>? = null
    var currentUrlString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        selectedTitle = resources.getString(R.string.new_entry)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_list, container, false)
        listView = view.findViewById(R.id.list) as ListView
        loadingView = view.findViewById(R.id.loading_view) as RelativeLayout
        loadingText = view.findViewById(R.id.loading_text) as TextView

        val data = ArrayList<HatenaArticle>()
        adapter = ArticleListAdapter(context, data, R.layout.article_list_item)
        listView?.adapter = adapter

        request(ApiUrlString.Hatena.newEntry)

        listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val text = ((view as LinearLayout).getChildAt(1) as TextView).text as String

            Uri.parse(text).let {
                startActivity(Intent(Intent.ACTION_VIEW, it))
            }
        }

        listView?.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val article = adapter?.data?.get(position)
            val title = article?.url

            title?.let {
                val dialog = ArticleDialogFragment()
                dialog.mediaType = Media.HATENA
                dialog.title = title
                dialog.article = article

                dialog.setTargetFragment(this, 0)
                dialog.show(fragmentManager, "articleDialog")
            }

            true
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        applyFilter()
    }

    override fun request(urlString: String) {
        val asyncNetWorkTask = AsyncNetworkTask()
        asyncNetWorkTask.request(urlString, AsyncNetworkTask.Method.GET)

        loadingView?.alpha = 1F
        loadingText?.text = resources.getString(R.string.loading)

        asyncNetWorkTask.onResponse = { responseCode, content ->
            if (responseCode == HttpURLConnection.HTTP_OK && content != null) {
                list.parse(content)?.let {
                    adapter?.data = list.filter(it, activity)

                    listView?.adapter = adapter
                    loadingView?.alpha = 0F
                }
            } else {
                loadingText?.text = resources.getString(R.string.loading_fail)
            }
        }
    }

    override fun applyFilter() {
        adapter?.let {
            adapter?.data = list.filter(it.data, activity)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun reload() {
        currentUrlString?.let {
            request(it)
        }
    }
}
