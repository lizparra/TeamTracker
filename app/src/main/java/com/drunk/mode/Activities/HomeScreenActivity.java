package com.drunk.mode.Activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.drunk.mode.Adapters.MyPagerAdapter;
import com.drunk.mode.Fragments.HomeFragment;
import com.drunk.mode.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreenActivity extends AppCompatActivity {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.vp_horizontal_ntb) AHBottomNavigationViewPager viewPager;
    @BindView(R.id.bottomNavigation) AHBottomNavigation bottomNavigationView;

    private NavigationView navigationView;
    Fragment currentFragment;
    AHBottomNavigationAdapter navigationAdapter;
    MyPagerAdapter myPagerAdapter;
    private int[] tabColors;

    boolean doubleBackToExitPressedOnce = false;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        ButterKnife.bind(this);


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        setToolbar();

        setViewPager();
        setBottomNav();
        setTabSelectedListener();

        tabColors = this.getResources().getIntArray(R.array.tab_colors);
        navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.menu_bottom_navigation);
        navigationAdapter.setupWithBottomNavigation(bottomNavigationView, tabColors);
    }


    private void setBottomNav()
    {
        bottomNavigationView.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        bottomNavigationView.setBehaviorTranslationEnabled(true);
        bottomNavigationView.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigationView.setUseElevation(true);
        bottomNavigationView.setAccentColor(Color.parseColor("#283a69"));
    }




    private void setTabSelectedListener() {
        bottomNavigationView.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (currentFragment == null) {
                    currentFragment = (HomeFragment) myPagerAdapter.getCurrentFragment();
                }

                if (wasSelected) {

                    return true;
                }

                viewPager.setCurrentItem(position, false);

                if (currentFragment == null) {
                    return true;
                }

                currentFragment = myPagerAdapter.getCurrentFragment();

                return true;
            }

        });

    }

    private void setToolbar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
    }

    private void setViewPager()
    {

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), HomeScreenActivity.this);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(myPagerAdapter);

        currentFragment = myPagerAdapter.getCurrentFragment();

    }

    public void updateToolBar(Fragment fragment, String title, Boolean back) {

        if (back) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        getSupportActionBar().setTitle(title);
    }


//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.nav_logout) {
//            auth.signOut();
//            finish();
//            Intent intent = new Intent(HomeScreenActivity.this,MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
