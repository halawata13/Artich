package net.halawata.artich

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import net.halawata.artich.entity.Article
import java.util.*

class CuratorListAdaptor<T: Article>(val context: Context, var data: ArrayList<T>, val resource: Int): BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return data[position].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val activity = context as Activity
        val item = getItem(position) as Article

        val view = convertView ?: activity.layoutInflater.inflate(resource, null)

        (view.findViewById(R.id.title) as TextView).text = item.title
        (view.findViewById(R.id.url) as TextView).text = item.url

        return view
    }

}
