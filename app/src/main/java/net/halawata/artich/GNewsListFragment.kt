package net.halawata.artich

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import net.halawata.artich.entity.GNewsArticle
import net.halawata.artich.model.AsyncNetworkTask
import net.halawata.artich.model.MediaListAdapter
import net.halawata.artich.model.list.GNewsList

class GNewsListFragment : Fragment(), ListActivityInterface {

    override val list = GNewsList()

    lateinit var listView: ListView

    var adapter: MediaListAdapter<GNewsArticle>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_list, container, false)
        listView = view.findViewById(R.id.list) as ListView

        val data = ArrayList<GNewsArticle>()
        adapter = MediaListAdapter(context, data, R.layout.list_item)
        listView.adapter = adapter

        val asyncNetWorkTask = AsyncNetworkTask(this)
        asyncNetWorkTask.execute("https://news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=t")

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val text = ((view as LinearLayout).getChildAt(1) as TextView).text as String

            Uri.parse(text).let {
                val intent = Intent(Intent.ACTION_VIEW, it)
                startActivity(intent)
            }
        }

        return view
    }

    override fun updateList(content: String) {
        list.parse(content)?.let {
            adapter?.data = it

            listView.adapter = adapter
        }
    }

}
