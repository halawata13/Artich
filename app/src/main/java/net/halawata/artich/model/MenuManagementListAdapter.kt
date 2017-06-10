package net.halawata.artich.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nhaarman.listviewanimations.ArrayAdapter
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter
import net.halawata.artich.R
import java.util.*

class MenuManagementListAdapter(val context: Context, var data: ArrayList<String>) : ArrayAdapter<String>(data), UndoAdapter {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.drag_drop_list_item, parent, false)
        }

        (view!!.findViewById(R.id.drag_drop_list_title) as TextView).text = getItem(position)

        return view
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
