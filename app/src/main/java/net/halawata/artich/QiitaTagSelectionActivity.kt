package net.halawata.artich

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import net.halawata.artich.entity.QiitaTag
import net.halawata.artich.model.ApiUrlString
import net.halawata.artich.model.AsyncNetworkTask
import net.halawata.artich.model.QiitaTagListAdapter
import net.halawata.artich.model.config.QiitaTagList
import java.net.HttpURLConnection

class QiitaTagSelectionActivity : AppCompatActivity() {

    var adapter: QiitaTagListAdapter? = null
    var listView: ListView? = null
    var loadingView: RelativeLayout? = null
    var loadingText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qiita_tag_selection)

        loadingView = findViewById(R.id.loading_view) as RelativeLayout
        loadingText = findViewById(R.id.loading_text) as TextView

        // list setup
        adapter = QiitaTagListAdapter(this, ArrayList<QiitaTag>(), R.layout.qiita_tag_list_item)
        listView = findViewById(R.id.qiita_tag_list) as ListView
        listView?.adapter = adapter

        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            adapter?.data?.get(i)?.title?.let {
                val dialog = ConfirmDialogFragment(it)
                dialog.show(fragmentManager, "qiitaTagAddition")
            }
        }

        // request
        request()
    }

    fun request() {
        loadingView?.alpha = 1F
        loadingText?.text = resources.getString(R.string.loading_tag)

        val asyncNetWorkTask = AsyncNetworkTask()
        asyncNetWorkTask.request(ApiUrlString.Qiita.tagList, AsyncNetworkTask.Method.GET)

        asyncNetWorkTask.onResponse = { responseCode, content ->
            if (responseCode == HttpURLConnection.HTTP_OK && content != null) {
                val qiitaTagList = QiitaTagList()
                qiitaTagList.parse(content)?.let {
                    adapter?.data = it

                    listView?.adapter = adapter
                    loadingView?.alpha = 0F

                } ?: run {
                    loadingText?.text = resources.getString(R.string.loading_fail_tag)
                }
            } else {
                loadingText?.text = resources.getString(R.string.loading_fail_tag)
            }
        }
    }

    /**
     * タグ選択確認ダイアログ
     */
    class ConfirmDialogFragment(val tagName: String? = null) : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            val activity = activity as QiitaTagSelectionActivity

            val title = tagName?.let {
                "「$it」を追加します。"
            } ?: run {
                "確認"
            }

            builder.setTitle(title)
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                        val intent = Intent()
                        intent.putExtra("selectedTag", tagName)
                        activity.setResult(Activity.RESULT_OK, intent)

                        activity.finish()
                    })
                    .setNegativeButton("キャンセル", null)

            return builder.create()
        }
    }
}
