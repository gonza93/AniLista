package redix.soft.anilista.util;

import android.content.Context;
import android.graphics.Color;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.fragment.AnimeFragment;
import redix.soft.anilista.fragment.HomeFragment;
import redix.soft.anilista.fragment.ListFragment;
import redix.soft.anilista.fragment.SearchFragment;
import redix.soft.anilista.fragment.SeiyuFragment;
import redix.soft.anilista.fragment.UserFragment;
import redix.soft.anilista.fragment.UserListFragment;
import redix.soft.anilista.model.User;

public class NavigationUtil {

    private MainActivity activity;
    private FragmentStack stack;
    private Map<TAB, Stack<Fragment>> fragmentStacks;
    public enum TAB { HOME, SEARCH, ACCOUNT }
    private enum ACTION { OPEN, CLOSE, SWITCH }
    private int lastSelectedTab = -1;
    private int nextSelectTab = R.id.navigation_home;

    public NavigationUtil(MainActivity activity){
        this.activity = activity;

        fragmentStacks = new HashMap<>();
        fragmentStacks.put(TAB.HOME, new Stack<>());
        fragmentStacks.put(TAB.SEARCH, new Stack<>());
        fragmentStacks.put(TAB.ACCOUNT, new Stack<>());

        stack = new FragmentStack();
        stack.setFragmentStacks(fragmentStacks);
    }

    public void navigateTo(Fragment fragment, String tag, int tabId) {
        TAB tab = getSelectedTab(tabId);
        nextSelectTab = tabId;

        changeToolbar(fragment, tag, null);

        if(lastSelectedTab != tabId && lastSelectedTab != -1)
            switchTab(tabId);

        if(fragmentStacks.get(tab).search(fragment) == -1) {
            fragmentTransaction(fragment, tag, ACTION.OPEN, tabId);
            fragmentStacks.get(tab).push(fragment);
        }

        setBackButton();

        lastSelectedTab = tabId;
    }

    public Fragment goBack(int tabId){
        TAB tab = getSelectedTab(tabId);
        if(fragmentStacks.get(tab).size() > 1) {
            Fragment fragmentToRemove = fragmentStacks.get(tab).pop();
            fragmentTransaction(fragmentToRemove, fragmentToRemove.getTag(), ACTION.CLOSE, tabId);
            Fragment fragment = fragmentStacks.get(tab).peek();
            changeToolbar(fragment, fragment.getTag(), fragmentToRemove);
            return fragment;
        }
        else
            return null;
    }

