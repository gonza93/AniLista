package redix.soft.anilist.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import redix.soft.anilist.R;

public class NavigationUtil {

    private AppCompatActivity activity;
    private Map<TAB, Stack<Fragment>> fragmentStacks;
    public enum TAB { HOME, SEARCH, ACCOUNT }
    private enum ACTION { OPEN, CLOSE, SWITCH }
    private int lastSelectedTab = -1;

    public NavigationUtil(AppCompatActivity activity){
        this.activity = activity;

        fragmentStacks = new HashMap<>();
        fragmentStacks.put(TAB.HOME, new Stack<>());
        fragmentStacks.put(TAB.SEARCH, new Stack<>());
        fragmentStacks.put(TAB.ACCOUNT, new Stack<>());
    }

    public void navigateTo(Fragment fragment, String tag, int tabId){
        TAB tab = getSelectedTab(tabId);

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
}
