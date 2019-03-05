package com.wtg.navigation.kotlin

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.brent.navigation.TabNavigation
import com.brent.navigation.tabNavigation
import com.wtg.navigation.R
import com.wtg.navigation.java.ExampleActivity
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : AppCompatActivity(), TabNavigation.OnPageChangedListener, TabNavigation.OnPageReselectedListener, BottomNavigationView.OnNavigationItemSelectedListener
        , TabNavigation.AnimationInterceptor{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tabNavigation.enableRobustLogging()
                .setContainer(R.id.homeContent)
                .useCommitNowForBaseFragments()
                .addPage(R.id.page_b, ExampleFragment.newInstance(R.color.background_1))
                .addPage(R.id.page_a, ExampleFragment.newInstance(R.color.background_0))
                .addPage(R.id.page_a, ExampleFragment.newInstance(R.color.background_0))
                .addPage(R.id.page_c, ExampleFragment.newInstance(R.color.background_2))
                .addListener(this)

        homeNavBar.selectedItemId = tabNavigation.currentPage
        homeNavBar.setOnNavigationItemSelectedListener(this)

        Toast.makeText(this, getString(R.string.toast_kotlin), Toast.LENGTH_SHORT).show()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.page_swap) {
            startActivity(Intent(this, ExampleActivity::class.java))
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

    override fun onOverrideTabAnimation(animations: TabNavigation.AnimSet) {
        animations.enterRes = 1
    }
}
