package net.halawata.artich

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import net.halawata.artich.entity.HatenaArticle
import java.util.*

class HatenaListActivity: AppCompatActivity() {

    var adaptor: CuratorListAdaptor<HatenaArticle>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setTitle(R.string.hatena_list_activity_name)

        val data = ArrayList<HatenaArticle>()
        data.add(HatenaArticle(id = 0, title = "Google", url = "https://www.google.co.jp", pubDate = "2017/02/16"))
        data.add(HatenaArticle(id = 1, title = "Yahoo", url = "http://www.yahoo.co.jp", pubDate = "2017/02/17"))

        adaptor = CuratorListAdaptor(this, data, R.layout.list_item)

        val list = findViewById(R.id.list) as ListView
        list.adapter = adaptor

        list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val text = ((view as LinearLayout).getChildAt(1) as TextView).text as String
            val uri = Uri.parse(text)

            uri.let {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
    }
}
