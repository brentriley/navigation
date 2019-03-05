package com.wtg.navigation.graph

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import com.wtg.navigation.R
import com.wtg.navigation.kotlin.MainActivity
import kotlinx.android.synthetic.main.activity_host.*

class GraphActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        setTitle(R.string.nested_title)

        hostNavBar.setOnNavigationItemSelectedListener(this)

        Toast.makeText(this, getString(R.string.toast_navigation), Toast.LENGTH_SHORT).show()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.page_swap) {
            startActivity(Intent(this, MainActivity::class.java))
            super.finish()
        } else {
            findNavController(R.id.hostContent).navigate(menuItem.itemId)
        }
        return true
    }

    override fun finish() {
        Toast.makeText(this, getString(R.string.backstack_empty), Toast.LENGTH_SHORT).show()
    }
}