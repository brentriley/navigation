package com.wtg.navigation.androidx

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.brent.navigation.TabNavigation
import com.brent.navigation.tabNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wtg.navigation.R
import com.wtg.navigation.graph.GraphActivity
import kotlinx.android.synthetic.main.activity_home.*

class NavigationActivity : AppCompatActivity(), TabNavigation.OnPageChangedListener, TabNavigation.OnPageReselectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setTitle(R.string.navigation_title)

        tabNavigation.enableRobustLogging()
                .setContainer(R.id.homeContent)
                .useCommitNowForBaseFragments()
                .addPage(R.id.page_a, NavHostFragment.create(R.navigation.example_graph))
                .addPage(R.id.page_b, NavHostFragment.create(R.navigation.example_graph))
                .addPage(R.id.page_c, NavHostFragment.create(R.navigation.example_graph))
                .addListener(this)

        homeNavBar.setOnNavigationItemSelectedListener(this)

        Toast.makeText(this, getString(R.string.toast_navigation), Toast.LENGTH_SHORT).show()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.page_swap) {
            startActivity(Intent(this, GraphActivity::class.java))
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

    override fun finish() {
        Toast.makeText(this, getString(R.string.backstack_empty), Toast.LENGTH_SHORT).show()
    }
}