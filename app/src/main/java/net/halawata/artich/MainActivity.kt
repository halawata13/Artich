package net.halawata.artich

import android.content.Intent
import android.content.res.Configuration
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
import net.halawata.artich.model.ArticleCache
import net.halawata.artich.model.DatabaseHelper
import net.halawata.artich.model.MenuListAdapter
import net.halawata.artich.model.config.ConfigList
import net.halawata.artich.model.menu.GNewsMenu
import net.halawata.artich.model.menu.HatenaMenu
import net.halawata.artich.model.menu.MediaMenu
import net.halawata.artich.model.menu.QiitaMenu

class MainActivity : AppCompatActivity() {

    val hatenaListFragment = HatenaListFragment()
    val qiitaListFragment = QiitaListFragment()
    val gNewsListFragment = GNewsListFragment()

    private val dbHelper = DatabaseHelper(this)

    private lateinit var hatenaMenu: HatenaMenu
    private lateinit var qiitaMenu: QiitaMenu
    private lateinit var gnewsMenu: GNewsMenu

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerListView: ListView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerList: ArrayList<SideMenuItem>
    private lateinit var drawerListAdapter: MenuListAdapter
    private lateinit var menu: MediaMenu

    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // cache init
        ArticleCache.init(this)

        // SideMenu setup
        hatenaMenu = HatenaMenu(dbHelper, resources)
        qiitaMenu = QiitaMenu(dbHelper, resources)
        gnewsMenu = GNewsMenu(dbHelper, resources)

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
                reloadMenu(position)
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
            selectSideMenuItem(position)
        }

        // article init
        reloadMenu(Page.HATENA.num)
        supportActionBar?.title = hatenaListFragment.selectedTitle
    }

    override fun onResume() {
        super.onResume()

        drawerListAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()

        // cache clear
        ArticleCache.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            // 記事更新フラグが立ってたら更新
            if (data.getBooleanExtra("reload_article", false)) {
                hatenaListFragment.reload()
                qiitaListFragment.reload()
                gNewsListFragment.reload()
            }

            // サイドメニュー更新フラグが立っていたら更新
            if (data.getBooleanExtra("reload_menu", false)) {
                drawerList = menu.getMenuList()
                drawerListAdapter.data = drawerList
                drawerListView.adapter = drawerListAdapter
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, resources.getString(R.string.menu_management_activity_title))
        menu?.add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, resources.getString(R.string.mute_management_activity_title))

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // サイドメニュー編集
        if (item?.itemId == Menu.FIRST + 1) {
            val intent = Intent(this, MenuManagementActivity::class.java)
            intent.putExtra(MenuManagementActivity.mediaTypeKey, resources.getString(R.string.common_list_name))
            startActivityForResult(intent, 0)

            return true
        }

        // ミュート
        if (item?.itemId == Menu.FIRST + 2) {
            val intent = Intent(this, ConfigActivity::class.java)
            intent.putExtra(ConfigActivity.configTypeKey, ConfigList.Type.MUTE.num)
            startActivityForResult(intent, 0)

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

    /**
     * サイドメニュー項目選択時
     */
    private fun selectSideMenuItem(position: Int) {
        val title = drawerList[position].title
        val urlString = drawerList[position].urlString

        supportActionBar?.title = title

        // 現在表示しているメディアの記事を更新しつつ、同名のサイドメニュー項目があれば他のメディアも更新
        when (viewPager.currentItem) {
            Page.HATENA.num -> {
                hatenaListFragment.update(urlString, title)

                qiitaMenu.getUrlStringFrom(title)?.let { qiitaListFragment.update(it, title) }
                gnewsMenu.getUrlStringFrom(title)?.let { gNewsListFragment.reserve(it, title) }
            }

            Page.QIITA.num -> {
                qiitaListFragment.update(urlString, title)

                hatenaMenu.getUrlStringFrom(title)?.let { hatenaListFragment.update(it, title) }
                gnewsMenu.getUrlStringFrom(title)?.let { gNewsListFragment.update(it, title) }
            }

            Page.GNEWS.num -> {
                gNewsListFragment.update(urlString, title)

                hatenaMenu.getUrlStringFrom(title)?.let { hatenaListFragment.reserve(it, title) }
                qiitaMenu.getUrlStringFrom(title)?.let { qiitaListFragment.update(it, title) }
            }
        }

        drawerLayout.closeDrawer(drawerListView)
    }

    /**
     * ページャー移動時
     */
    fun selectPage(position: Int) {
        val tabLayout = findViewById(R.id.tab_layout) as TabLayout

        when (position) {
            Page.HATENA.num -> {
                supportActionBar?.title = hatenaListFragment.selectedTitle
                window.statusBarColor = resources.getColor(R.color.hatena)
                supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.hatena, null))

                tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.hatena))
            }

            Page.QIITA.num -> {
                supportActionBar?.title = qiitaListFragment.selectedTitle
                window.statusBarColor = resources.getColor(R.color.qiita)
                supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.qiita, null))

                tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.qiita))
            }

            Page.GNEWS.num -> {
                supportActionBar?.title = gNewsListFragment.selectedTitle
                window.statusBarColor = resources.getColor(R.color.gnews)
                supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.gnews, null))

                tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.gnews))
            }
        }
    }

    /**
     * サイドメニューを更新する
     */
    fun reloadMenu(position: Int) {
        when (position) {
            Page.HATENA.num -> {
                menu = HatenaMenu(dbHelper, resources)
                drawerList = menu.getMenuList()
            }

            Page.QIITA.num -> {
                menu = QiitaMenu(dbHelper, resources)
                drawerList = menu.getMenuList()
            }

            Page.GNEWS.num -> {
                menu = GNewsMenu(dbHelper, resources)
                drawerList = menu.getMenuList()
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

        override fun getCount(): Int = Page.values().count()

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
