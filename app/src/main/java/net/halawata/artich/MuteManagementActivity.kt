package net.halawata.artich

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter
import net.halawata.artich.entity.ListItem
import net.halawata.artich.enum.Media
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.Log
import net.halawata.artich.model.MuteManagementListAdapter
import net.halawata.artich.model.config.ConfigList
import net.halawata.artich.model.mute.MediaMuteFactory
import net.halawata.artich.model.mute.MediaMuteInterface

class MuteManagementActivity : AppCompatActivity() {

    companion object {
        val mediaTypeKey = "muteManagementMediaTypeKey"
    }

    lateinit var mediaMute : MediaMuteInterface
    lateinit var mediaList: ArrayList<ListItem>
    lateinit var adapter: AlphaInAnimationAdapter
    lateinit var listView: DynamicListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mute_management)

        val mediaString = intent.getStringExtra(MuteManagementActivity.mediaTypeKey)
        val configList = ConfigList(resources, ConfigList.Type.MUTE)
        val mediaType = configList.getMediaId(mediaString) ?: return

        title = resources.getString(R.string.mute_management_activity_title) + "（" + mediaString + "）"

        val helper = DatabaseHelper(this)

        mediaMute = MediaMuteFactory.create(mediaType, helper)
        try {
            mediaList = mediaMute.get()

        } catch (ex: Exception) {
            showError(getString(R.string.loading_fail_data))
            return
        }

        val listAdapter = MuteManagementListAdapter(this, mediaList)
        listView = findViewById(R.id.mute_management_list) as DynamicListView

        val simpleSwipeUndoAdapter = SimpleSwipeUndoAdapter(listAdapter, this, OnDismissCallback { listView, reverseSortedPositions ->
        })

        adapter = AlphaInAnimationAdapter(simpleSwipeUndoAdapter)
        adapter.setAbsListView(listView)
        adapter.viewAnimator?.setInitialDelayMillis(300)
        listView.adapter = adapter

        listView.enableSwipeToDismiss { listView, reverseSortedPositions ->
            for (position in reverseSortedPositions) {
                try {
                    mediaList.removeAt(position)
                    mediaMute.save(mediaList)

                    adapter.notifyDataSetChanged()
                    this.listView.invalidateViews()

                } catch (ex: Exception) {
                    showError(getString(R.string.delete_fail_item))
                }
            }
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, getString(R.string.caption_mute_item), Toast.LENGTH_LONG).show()
        }

        // ミュートがひとつもない場合は説明文を表示
        if (mediaList.count() == 0) {
            (findViewById(R.id.mute_management_not_found) as RelativeLayout).visibility = View.VISIBLE

            val text = when (mediaType) {
                Media.HATENA -> resources.getString(R.string.mute_list_not_found_hatena)
                Media.QIITA -> resources.getString(R.string.mute_list_not_found_qiita)
                Media.GNEWS -> resources.getString(R.string.mute_list_not_found_gnews)
                else -> {
                    Log.e("Invalid Media Type")
                    ""
                }
            }

            (findViewById(R.id.mute_management_not_found_text) as TextView).text = text
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
