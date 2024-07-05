package redix.soft.anilista.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import redix.soft.anilista.R;
import redix.soft.anilista.adapter.GenreAdapter;
import redix.soft.anilista.dialog.BottomDialogFragment;
import redix.soft.anilista.dialog.InfoDialog;
import redix.soft.anilista.fragment.HomeFragment;
import redix.soft.anilista.fragment.ListFragment;
import redix.soft.anilista.fragment.SearchFragment;
import redix.soft.anilista.fragment.SeiyuFragment;
import redix.soft.anilista.fragment.UserFragment;
import redix.soft.anilista.fragment.UserListFragment;
import redix.soft.anilista.model.Genre;
import redix.soft.anilista.model.Picture;
import redix.soft.anilista.util.AnimationUtil;
import redix.soft.anilista.util.DataUtil;
import redix.soft.anilista.util.NavigationUtil;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private boolean start = true;
    private boolean filtersSet;
    public TextView lastSelectedOrderView;
    public NavigationUtil navigationUtil;

    @BindView(R.id.container) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.appbar) LinearLayout toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.home_logo) View homeLogo;
    @BindView(R.id.search_bar) View searchBar;
    @BindView(R.id.search_bar_input) EditText editSearchInput;
    @BindView(R.id.search_bar_layout) View searchBarLayout;
    @BindView(R.id.back_button) ImageView backbutton;
    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindView(R.id.toolbar_toggles) TextView toggles;
    @BindView(R.id.toolbar_filters) View filters;
    @BindView(R.id.toolbar_save) ImageView saveButton;
    @BindView(R.id.toolbar_active_genre) TextView toolbarGenre;
    @BindView(R.id.toolbar_search) ImageView toolbarSearch;
    @BindView(R.id.toolbar_config_layout) View toolbarConfigLayout;

    @BindView(R.id.filters) View filterDialog;
    @BindView(R.id.filters_scroll) NestedScrollView filters_scroll;
    @BindView(R.id.filters_list_genre) RecyclerView listFiltersGenre;
    @BindView(R.id.filters_score) TextView filterScore;
    @BindView(R.id.filters_score_bar) SeekBar filterScoreBar;
    @BindView(R.id.filters_main_button) TextView filtersText;
    @BindView(R.id.filters_sort_layout) LinearLayout filtersLayoutSort;
    @BindView(R.id.filters_order_layout) LinearLayout filtersLayoutOrder;
    @BindView(R.id.filters_score_layout) View filtersScoreLayout;
    @BindView(R.id.filters_order_layout_parent) View filtersLayoutOrderParent;
    @BindView(R.id.filters_sort_layout_parent) View filtersLayoutSortParent;
    @BindView(R.id.filters_genre_layout) View filtersLayoutGenre;
    @BindView(R.id.filters_clear) View filtersClear;

    public LinearLayout getToolbar() { return toolbar; }
    public TextView getToolbarTitleView() { return toolbarTitle; }
    public View getToolbarConfigLayout() { return toolbarConfigLayout; }
    public View getHomeLogo() { return homeLogo; }
    public ImageView getBackbuttonView() { return backbutton; }
    public TextView getTogglesView() { return toggles; }
    public TextView getToolbarGenre() { return toolbarGenre; }
    public TextView getFilterScore() { return filterScore; }
    public View getToolbarFilters() { return filters; }
    public View getToolbarSearch() { return toolbarSearch; }
    public EditText getEditSearchInput() { return editSearchInput; }
    public ImageView getToolbarSaveButton() { return saveButton; }
    public View getFiltersScoreLayout() { return filtersScoreLayout; }
    public View getFiltersLayoutOrderParent() { return filtersLayoutOrderParent; }
    public LinearLayout getFiltersLayoutOrder() {
        return filtersLayoutOrder;
    }
    public View getFiltersLayoutSortParent() { return filtersLayoutSortParent; }
    public LinearLayout getFiltersLayoutSort() {
        return filtersLayoutSort;
    }
    public View getSearchBarLayout() { return searchBarLayout; }
    public View getSearchBar() { return searchBar; }
    public View getBackButton() { return backbutton; }
    public View getFiltersLayoutGenre() { return filtersLayoutGenre; }
    public View getFiltersClear() { return filtersClear; }

    public void resetFiltersLayout() {
        filtersScoreLayout.setVisibility(View.VISIBLE);
        filtersLayoutOrderParent.setVisibility(View.VISIBLE);
        filtersLayoutSortParent.setVisibility(View.VISIBLE);
        filtersClear.setVisibility(View.VISIBLE);
        filtersLayoutGenre.setVisibility(View.VISIBLE);
        filtersLayoutOrder.findViewWithTag("end_date").setVisibility(View.VISIBLE);
        filtersLayoutOrder.findViewWithTag("type").setVisibility(View.VISIBLE);
        filtersLayoutOrder.findViewWithTag("episodes").setVisibility(View.VISIBLE);
        filtersLayoutOrder.findViewWithTag("update_date").setVisibility(View.GONE);
        getFiltersLayoutSort().setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(DataUtil.getInstance(this).getUIMode());
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        navigationUtil = new NavigationUtil(this);

        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(this);

        navigationUtil.navigateTo(HomeFragment.getInstance(), HomeFragment.TAG, R.id.navigation_home);

        toolbarColor();
        initFilters();
        initSaveButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (DataUtil.getInstance(this).showSupport()) {
            InfoDialog infoDialog = new InfoDialog();
            infoDialog.setLayout(R.layout.dialog_support);

            Bundle args = new Bundle();
            args.putString("text", getString(R.string.support_text));
            args.putBoolean("seiyu", false);
            infoDialog.setArguments(args);

            infoDialog.show(getSupportFragmentManager(), InfoDialog.TAG);
        }
    }

    private void toolbarColor(){
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == navigation.getSelectedItemId())
            return true; //TODO GO TO TOP

        Fragment fragment = navigationUtil.getTopFragmentFromTab(item.getItemId());

        switch (item.getItemId()) {
            case R.id.navigation_home:
                if (fragment == null)
                    fragment = HomeFragment.getInstance();

                navigationUtil.navigateTo(fragment, HomeFragment.TAG, item.getItemId());
                break;
            case R.id.navigation_search:
                if (fragment == null)
                    fragment = SearchFragment.getInstance();

                navigationUtil.navigateTo(fragment, SearchFragment.TAG, item.getItemId());
                break;
            case R.id.navigation_account:
                if (fragment == null)
                    fragment = UserFragment.getInstance();

                navigationUtil.navigateTo(fragment, UserFragment.TAG, item.getItemId());
                break;
        }

        return true;
    }

    public void loadFragment(Fragment fragment, String tag) {
        navigationUtil.navigateTo(fragment, tag, navigation.getSelectedItemId());
    }

    public void loadGallery(ArrayList<Picture> pictures, int selectedPicture){;
        Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
        intent.putExtra("pictures", pictures);
        intent.putExtra("selectedPicture", selectedPicture);
        startActivity(intent);
    }

    public void hideSearchBar(){
        backbutton.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.GONE);
        searchBarLayout.setVisibility(View.GONE);
    }

    public void showSearchBar(){
        backbutton.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.GONE);
        searchBar.setVisibility(View.VISIBLE);
        searchBarLayout.setVisibility(View.VISIBLE);
    }

    @OnTextChanged(value = R.id.search_bar_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChangedSearch(Editable editable) {
        Fragment fragment = navigationUtil.getCurrentPage();
        if (fragment instanceof SearchFragment)
            return;

        String query = editable.toString();

        if (fragment instanceof ListFragment) {
            ListFragment listFragment = ((ListFragment) fragment);
            if (listFragment.getType().equals(ListFragment.TYPES.CHARACTERS))
                listFragment.filterCharacters(query);
        }
        if (fragment instanceof SeiyuFragment){
            SeiyuFragment seiyuFragment = ((SeiyuFragment) fragment);
            seiyuFragment.filterRoles(query);
        }
    }

    @OnEditorAction(value = R.id.search_bar_input)
    public boolean onEnterSearch(int actionId){
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            Fragment fragment = navigationUtil.getCurrentPage();

            if (fragment instanceof SearchFragment) {
                String query = editSearchInput.getText().toString();

                SearchFragment searchFragment = ((SearchFragment) fragment);

                searchFragment.setQuery(query);
                searchFragment.searchAnime();

                DataUtil.getInstance(this).saveString(DataUtil.DATA.QUERY.toString(), query);
            }

            if (fragment instanceof UserFragment){
                UserFragment userFragment = ((UserFragment) fragment);
                userFragment.fetchUser(editSearchInput.getText().toString());
            }

            hideKeyboard();
            return true;
        }
        return false;
    }

    public void showKeyboard() {
        editSearchInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editSearchInput, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editSearchInput.getWindowToken(), 0);
    }

    private void initSaveButton() {
        String username = DataUtil.getInstance(this).getSavedUsername();
        saveButton.setColorFilter(username == null? Color.WHITE : ContextCompat.getColor(this, R.color.colorTurquoise));
    }


    @SuppressLint("SetTextI18n")
    private void initFilters() {
        BottomSheetBehavior.from(findViewById(R.id.filters)).setState(BottomSheetBehavior.STATE_HIDDEN);
        BottomSheetBehavior.from(findViewById(R.id.filters)).addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                float elevation =   newState == BottomSheetBehavior.STATE_EXPANDED ||
                                    newState == BottomSheetBehavior.STATE_DRAGGING ||
                                    newState == BottomSheetBehavior.STATE_COLLAPSED ? 0 : 30;
                 navigation.setElevation(elevation);

                 if (newState == BottomSheetBehavior.STATE_HIDDEN){
                     Fragment fragment = navigationUtil.getCurrentPage();
                     if (fragment instanceof SearchFragment){
                         SearchFragment searchFragment = (SearchFragment) fragment;
                         searchFragment.setListPaddingBottom(0);
                     }
                 } else if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                     Fragment fragment = navigationUtil.getCurrentPage();
                     if (fragment instanceof SearchFragment){
                         SearchFragment searchFragment = (SearchFragment) fragment;
                         searchFragment.setListPaddingBottom(130);
                     }
                 }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        GenreAdapter genreAdapter = new GenreAdapter(Genre.getGenres(), this, R.layout.list_genre_3);
        listFiltersGenre.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listFiltersGenre.setAdapter(genreAdapter);

        filterScoreBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                filterScore.setText(getString(R.string.filters_score_near) + " " + filterScoreBar.getProgress());
            }
        });

        findViewById(R.id.filters_desc).setSelected(true);
    }

    @OnClick(R.id.toolbar_save)
    public void onClickToolbarSave(){
        String username = DataUtil.getInstance(this).getSavedUsername();
        if(username == null) {
            DataUtil.getInstance(this).saveString(DataUtil.DATA.SAVED_USER.toString(), editSearchInput.getText().toString());
            AnimationUtil.changeImageColor(this, saveButton, Color.WHITE, ContextCompat.getColor(this, R.color.colorTurquoise));
            Toast.makeText(this, "User saved as default user", Toast.LENGTH_SHORT).show();
        }
        else {
            DataUtil.getInstance(this).saveString(DataUtil.DATA.SAVED_USER.toString(), null);
            AnimationUtil.fadeIn(editSearchInput);
            AnimationUtil.changeImageColor(this, saveButton, ContextCompat.getColor(this, R.color.colorTurquoise), Color.WHITE);
            Toast.makeText(this, "User removed as default user", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.toolbar_filters)
    public void onClickToolbarFilters(){
        BottomSheetBehavior.from(filterDialog).setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.filters_main_button)
    public void onClickFiltersDrop(){
        BottomSheetBehavior.from(filterDialog).setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.filters_apply)
    public void onClickFiltersApply(){
        List<Genre> filterGenres = ((GenreAdapter) listFiltersGenre.getAdapter()).getItems();

        if (navigationUtil.getCurrentPage() instanceof ListFragment){
            Genre g = new Genre(1, "Action");
            for(Genre genre : filterGenres)
                if (genre.isSelected())
                    g = genre;


            toolbarGenre.setText(g.getName());
            ((ListFragment) navigationUtil.getCurrentPage()).populateGenre(g);
            setFilterState(BottomSheetBehavior.STATE_HIDDEN);
        }
        else if (navigationUtil.getCurrentPage() instanceof SearchFragment){
            SearchFragment fragment = ((SearchFragment) navigationUtil.getCurrentPage());
            Map<String, String> params = fragment.getSearchParams();

            //Genre
            String selectedGenres = "";
            for(Genre genre : filterGenres)
                if (genre.isSelected())
                    selectedGenres += genre.getId() + ",";

            selectedGenres = !selectedGenres.equals("") ? selectedGenres.substring(0, selectedGenres.length() - 1) : "";

            if(!selectedGenres.equals("")) {
                params.put("genre", selectedGenres);
                params.put("genre_exclude", "0");
            }
            else {
                params.put("genre", "12");
                params.put("genre_exclude", "1");
            }

            if (!editSearchInput.getText().toString().isEmpty())
                params.put("q", editSearchInput.getText().toString());
            else
                params.remove("q");

            //Scores
            if(!filterScore.getText().toString().equals(getString(R.string.filters_score)))
                params.put("score", String.valueOf(filterScoreBar.getProgress()));
            else
                params.remove("score");


            if (    !selectedGenres.equals("") ||
                    !filterScore.getText().toString().equals(getString(R.string.filters_score)) ||
                    params.get("order_by") != null
            ){
                fragment.searchAnime();
                fragment.setListPaddingBottom(112);

                filtersSet = true;
                filtersText.setText(getString(R.string.filters_result));
                filtersText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                setFilterState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            else{
                setFilterState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
        else if (navigationUtil.getCurrentPage() instanceof UserListFragment) {
            //Order
            String sort = lastSelectedOrderView.getTag().toString();

            switch (sort.toLowerCase()) {
                case "score":
                    sort = "list_score";
                    break;
                case "update_date":
                    sort = "list_updated_at";
                    break;
                case "title":
                    sort = "anime_title";
                    break;
                case "start_date":
                    sort = "anime_start_date";
                    break;
            }

            //Sort
            //View viewAsc = filtersLayoutSort.getChildAt(0);
            //String sort = viewAsc.isSelected()? "asc" : "desc";

            ((UserListFragment) navigationUtil.getCurrentPage()).fetchUserList(sort);
            setFilterState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else
            setFilterState(BottomSheetBehavior.STATE_COLLAPSED);

        filters_scroll.scrollTo(0, 0);
    }

    public void onClickFiltersOrderBy(View view){

        if (lastSelectedOrderView != null){
            if (lastSelectedOrderView != view) {
                lastSelectedOrderView.setSelected(false);
                lastSelectedOrderView.setBackgroundResource(R.drawable.drawable_genre);
                lastSelectedOrderView.setTextColor(ContextCompat.getColor(this, R.color.colorGrayText));
            }
        }

        view.setSelected(!view.isSelected());
        view.setBackgroundResource(view.isSelected() ? R.drawable.drawable_genre_selected : R.drawable.drawable_genre);
        ((TextView) view).setTextColor(view.isSelected() ?
                ContextCompat.getColor(this, R.color.colorPrimary) :ContextCompat.getColor(this, R.color.colorGrayText));

        lastSelectedOrderView = (TextView) view;

        if (navigationUtil.getCurrentPage() instanceof SearchFragment) {

            SearchFragment fragment = (SearchFragment) navigationUtil.getCurrentPage();
            Map<String, String> params = fragment.getSearchParams();

            params.put("order_by", view.getTag().toString());

            if (!view.isSelected())
                params.remove("order_by");
        }
    }

    public void onClickFiltersSortBy(View view) {
        view.setSelected(true);
        view.setBackgroundResource(R.drawable.drawable_genre_selected);
        ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        View viewAsc = filtersLayoutSort.getChildAt(0);
        View viewDesc = filtersLayoutSort.getChildAt(1);

        if (view.getId() == R.id.filters_desc) {
            viewAsc.setSelected(false);
            viewAsc.setBackgroundResource(R.drawable.drawable_genre);
            ((TextView) viewAsc).setTextColor(ContextCompat.getColor(this, R.color.colorGrayText));
        } else {
            viewDesc.setSelected(false);
            viewDesc.setBackgroundResource(R.drawable.drawable_genre);
            ((TextView) viewDesc).setTextColor(ContextCompat.getColor(this, R.color.colorGrayText));
        }

        if (navigationUtil.getCurrentPage() instanceof SearchFragment) {
            SearchFragment fragment = (SearchFragment) navigationUtil.getCurrentPage();
            Map<String, String> params = fragment.getSearchParams();

            switch (view.getTag().toString()) {
                case "ascending":
                    params.put("sort", "asc");
                    break;
                case "descending":
                    params.put("sort", "desc");
                    break;
            }
        }
    }

    @OnClick(R.id.filters_clear)
    public void onClickFiltersClear(){
        filterScore.setText(R.string.filters_score);
        filterScoreBar.setProgress(0);
        if (lastSelectedOrderView != null) {
            lastSelectedOrderView.setSelected(false);
            lastSelectedOrderView.setBackgroundResource(R.drawable.drawable_genre);
            lastSelectedOrderView.setTextColor(ContextCompat.getColor(this, R.color.colorGrayText));
        }
        setFilterState(BottomSheetBehavior.STATE_HIDDEN);
        filtersSet = false;

        if (navigationUtil.getCurrentPage() instanceof SearchFragment){
            ((GenreAdapter) listFiltersGenre.getAdapter()).clear();
            ((SearchFragment) navigationUtil.getCurrentPage()).clearSearchParams();
            ((SearchFragment) navigationUtil.getCurrentPage()).searchAnime();
        }
    }

    @OnClick(R.id.toolbar_active_genre)
    public void onClickToolbarActiveGenre(){
        setFilterState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void setFilterState(int behaviour){
        BottomSheetBehavior.from(filterDialog).setState(behaviour);
    }

    @OnClick(R.id.toolbar_config)
    public void onClickToolbarConfig() {
        if (navigationUtil.getCurrentPage() instanceof HomeFragment) {
            BottomDialogFragment bottomDialog = new BottomDialogFragment();
            bottomDialog.show(getSupportFragmentManager(), "ConfigDialog");
        }
    }

    @OnClick(R.id.toolbar_support)
    public void onClickToolbarSupport() {
        if (navigationUtil.getCurrentPage() instanceof HomeFragment) {
            String presentationUrl = getString(R.string.support_link);
            Uri uriAuthURL = Uri.parse(presentationUrl);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, uriAuthURL);
            startActivity(browserIntent);
        }
    }

    @OnClick(R.id.toolbar_search)
    public void onClickToolbarSearch() {
        Fragment fragment = navigationUtil.getCurrentPage();

        if (fragment instanceof ListFragment || fragment instanceof SeiyuFragment){
            toolbarSearch.setVisibility(View.INVISIBLE);
            toolbarTitle.setVisibility(View.GONE);
            searchBarLayout.setVisibility(View.VISIBLE);

            if (fragment instanceof SeiyuFragment) {
                SeiyuFragment seiyuFragment = (SeiyuFragment) fragment;
                seiyuFragment.listPerson.smoothScrollToPosition(1);
            }

            AnimationUtil.fadeIn(searchBar);
            showKeyboard();
        }
    }

    @OnClick(R.id.toolbar_toggles)
    public void onClickToolbarEpFilter() {
        Fragment fragment = navigationUtil.getCurrentPage();
        String text = toggles.getText().toString();

        if (text.equals(getString(R.string.toggle_episodes))) {
            toggles.setBackgroundResource(R.drawable.bg_canon);
            toggles.setTextColor(ContextCompat.getColor(this, R.color.colorCanonText));
            toggles.setText(getString(R.string.canon));
            ((ListFragment) fragment).filterEpisodes("canon");
        }
        if (text.equals(getString(R.string.canon))) {
            toggles.setBackgroundResource(R.drawable.bg_filler);
            toggles.setTextColor(ContextCompat.getColor(this, R.color.colorFillerText));
            toggles.setText(getString(R.string.filler));
            ((ListFragment) fragment).filterEpisodes("filler");
        }
        if (text.equals(getString(R.string.filler))) {
            toggles.setBackgroundResource(R.drawable.bg_search);
            toggles.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            toggles.setText(getString(R.string.toggle_episodes));
            ((ListFragment) fragment).filterEpisodes("no");
        }
    }

    @OnClick(R.id.back_button)
    public void onClickBack(View view){
        onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            Fragment fragment = navigationUtil.getCurrentPage();
            UserFragment userFragment = (UserFragment) fragment;

            String authCode = uri.getQueryParameter("code");
            userFragment.authenticateUser(authCode);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = navigationUtil.getCurrentPage();
        boolean searchable = false;

        if (fragment instanceof ListFragment) {
            ListFragment listFragment = (ListFragment) fragment;
            searchable = listFragment.getType().equals(ListFragment.TYPES.CHARACTERS);
        }
        if (fragment instanceof SeiyuFragment)
            searchable = true;

        if (searchable) {
            if (!editSearchInput.getText().toString().equals("")) {
                editSearchInput.getText().clear();
                return;
            } else if (editSearchInput.getText().toString().equals("") && !toolbarSearch.isShown()) {
                toolbarSearch.setVisibility(View.VISIBLE);
                AnimationUtil.fadeOut(searchBarLayout, toolbarTitle);
                return;
            }
        }

        BottomSheetBehavior bst = BottomSheetBehavior.from(findViewById(R.id.filters));

        if (bst.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bst.setState(!filtersSet ? BottomSheetBehavior.STATE_HIDDEN : BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        if(bst.getState() == BottomSheetBehavior.STATE_COLLAPSED){
            bst.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        filters_scroll.scrollTo(0,0);

        Fragment backFragment = navigationUtil.goBack(navigation.getSelectedItemId());

        if(backFragment == null)
            super.onBackPressed();
    }
}
