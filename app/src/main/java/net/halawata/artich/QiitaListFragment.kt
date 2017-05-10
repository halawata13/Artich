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
import net.halawata.artich.entity.QiitaArticle
import net.halawata.artich.model.ApiUrlString
import net.halawata.artich.model.AsyncNetworkTask
import net.halawata.artich.model.MediaListAdapter
import net.halawata.artich.model.list.QiitaList

class QiitaListFragment : Fragment(), ListFragmentInterface {

    override val list = QiitaList()

    lateinit var selectedTitle: String

    lateinit var listView: ListView

    var adapter: MediaListAdapter<QiitaArticle>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        selectedTitle = resources.getString(R.string.new_entry)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_list, container, false)
        listView = view.findViewById(R.id.list) as ListView

        val data = ArrayList<QiitaArticle>()
        adapter = MediaListAdapter(context, data, R.layout.list_item)
        listView.adapter = adapter

        request(ApiUrlString.Qiita.newEntry)

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val text = ((view as LinearLayout).getChildAt(1) as TextView).text as String

            Uri.parse(text).let {
                startActivity(Intent(Intent.ACTION_VIEW, it))
            }
        }

        return view
    }

    override fun request(urlString: String) {
        val asyncNetWorkTask = AsyncNetworkTask(this)
        asyncNetWorkTask.execute(urlString)
    }

    override fun updateList(content: String) {
        list.parse(content)?.let {
            adapter?.data = it

            listView.adapter = adapter
        }
    }

}
