package net.halawata.artich

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
import net.halawata.artich.model.MenuListAdapter
import net.halawata.artich.model.menu.GNewsMenu
import net.halawata.artich.model.menu.HatenaMenu
import net.halawata.artich.model.menu.QiitaMenu

class MainActivity : AppCompatActivity() {

    val hatenaListFragment = HatenaListFragment()
    val qiitaListFragment = QiitaListFragment()
    val gNewsListFragment = GNewsListFragment()

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
        val hatenaMenu = HatenaMenu(resources)
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        drawerListView = findViewById(R.id.drawer) as ListView
        drawerList = hatenaMenu.getList()

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
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
            0 -> {
                hatenaListFragment.request(urlString)
                hatenaListFragment.selectedTitle = title
            }
            1 -> {
                qiitaListFragment.request(urlString)
                qiitaListFragment.selectedTitle = title
            }
            2 -> {
                gNewsListFragment.request(urlString)
                gNewsListFragment.selectedTitle = title
            }
            else -> {
            }
        }

        drawerLayout.closeDrawer(drawerListView)
    }

    fun selectPage(position: Int) {
        when (position) {
            0 -> {
                val hatenaMenu = HatenaMenu(resources)
                drawerList = hatenaMenu.getList()
                supportActionBar?.title = hatenaListFragment.selectedTitle
            }
            1 -> {
                val qiitaMenu = QiitaMenu(resources)
                drawerList = qiitaMenu.getList()
                supportActionBar?.title = qiitaListFragment.selectedTitle
            }
            2 -> {
                val gNewsMenu = GNewsMenu(resources)
                drawerList = gNewsMenu.getList()
                supportActionBar?.title = gNewsListFragment.selectedTitle
            }
            else -> {
            }
        }

        drawerListAdapter.data = drawerList
        drawerListView.adapter = drawerListAdapter
    }

    private inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            var fragment: Fragment? = null

            when (position) {
                0 -> fragment = hatenaListFragment
                1 -> fragment = qiitaListFragment
                2 -> fragment = gNewsListFragment
                else -> {
                }
            }

            return fragment
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence {
            var title = ""

            when (position) {
                0 -> title = resources.getString(R.string.hatena_tab_name)
                1 -> title = resources.getString(R.string.qiita_tab_name)
                2 -> title = resources.getString(R.string.gnews_tab_name)
                else -> {
                }
            }

            return title
        }
    }

}
