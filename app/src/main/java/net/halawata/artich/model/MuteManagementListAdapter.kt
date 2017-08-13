package net.halawata.artich.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter
import net.halawata.artich.R
import net.halawata.artich.entity.ListItem
import kotlin.collections.ArrayList

class MuteManagementListAdapter(val context: Context, var data: ArrayList<ListItem>) : BaseAdapter(), UndoAdapter {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mute_list_item, parent, false)
        }

        (view!!.findViewById(R.id.deletable_list_title) as TextView).text = getItem(position).title

        return view
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): ListItem {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getUndoView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.cell_drag_drop, parent, false)
        }
        return view!!
    }

    override fun getUndoClickView(view: View): View {
        return view.findViewById(R.id.titleView)
    }
}
