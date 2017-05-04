package net.halawata.artich

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import net.halawata.artich.entity.GNewsArticle
import net.halawata.artich.model.AsyncNetworkTask
import net.halawata.artich.model.MediaListAdapter
import net.halawata.artich.model.list.GNewsList
import java.util.*

class GNewsListActivity : AppCompatActivity(), ListActivityInterface {

    override val list = GNewsList()

    var adapter: MediaListAdapter<GNewsArticle>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setTitle(R.string.gnews_list_activity_name)

        val data = ArrayList<GNewsArticle>()

        adapter = MediaListAdapter(this, data, R.layout.list_item)

        val list = findViewById(R.id.list) as ListView
        list.adapter = adapter

        val asyncNetWorkTask = AsyncNetworkTask(this)
        asyncNetWorkTask.execute("https://news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=t")

        list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val text = ((view as LinearLayout).getChildAt(1) as TextView).text as String

            Uri.parse(text).let {
                val intent = Intent(Intent.ACTION_VIEW, it)
                startActivity(intent)
            }
        }
    }

    override fun updateList(content: String) {
        list.parse(content)?.let {
            adapter?.data = it

            val list = findViewById(R.id.list) as ListView
            list.adapter = adapter
        }
    }
}
