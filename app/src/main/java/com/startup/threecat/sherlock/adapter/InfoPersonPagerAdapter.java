package com.startup.threecat.sherlock.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.startup.threecat.sherlock.fragment.InfoDetailPersonFragment;
import com.startup.threecat.sherlock.fragment.MovementFragment;
import com.startup.threecat.sherlock.model.Person;

/**
 * Created by Dell on 20-Jul-16.
 */
public class InfoPersonPagerAdapter extends FragmentPagerAdapter {

    private Person person;
    public static final int NUM_ITEMS = 2;
    public static final int MOVEMENT_LIST_POSITION = 0;
    public static final int INFO_PERSON_POSITION = 1;
    public static final String [] TAB_TITLE = {"MOVEMENT LIST", "PERSON INFORMATION"};
    private MovementFragment movementFragment;

    public InfoPersonPagerAdapter(FragmentManager fm, Person person) {
        super(fm);
        this.person = person;
        movementFragment = null;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case MOVEMENT_LIST_POSITION :
                movementFragment = MovementFragment.newInstance(person);
                return movementFragment;
            case INFO_PERSON_POSITION :
                return InfoDetailPersonFragment.newInstance(person);

        }
       return null;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLE[position];
    }

    public MovementFragment getMovementFragment() {
        return movementFragment;
    }
}
