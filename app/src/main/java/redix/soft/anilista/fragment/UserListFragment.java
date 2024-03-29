package redix.soft.anilista.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilista.R;
import redix.soft.anilista.adapter.ListPagerAdapter;

public class UserListFragment extends Fragment {

    public static final String TAG = "User List";

    @BindView(R.id.user_viewpager) ViewPager viewPager;
    @BindView(R.id.user_list_tabs) SmartTabLayout tabLayout;
    private String username;
    private HashMap<String, String> userListParams;

    public void setUsername(String username) { this.username = username; }

    public void clearParams(){
        userListParams.clear();
    }
    public Map<String, String> getParams() { return userListParams; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        ButterKnife.bind(this, view);

        ListPagerAdapter pagerAdapter = new ListPagerAdapter(getContext(), getChildFragmentManager(), username);

        viewPager.setOffscreenPageLimit(3);
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

    public void fetchUserList(String sort) {
        ListPagerAdapter adapter = (ListPagerAdapter) viewPager.getAdapter();
        ListFragment listFragment = (ListFragment) adapter.getItem(viewPager.getCurrentItem());

        listFragment.getParams().put("offset", "0");
        listFragment.getParams().put("sort", sort);
        listFragment.clearUserList();
        listFragment.fetchUserList();
    }
}
