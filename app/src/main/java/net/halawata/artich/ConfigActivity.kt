package net.halawata.artich

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import net.halawata.artich.model.ConfigListAdapter
import net.halawata.artich.model.config.ConfigList

class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        title = "メニュー編集"

        // list setup
        val listView = findViewById(R.id.config_list) as ListView
        val configList = ConfigList(resources)
        val adapter = ConfigListAdapter(this, configList.getMenuList(), R.layout.config_list_item)

        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val mediaType = ((view as LinearLayout).getChildAt(0) as TextView).text as String
            val menuManagementIntent = Intent(this, MenuManagementActivity::class.java)
            menuManagementIntent.putExtra(MenuManagementActivity.mediaTypeKey, mediaType)
            startActivity(menuManagementIntent)
        }
    }
}
