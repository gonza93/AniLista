package redix.soft.anilist.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.fragment.AnimeFragment;
import redix.soft.anilist.fragment.HomeFragment;
import redix.soft.anilist.fragment.ListFragment;
import redix.soft.anilist.fragment.SearchFragment;

public class NavigationUtil {

    private MainActivity activity;
    private Map<TAB, Stack<Fragment>> fragmentStacks;
    public enum TAB { HOME, SEARCH, ACCOUNT }
    private enum ACTION { OPEN, CLOSE, SWITCH }
    private int lastSelectedTab = -1;

    public NavigationUtil(MainActivity activity){
        this.activity = activity;

        fragmentStacks = new HashMap<>();
        fragmentStacks.put(TAB.HOME, new Stack<>());
        fragmentStacks.put(TAB.SEARCH, new Stack<>());
        fragmentStacks.put(TAB.ACCOUNT, new Stack<>());
    }

    public void navigateTo(Fragment fragment, String tag, int tabId){
        TAB tab = getSelectedTab(tabId);

        changeToolbar(fragment, tag);

        if(lastSelectedTab != tabId && lastSelectedTab != -1)
            switchTab(tabId);

        if(fragmentStacks.get(tab).search(fragment) == -1) {
            fragmentTransaction(fragment, tag, ACTION.OPEN, tabId);
            fragmentStacks.get(tab).push(fragment);
        }

        lastSelectedTab = tabId;
    }

    public Fragment goBack(int tabId){
        TAB tab = getSelectedTab(tabId);
        if(fragmentStacks.get(tab).size() > 1) {
            Fragment fragment = fragmentStacks.get(tab).pop();
            fragmentTransaction(fragment, fragment.getTag(), ACTION.CLOSE, tabId);
            return fragmentStacks.get(tab).peek();
        }
        else
            return null;
    }

    private void switchTab(int tabId){
        View fragmentToShow = getSelectedFragmentView(tabId);
        View fragmentToHide = getSelectedFragmentView(lastSelectedTab);
        AnimationUtil.switchFragments(fragmentToHide, fragmentToShow, activity);
    }

    private void fragmentTransaction(Fragment fragment, String tag, ACTION action, int tabId){
        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction()
                .setTransition(action.equals(ACTION.OPEN) ? FragmentTransaction.TRANSIT_FRAGMENT_OPEN : FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        if(action.equals(ACTION.OPEN))
            transaction.add(getSelectedFragmentView(tabId).getId(), fragment, tag);

        if(action.equals(ACTION.CLOSE))
            transaction.remove(fragment);

        transaction.commit();
    }

    private NavigationUtil.TAB getSelectedTab(int tabId){
        switch (tabId){
            case R.id.navigation_home:
                return NavigationUtil.TAB.HOME;
            case R.id.navigation_search:
                return NavigationUtil.TAB.SEARCH;
            default:
                return NavigationUtil.TAB.ACCOUNT;
        }
    }

    private View getSelectedFragmentView(int tabId){
        switch (tabId){
            case R.id.navigation_home:
                return activity.findViewById(R.id.fragment_container_home);
            case R.id.navigation_search:
                return activity.findViewById(R.id.fragment_container_search);
            default:
                return activity.findViewById(R.id.fragment_container_home);
        }
    }

    private void changeToolbar(Fragment fragment, String tag) {
        ImageView backbutton = activity.getBackbuttonView();

        if(tag.equals(HomeFragment.TAG) && fragmentStacks.get(TAB.HOME).size() <= 1)
            backbutton.setImageResource(R.mipmap.ic_main);
        else
            backbutton.setImageResource(R.drawable.ic_back);

        if(tag.equals(SearchFragment.TAG) && fragmentStacks.get(TAB.SEARCH).size() <= 1)
            activity.showSearchBar();
        else
            activity.hideSearchBar();

        activity.getTogglesView().setVisibility(View.GONE);

        if(tag.equals(HomeFragment.TAG) && fragmentStacks.get(TAB.HOME).size() > 0) {
            tag = fragmentStacks.get(TAB.HOME).peek().getTag();
            fragment = fragmentStacks.get(TAB.HOME).peek();
        }
        if(tag.equals(SearchFragment.TAG) && fragmentStacks.get(TAB.SEARCH).size() > 0) {
            tag = fragmentStacks.get(TAB.SEARCH).peek().getTag();
            fragment = fragmentStacks.get(TAB.SEARCH).peek();
        }

        String title = tag;


        if(tag.equals(ListFragment.TAG)){
            ListFragment listFragment = ((ListFragment) fragment);

            if (listFragment.getType().equals(ListFragment.TYPES.THEMES))
                activity.getTogglesView().setVisibility(View.VISIBLE);

            title = listFragment.getType().toString().substring(0, 1) +
                    listFragment.getType().toString().substring(1).toLowerCase();
        }

        activity.getToolbarTitleView().setText(title);
    }
}
