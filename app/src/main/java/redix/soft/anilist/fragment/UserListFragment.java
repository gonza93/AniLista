package redix.soft.anilist.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.ListPagerAdapter;

public class UserListFragment extends Fragment {

    public static final String TAG = "User List";

    @BindView(R.id.user_viewpager) ViewPager viewPager;
    @BindView(R.id.user_list_tabs) SmartTabLayout tabLayout;
    private String username;
    public void setUsername(String username) { this.username = username; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        ButterKnife.bind(this, view);

        ListPagerAdapter pagerAdapter = new ListPagerAdapter(getContext(), getChildFragmentManager(), username);

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);

        return view;
    }

    public void fetchUserList(String order, String sort){
        ListPagerAdapter adapter = (ListPagerAdapter) viewPager.getAdapter();
        ListFragment listFragment = (ListFragment) adapter.getItem(viewPager.getCurrentItem());

        listFragment.setNextPage(1);
        listFragment.setOrderBy(order);
        listFragment.setSort(sort);

        listFragment.fetchUserList();
    }
}
