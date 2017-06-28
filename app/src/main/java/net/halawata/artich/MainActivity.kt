package net.halawata.artich

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import net.halawata.artich.entity.SideMenuItem
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.MenuListAdapter
import net.halawata.artich.model.config.ConfigList
import net.halawata.artich.model.menu.GNewsMenu
import net.halawata.artich.model.menu.HatenaMenu
import net.halawata.artich.model.menu.QiitaMenu

class MainActivity : AppCompatActivity() {

    val hatenaListFragment = HatenaListFragment()
    val qiitaListFragment = QiitaListFragment()
    val gNewsListFragment = GNewsListFragment()

    val dbHelper: SQLiteOpenHelper = DatabaseHelper(this)

    lateinit var drawerLayout: DrawerLayout
    lateinit var drawerListView: ListView
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var drawerList: ArrayList<SideMenuItem>
    lateinit var drawerListAdapter: MenuListAdapter

    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TabLayout setup
        val tabLayout = findViewById(R.id.tab_layout) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText(R.string.hatena_tab_name))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.qiita_tab_name))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.gnews_tab_name))

        viewPager = findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                selectPage(position)
            }

            override fun onPageSelected(position: Int) {
                selectPage(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        tabLayout.setupWithViewPager(viewPager)

        // Drawer setup
        val hatenaMenu = HatenaMenu(dbHelper, resources)
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        drawerListView = findViewById(R.id.drawer) as ListView
        drawerList = hatenaMenu.getMenuList()

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START)
        drawerListAdapter = MenuListAdapter(this, drawerList, R.layout.drawer_list_item)
        drawerListView.adapter = drawerListAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(view: View) {
                invalidateOptionsMenu()
            }

            override fun onDrawerOpened(drawerView: View) {
                invalidateOptionsMenu()
            }
        }

        drawerLayout.addDrawerListener(drawerToggle)

        drawerListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            selectItem(position)
        }

        selectItem(0)
    }

    override fun onResume() {
        super.onResume()

        drawerListAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (resultCode == Activity.RESULT_OK && data.getBooleanExtra("reload", false)) {
                hatenaListFragment.reload()
                qiitaListFragment.reload()
                gNewsListFragment.reload()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "メニュー管理")
        menu?.add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, "ミュート")

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == Menu.FIRST + 1) {
            val configIntent = Intent(this, ConfigActivity::class.java)
            configIntent.putExtra(ConfigActivity.configTypeKey, ConfigList.Type.MENU.num)
            startActivityForResult(configIntent, 0)

            return true
        }

        if (item?.itemId == Menu.FIRST + 2) {
            val configIntent = Intent(this, ConfigActivity::class.java)
            configIntent.putExtra(ConfigActivity.configTypeKey, ConfigList.Type.MUTE.num)
            startActivityForResult(configIntent, 0)

            return true
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    fun selectItem(position: Int) {
        val title = drawerList[position].title
        val urlString = drawerList[position].urlString

        supportActionBar?.title = title

        when (viewPager.currentItem) {
            Page.HATENA.num -> {
                hatenaListFragment.request(urlString)
                hatenaListFragment.selectedTitle = title
            }
            Page.QIITA.num -> {
                qiitaListFragment.request(urlString)
                qiitaListFragment.selectedTitle = title
            }
            Page.GNEWS.num -> {
                gNewsListFragment.request(urlString)
                gNewsListFragment.selectedTitle = title
            }
        }

        drawerLayout.closeDrawer(drawerListView)
    }

    fun selectPage(position: Int) {
        val tabLayout = findViewById(R.id.tab_layout) as TabLayout

        when (position) {
            Page.HATENA.num -> {
                val hatenaMenu = HatenaMenu(dbHelper, resources)
                drawerList = hatenaMenu.getMenuList()

                supportActionBar?.title = hatenaListFragment.selectedTitle
                window.statusBarColor = resources.getColor(R.color.hatena)
                supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.hatena, null))

                tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.hatena))
            }
            Page.QIITA.num -> {
                val qiitaMenu = QiitaMenu(dbHelper, resources)
                drawerList = qiitaMenu.getMenuList()

                supportActionBar?.title = qiitaListFragment.selectedTitle
                window.statusBarColor = resources.getColor(R.color.qiita)
                supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.qiita, null))

                tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.qiita))
            }
            Page.GNEWS.num -> {
                val gNewsMenu = GNewsMenu(dbHelper, resources)
                drawerList = gNewsMenu.getMenuList()

                supportActionBar?.title = gNewsListFragment.selectedTitle
                window.statusBarColor = resources.getColor(R.color.gnews)
                supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.gnews, null))

                tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.gnews))
            }
        }

        drawerListAdapter.data = drawerList
        drawerListView.adapter = drawerListAdapter
    }

    private inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            var fragment: Fragment? = null

            when (position) {
                Page.HATENA.num -> fragment = hatenaListFragment
                Page.QIITA.num -> fragment = qiitaListFragment
                Page.GNEWS.num -> fragment = gNewsListFragment
            }

            return fragment
        }

        override fun getCount(): Int {
            return Page.values().count()
        }

        override fun getPageTitle(position: Int): CharSequence {
            var title = ""

            when (position) {
                Page.HATENA.num -> title = resources.getString(R.string.hatena_tab_name)
                Page.QIITA.num -> title = resources.getString(R.string.qiita_tab_name)
                Page.GNEWS.num -> title = resources.getString(R.string.gnews_tab_name)
            }

            return title
        }
    }

    private enum class Page(val num: Int) {
        HATENA(0),
        QIITA(1),
        GNEWS(2),
    }

}
