package net.halawata.artich.model

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import net.halawata.artich.R
import net.halawata.artich.entity.Article
import kotlin.collections.ArrayList

class ArticleListAdapter<T: Article>(val context: Context, var data: ArrayList<T>, val resource: Int): BaseAdapter() {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = data[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val activity = context as Activity
        val item = getItem(position) as Article

        val view = convertView ?: activity.layoutInflater.inflate(resource, null)

        (view.findViewById(R.id.pub_date) as TextView).text = item.pubDate
        (view.findViewById(R.id.title) as TextView).text = item.title
        (view.findViewById(R.id.url) as TextView).text = item.url

        return view
    }
}
