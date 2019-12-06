package com.drunk.mode.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


import com.drunk.mode.Fragments.EmergencyFragment;
import com.drunk.mode.Fragments.HomeFragment;
import com.drunk.mode.Fragments.ChatsFragment;
import com.drunk.mode.Fragments.JoinFragment;
import com.drunk.mode.Fragments.MyCircleFragment;

import java.util.ArrayList;


public class MyPagerAdapter extends FragmentPagerAdapter
{

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;
    private Context mCxt;

    public MyPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        mCxt = context;

        fragments.clear();

        HomeFragment homeFragmentFragment = new HomeFragment();
        homeFragmentFragment.setTitle("Home");

        fragments.add(homeFragmentFragment);

        JoinFragment joinFragment = new JoinFragment();
        joinFragment.setTitle("Join Circle");

        fragments.add(joinFragment);


        MyCircleFragment myCircleFragment = new MyCircleFragment();
        myCircleFragment.setTitle("My Group");
        fragments.add(myCircleFragment);

        ChatsFragment chatsFragment = new ChatsFragment();
        chatsFragment.setTitle("Chat with Friends");

        fragments.add(chatsFragment);

        EmergencyFragment emergencyFragment = new EmergencyFragment();
        emergencyFragment.setTitle("Emergency Alerts");
        fragments.add(emergencyFragment);


    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);

    }


    public Fragment getCurrentFragment() {
        return currentFragment;
    }


}