    private void switchTab(int tabId){
        View fragmentToShow = getSelectedFragmentView(tabId);
        View fragmentToHide = getSelectedFragmentView(lastSelectedTab);
        AnimationUtil.smoothSwitch(fragmentToHide, fragmentToShow, isRight(tabId));
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

    private TAB getSelectedTab(int tabId){
        switch (tabId){
            case R.id.navigation_home:
                return TAB.HOME;
            case R.id.navigation_search:
                return TAB.SEARCH;
            default:
                return TAB.ACCOUNT;
        }
    }

    private View getSelectedFragmentView(int tabId){
        switch (tabId){
            case R.id.navigation_home:
                return activity.findViewById(R.id.fragment_container_home);
            case R.id.navigation_search:
                return activity.findViewById(R.id.fragment_container_search);
            default:
                return activity.findViewById(R.id.fragment_container_account);
        }
    }

    private void setBackButton(){
        if (fragmentStacks.get(getSelectedTab(nextSelectTab)).size() > 1)
            activity.getBackbuttonView().setImageResource(R.drawable.ic_back);
    }

    private void changeToolbar(Fragment fragment, String tag, Fragment pastFragment) {
        activity.setFilterState(BottomSheetBehavior.STATE_HIDDEN);
        ImageView backButton = activity.getBackbuttonView();

        activity.getToolbarSearch().setVisibility(View.GONE);
        activity.getToolbarSaveButton().setVisibility(View.GONE);
        activity.getToolbarFilters().setVisibility(View.GONE);

        if (pastFragment != null) {
            if (pastFragment instanceof ListFragment) {
                if (((ListFragment) pastFragment).getType().equals(ListFragment.TYPES.EPISODES)) {
                    activity.getTogglesView().setBackgroundResource(R.drawable.bg_search);
                    activity.getTogglesView().setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                    activity.getTogglesView().setText(activity.getString(R.string.toggle_episodes));
                }
            }
        }

        activity.resetFiltersLayout();

        if (fragment instanceof UserFragment) {
            activity.getFiltersScoreLayout().setVisibility(View.GONE);
            activity.getFiltersLayoutGenre().setVisibility(View.GONE);
            activity.getToolbarTitleView().setVisibility(View.VISIBLE);
            activity.getToolbarSaveButton().setVisibility(View.GONE);
            activity.getSearchBar().setVisibility(View.GONE);
            activity.getSearchBarLayout().setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.GONE);

            activity.getToolbarFilters().setVisibility(View.GONE);
            activity.getToolbarTitleView().setTextColor(Color.WHITE);
            activity.getToolbarTitleView().setVisibility(View.VISIBLE);
        }

        if (fragment instanceof UserFragment) {
            AnimationUtil.changeToolbarColor(activity,
                    ContextCompat.getColor(activity, R.color.colorPrimaryDark),
                    ContextCompat.getColor(activity, R.color.colorToolbarAccount));
        }
        else if (getSelectedTab(lastSelectedTab) == TAB.ACCOUNT) {
                AnimationUtil.changeToolbarColor(activity,
                        ContextCompat.getColor(activity, R.color.colorToolbarAccount),
                        ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                activity.getToolbarTitleView().setTextColor(ContextCompat.getColor(activity, R.color.colorBlack));
        }

        if(tag.equals(SearchFragment.TAG) && fragmentStacks.get(TAB.SEARCH).size() <= 1) {
            activity.getEditSearchInput().setHint(R.string.search_bar_text);
            activity.getToolbarFilters().setVisibility(View.VISIBLE);
            activity.getToolbarSaveButton().setVisibility(View.GONE);
            activity.getEditSearchInput().setText(DataUtil.getInstance(activity).getLastSearch());
            activity.resetFiltersLayout();
            activity.showSearchBar();
        }
        else
            if(!(fragment instanceof UserFragment))
                activity.hideSearchBar();

        activity.getTogglesView().setVisibility(View.GONE);
        activity.getToolbarGenre().setVisibility(View.GONE);

        if(tag.equals(HomeFragment.TAG) && fragmentStacks.get(TAB.HOME).size() > 0) {
            tag = fragmentStacks.get(TAB.HOME).peek().getTag();
            fragment = fragmentStacks.get(TAB.HOME).peek();
            if (fragment instanceof HomeFragment)
                activity.getToolbarConfigLayout().setVisibility(View.VISIBLE);
        }
        else if (!tag.equals(HomeFragment.TAG))
            activity.getToolbarConfigLayout().setVisibility(View.GONE);

        if(tag.equals(SearchFragment.TAG) && fragmentStacks.get(TAB.SEARCH).size() > 0) {
            tag = fragmentStacks.get(TAB.SEARCH).peek().getTag();
            fragment = fragmentStacks.get(TAB.SEARCH).peek();
        }

        String title = tag;

        if(fragment instanceof ListFragment){
            ListFragment listFragment = ((ListFragment) fragment);

            activity.getToolbarFilters().setVisibility(View.VISIBLE);
            activity.getToolbarSearch().setVisibility(View.GONE);

            if (listFragment.getType().equals(ListFragment.TYPES.THEMES))
                activity.getTogglesView().setVisibility(View.VISIBLE);

            if (listFragment.getType().equals(ListFragment.TYPES.GENRE)) {
                activity.getToolbarGenre().setVisibility(View.VISIBLE);
                activity.getFiltersClear().setVisibility(View.GONE);
                activity.getFiltersScoreLayout().setVisibility(View.GONE);
                activity.getFiltersLayoutOrderParent().setVisibility(View.GONE);
                activity.getFiltersLayoutSortParent().setVisibility(View.GONE);
            }

            if (listFragment.getType().equals(ListFragment.TYPES.EPISODES))
                activity.getTogglesView().setVisibility(View.VISIBLE);

            if (listFragment.getType().equals(ListFragment.TYPES.CHARACTERS)) {
                activity.getEditSearchInput().setHint(R.string.search_characters);
                activity.getToolbarSearch().setVisibility(View.VISIBLE);
                activity.getToolbarFilters().setVisibility(View.GONE);
                activity.getSearchBar().setVisibility(View.VISIBLE);
                activity.getSearchBarLayout().setVisibility(View.INVISIBLE);
            }

            title = listFragment.getType().toString().substring(0, 1) +
                    listFragment.getType().toString().substring(1).toLowerCase();

            if (listFragment.getType().equals(ListFragment.TYPES.RELATED))
                title = activity.getString(R.string.viewing_order);
        }

        if(fragment instanceof AnimeFragment)
            activity.getToolbarTitleView().setText(AnimeFragment.TAG);
        if(fragment instanceof UserListFragment) {
            activity.getFiltersScoreLayout().setVisibility(View.GONE);
            activity.getFiltersLayoutGenre().setVisibility(View.GONE);
            activity.getFiltersLayoutOrder().findViewWithTag("end_date").setVisibility(View.GONE);
            activity.getFiltersLayoutOrder().findViewWithTag("type").setVisibility(View.GONE);
            activity.getFiltersLayoutOrder().findViewWithTag("episodes").setVisibility(View.GONE);
            activity.getFiltersLayoutOrder().findViewWithTag("update_date").setVisibility(View.VISIBLE);
            activity.getFiltersLayoutSort().setEnabled(false);
            activity.getToolbarTitleView().setVisibility(View.VISIBLE);
            activity.getToolbarSaveButton().setVisibility(View.GONE);
            activity.getSearchBar().setVisibility(View.VISIBLE);
            activity.getSearchBarLayout().setVisibility(View.GONE);
            activity.getToolbarFilters().setVisibility(View.VISIBLE);
        }
        if(fragment instanceof SeiyuFragment){
            activity.getEditSearchInput().setHint(R.string.search_roles);
            activity.getToolbarSearch().setVisibility(View.VISIBLE);
            activity.getToolbarFilters().setVisibility(View.GONE);
            activity.getSearchBar().setVisibility(View.VISIBLE);
            activity.getSearchBarLayout().setVisibility(View.INVISIBLE);
        }

        activity.getToolbarTitleView().setText(title);

        if(fragment instanceof HomeFragment) {
            activity.getToolbarTitleView().setVisibility(View.GONE);
            activity.getHomeLogo().setVisibility(View.VISIBLE);
            activity.getBackButton().setVisibility(View.GONE);
        }
        else
            activity.getHomeLogo().setVisibility(View.GONE);
    }

    public Fragment getCurrentPage(){
        return fragmentStacks.get(getSelectedTab(lastSelectedTab)).peek();
    }

    public Fragment getTopFragmentFromTab(int tabId){
        TAB tab = getSelectedTab(tabId);
        if (fragmentStacks.get(tab).size() == 0)
            return null;
        else
            return fragmentStacks.get(tab).peek();
    }

    public boolean isRight(int tabId){
        if (lastSelectedTab == R.id.navigation_home)
            return true;
        if (lastSelectedTab == R.id.navigation_account)
            return false;
        if (lastSelectedTab == R.id.navigation_search)
            return tabId == R.id.navigation_account;

        return true;
    }

    public int getLastSelectedTab() {
        return lastSelectedTab;
    }

    public FragmentStack getStack() {
        return stack;
    }
}
