package net.halawata.artich

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
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
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import net.halawata.artich.enum.Media
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
        val configList = ConfigList(resources, ConfigList.Type.MENU)
        val mediaType = configList.getMediaId(mediaString)

        title = resources.getString(R.string.menu_management_activity_title) + if (mediaType != Media.COMMON) "（$mediaString）" else ""

        val helper = DatabaseHelper(this)

        mediaType?.let {
            mediaMenu = MediaMenuFactory.create(mediaType, helper, resources)
            try {
                mediaList = mediaMenu.get()

            } catch (ex: Exception) {
                showError("データの読み込みに失敗しました")
                return
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
                listView.startDragging(position)
                true
            }

            listView.enableSwipeToDismiss { listView, reverseSortedPositions ->
                for (position in reverseSortedPositions) {
                    try {
                        mediaList.removeAt(position)
                        mediaMenu.save(mediaList)

                        adapter.notifyDataSetChanged()
                        this.listView.invalidateViews()

                    } catch (ex: Exception) {
                        showError("項目の削除に失敗しました")
                    }
                }
            }

            val addMenu = findViewById(R.id.menu_management_add_menu) as FloatingActionsMenu
            val addTextBtn = findViewById(R.id.menu_management_add_text_btn) as FloatingActionButton
            addTextBtn.setOnClickListener {
                val dialog = MenuAdditionFragment()
                dialog.mediaType = mediaType
                dialog.show(fragmentManager, "menuAddition")

                addMenu.collapse()
            }

            val addQiitaTagBtn = findViewById(R.id.menu_management_add_qiita_tag_btn) as FloatingActionButton
            addQiitaTagBtn.setOnClickListener {
                addMenu.collapse()

                val intent = Intent(this, QiitaTagSelectionActivity::class.java)
                intent.putExtra(QiitaTagSelectionActivity.mediaTypeKey, mediaString)
                startActivityForResult(intent, 0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                (data.getCharSequenceExtra("selectedTag") as? String)?.let {
                    try {
                        // activity.listView.insert() で最後尾に追加しようとすると落ちるので普通に突っ込む
                        mediaList.add(it)
                        adapter.notifyDataSetChanged()
                        listView.invalidateViews()

                        mediaMenu.save(mediaList)

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        showError("タグの追加に失敗しました")
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // メニューを更新するため更新フラグを立てておく
        val intent = Intent()
        intent.putExtra("reload_menu", true)
        setResult(Activity.RESULT_OK, intent)

        return super.onKeyDown(keyCode, event)
    }

    fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
