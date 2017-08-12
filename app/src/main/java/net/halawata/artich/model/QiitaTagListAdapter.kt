package net.halawata.artich.model

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import net.halawata.artich.R
import net.halawata.artich.entity.QiitaTag

class QiitaTagListAdapter(val context: Context, var data: ArrayList<QiitaTag>, val resource: Int) : BaseAdapter() {

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
        val item = getItem(position) as QiitaTag

        val view = convertView ?: activity.layoutInflater.inflate(resource, null)

        (view.findViewById(R.id.qiita_tag_list_title) as TextView).text = item.title
        // 選択済みのものはチェックマークをつける
        (view.findViewById(R.id.qiita_tag_list_selected) as ImageView).alpha = if (item.selected) 1F else 0F

        return view
    }
}
