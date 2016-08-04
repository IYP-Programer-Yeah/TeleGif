package ir.telegif.telegif;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by HoseinGhahremanzadeh on 5/9/2016.
 */
public class PagerAdapter  extends FragmentPagerAdapter  {
    private static int NUM_ITEMS = 4;

    public Fragment fragment[]=new Fragment[NUM_ITEMS];

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragment[0]=FirstFragment.newInstance(0);
        fragment[1]=SecondFragment.newInstance(1);
        fragment[2]=ThirdFragment.newInstance(2);
        fragment[3]=ForthFragment.newInstance(3);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment[position];
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
