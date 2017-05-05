package net.halawata.artich

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById(R.id.tab_layout) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText(R.string.hatena_tab_name))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.qiita_tab_name))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.gnews_tab_name))

        val viewPager = findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)

        tabLayout.setupWithViewPager(viewPager)
    }

    private inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            var fragment: Fragment? = null

            when (position) {
                0 -> fragment = HatenaListFragment()
                1 -> fragment = QiitaListFragment()
                2 -> fragment = GNewsListFragment()
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
