package redix.soft.anilist.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.fragment.ListFragment;

public class ListPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String username;
    private List<Fragment> fragments;

    public ListPagerAdapter(Context context, FragmentManager fm, String username) {
        super(fm);
        mContext = context;
        this.username = username;
        this.fragments = new ArrayList<>();

        fragments.add(new ListFragment().withType(ListFragment.TYPES.USER_LIST).withUser(username).withFilter("all"));
        fragments.add(new ListFragment().withType(ListFragment.TYPES.USER_LIST).withUser(username).withFilter("watching"));
        fragments.add(new ListFragment().withType(ListFragment.TYPES.USER_LIST).withUser(username).withFilter("completed"));
        fragments.add(new ListFragment().withType(ListFragment.TYPES.USER_LIST).withUser(username).withFilter("onhold"));
        fragments.add(new ListFragment().withType(ListFragment.TYPES.USER_LIST).withUser(username).withFilter("dropped"));
        fragments.add(new ListFragment().withType(ListFragment.TYPES.USER_LIST).withUser(username).withFilter("plantowatch"));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.all);
            case 1:
                return mContext.getString(R.string.watching);
            case 2:
                return mContext.getString(R.string.completed);
            case 3:
                return mContext.getString(R.string.on_hold);
            case 4:
                return mContext.getString(R.string.dropped);
            default:
                return mContext.getString(R.string.plan_to_watch);
        }
    }

}
