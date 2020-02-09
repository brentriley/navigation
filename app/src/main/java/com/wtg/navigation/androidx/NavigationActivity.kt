package com.wtg.navigation.androidx

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brent.navigation.support.NavHostFragment
import com.brent.navigation.tab.TabNavigation
import com.brent.navigation.tabNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wtg.navigation.R
import com.wtg.navigation.kotlin.MainActivity
import kotlinx.android.synthetic.main.activity_home.*

class NavigationActivity : AppCompatActivity(), TabNavigation.OnPageChangedListener, TabNavigation.OnPageReselectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setTitle(R.string.navigation_title)

        tabNavigation.enableRobustLogging()
                .setContainer(R.id.homeContent)
                .useCommitNowForBaseFragments()
                .addPage(R.id.page_a, NavHostFragment.newInstance(R.navigation.child_graph))
                .addPage(R.id.page_b, NavHostFragment.newInstance(R.navigation.child_graph))
                .addPage(R.id.page_c, NavHostFragment.newInstance(R.navigation.child_graph))
                .addListener(this)

        homeNavBar.setOnNavigationItemSelectedListener(this)

        Toast.makeText(this, getString(R.string.toast_navigation), Toast.LENGTH_SHORT).show()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.page_swap) {
            startActivity(Intent(this, MainActivity::class.java))
            super.finish()
        } else {
            tabNavigation.currentPage = menuItem.itemId
        }
        return true
    }

    override fun onNavigationPageChanged(currentPage: Int) {
        Toast.makeText(this, getString(R.string.page_change, currentPage), Toast.LENGTH_SHORT).show()
    }

    override fun onCurrentPageReselected(currentPage: Int) {
        tabNavigation.resetCurrentPage()
    }
}