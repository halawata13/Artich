package net.halawata.artich

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.os.Bundle
import android.widget.EditText
import net.halawata.artich.enum.Media
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.menu.MediaMenuFactory

class MenuAdditionFragment : DialogFragment() {
    var mediaType: Media? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val content = inflater.inflate(R.layout.fragment_menu_addition, null)

        builder.setView(content)

        builder.setTitle("項目を追加")
                .setPositiveButton("追加", DialogInterface.OnClickListener { dialogInterface, i ->
                    val editText = content.findViewById(R.id.menu_addition_text) as EditText

                    mediaType?.let {
                        val text = editText.text.toString()
                        val helper = DatabaseHelper(activity)
                        val mediaMenu = MediaMenuFactory.create(it, helper, activity.resources)
                        val activity = activity as MenuManagementActivity

                        try {
                            // activity.listView.insert() で最後尾に追加しようとすると落ちるので普通に突っ込む
                            activity.mediaList.add(text)
                            mediaMenu.save(activity.mediaList)
                            activity.adapter.notifyDataSetChanged()
                            activity.listView.invalidateViews()

                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            activity.showError("データの読み込みに失敗しました")
                        }
                    }
                })
                .setNegativeButton("キャンセル", null)

        return builder.create()
    }
}
