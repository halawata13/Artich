package net.halawata.artich

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.MenuManagementListAdapter
import net.halawata.artich.model.config.ConfigList
import net.halawata.artich.model.menu.MediaMenuFactory
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Toast
import net.halawata.artich.model.menu.MediaMenuInterface

class MenuManagementActivity : AppCompatActivity() {

    companion object {
        val mediaTypeKey = "menuManagementMediaTypeKey"
    }

    lateinit var mediaMenu: MediaMenuInterface
    lateinit var mediaList: ArrayList<String>
    lateinit var adapter: AlphaInAnimationAdapter
    lateinit var listView: DynamicListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_management)

        val mediaString = intent.getStringExtra(MenuManagementActivity.mediaTypeKey)
        val configList = ConfigList(resources)
        val mediaType = configList.getMediaId(mediaString)

        val helper = DatabaseHelper(this)

        mediaType?.let {
            mediaMenu = MediaMenuFactory.create(mediaType, helper, resources)
            try {
                mediaList = mediaMenu.get()

            } catch (ex: Exception) {
                showError("データの読み込みに失敗しました")
            }

            val listAdapter = MenuManagementListAdapter(this, mediaList)
            listView = findViewById(R.id.menu_management_list) as DynamicListView

            listView.enableDragAndDrop()
            listView.setDraggableManager(TouchViewDraggableManager(R.id.drag_drop_list_grip))

            val simpleSwipeUndoAdapter = SimpleSwipeUndoAdapter(listAdapter, this, OnDismissCallback { listView, reverseSortedPositions ->
            })

            adapter = AlphaInAnimationAdapter(simpleSwipeUndoAdapter)
            adapter.setAbsListView(listView)
            adapter.viewAnimator?.setInitialDelayMillis(300)
            listView.adapter = adapter

            listView.setOnItemMovedListener { originalPosition, newPosition ->
                try {
                    mediaMenu.save(mediaList)

                } catch (ex: Exception) {
                    showError("データの保存に失敗しました")
                }
            }

            listView.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id ->
                if (listView != null) {
                    listView.startDragging(position)
                    true
                }
                false
            }

            listView.enableSwipeToDismiss { listView, reverseSortedPositions ->
                for (position in reverseSortedPositions) {
                    try {
                        mediaMenu.remove(position)

                        mediaList.removeAt(position)
                        adapter.notifyDataSetChanged()
                        this.listView.invalidateViews()

                    } catch (ex: Exception) {
                        showError("項目の削除に失敗しました")
                    }
                }
            }

            val addBtn = findViewById(R.id.menu_management_add_btn) as FloatingActionButton
            addBtn.setOnClickListener {
                val dialog = MenuAdditionFragment()
                dialog.mediaType = mediaType
                dialog.show(fragmentManager, "menuAddition")
            }
        }
    }

    fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
