package com.wtg.navigation.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.brent.navigation.TabNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wtg.navigation.R;
import com.wtg.navigation.androidx.NavigationActivity;

import static com.brent.navigation.Navigation.getTabNavigation;

public class ExampleActivity extends AppCompatActivity implements TabNavigation.OnPageChangedListener,
        TabNavigation.OnPageReselectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    @NonNull
    private TabNavigation tabNavigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(R.string.java_title);

        tabNavigation = getTabNavigation(this);

        tabNavigation.enableRobustLogging()
                .setContainer(R.id.homeContent)
                .useCommitNowForBaseFragments()
                .addPage(R.id.page_a, ExampleFragment.newInstance(R.color.background_0))
                .addPage(R.id.page_b, ExampleFragment.newInstance(R.color.background_1))
                .addPage(R.id.page_c, ExampleFragment.newInstance(R.color.background_2))
                .addListener(this);

        ((BottomNavigationView) findViewById(R.id.homeNavBar))
                .setOnNavigationItemSelectedListener(this);

        Toast.makeText(this, getString(R.string.toast_java), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.page_swap) {
            startActivity(new Intent(this, NavigationActivity.class));
            super.finish();
        } else {
            tabNavigation.setCurrentPage(menuItem.getItemId());
        }
        return true;
    }

    @Override
    public void onNavigationPageChanged(int currentPage) {
        Toast.makeText(this, getString(R.string.page_change, currentPage), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCurrentPageReselected(int currentPage) {
        tabNavigation.resetCurrentPage();
    }

    @Override
    public void finish() {
        Toast.makeText(this, getString(R.string.backstack_empty), Toast.LENGTH_SHORT).show();
    }
}
