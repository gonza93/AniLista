package redix.soft.anilist.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.GenreAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.fragment.AnimeFragment;
import redix.soft.anilist.fragment.HomeFragment;
import redix.soft.anilist.fragment.ListFragment;
import redix.soft.anilist.fragment.SearchFragment;
import redix.soft.anilist.model.Genre;
import redix.soft.anilist.util.ChipsLayoutManagerHelper;
import redix.soft.anilist.util.NavigationUtil;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private boolean start = true;
    private boolean filtersSet;
    public NavigationUtil navigationUtil;

    @BindView(R.id.container) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.appbar) AppBarLayout toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.search_bar) View searchBar;
    @BindView(R.id.search_bar_input) EditText editSearchInput;
    @BindView(R.id.back_button) ImageView backbutton;
    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindView(R.id.toolbar_toggles) View toggles;
    @BindView(R.id.toolbar_filters) View filters;
    @BindView(R.id.toolbar_active_genre) TextView toolbarGenre;

    @BindView(R.id.filters) View filterDialog;
    @BindView(R.id.filters_list_genre) RecyclerView listFiltersGenre;
    @BindView(R.id.filters_score) TextView filterScore;
    @BindView(R.id.filters_score_bar) DiscreteSeekBar filterScoreBar;
    @BindView(R.id.filters_main_button) TextView filtersText;

    public AppBarLayout getToolbar() { return toolbar; }
    public TextView getToolbarTitleView() {
        return toolbarTitle;
    }
    public ImageView getBackbuttonView() {
        return backbutton;
    }
    public View getTogglesView() {
        return toggles;
    }
    public TextView getToolbarGenre() { return toolbarGenre; }
    public TextView getFilterScore() {
        return filterScore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        navigationUtil = new NavigationUtil(this);

        coordinatorLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {

            }
        });

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_home);

        initFilters();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == navigation.getSelectedItemId() && !start)
            return true; //TODO GO TO TOP

        switch (item.getItemId()) {
            case R.id.navigation_home:
                navigationUtil.navigateTo(HomeFragment.getInstance(), HomeFragment.TAG, item.getItemId());
                break;
            case R.id.navigation_search:
                navigationUtil.navigateTo(SearchFragment.getInstance(), SearchFragment.TAG, item.getItemId());
                break;
            case R.id.navigation_account:
                break;
        }

        start = false;

        return true;
    }

    public void loadFragment(Fragment fragment, String tag) {
        navigationUtil.navigateTo(fragment, tag, navigation.getSelectedItemId());
    }

    public void hideSearchBar(){
        backbutton.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.GONE);
        filters.setVisibility(View.GONE);
    }

    public void showSearchBar(){
        backbutton.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.GONE);
        searchBar.setVisibility(View.VISIBLE);
        filters.setVisibility(View.VISIBLE);
    }

    /*@OnTextChanged(value = R.id.search_bar_input,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterSearchInput(Editable editable) {
        if(editable.toString().length() > 2)
            ((SearchFragment) getSupportFragmentManager().findFragmentByTag("searchFragment"))
                    .searchAnime(editable.toString());
    }*/

    @OnEditorAction(value = R.id.search_bar_input)
    public boolean onEnterSearch(int actionId){
        if(actionId == EditorInfo.IME_ACTION_SEARCH) {
            SearchFragment fragment = ((SearchFragment) navigationUtil.getCurrentPage());

            fragment.setQuery(editSearchInput.getText().toString());
            fragment.searchAnime();

            hideKeyboard();
            return true;
        }
        return false;
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editSearchInput.getWindowToken(), 0);
    }

    @OnClick(R.id.toolbar_toggle_op)
    public void onClickToggleOp(View view){
        changeToggle(view);
    }

    @OnClick(R.id.toolbar_toggle_ed)
    public void onClickToggleEd(View view){
        changeToggle(view);
    }

    private void changeToggle(View view){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ListFragment.TAG);

        TextView otherToggle;
        boolean isOpening = false;
        if(view.getId() == R.id.toolbar_toggle_op) {
            otherToggle = toggles.findViewById(R.id.toolbar_toggle_ed);
            isOpening = true;
        }
        else
            otherToggle = toggles.findViewById(R.id.toolbar_toggle_op);

        ((ListFragment) fragment).toggleThemes(isOpening);

        TextView selectedToggle = ((TextView) view);
        selectedToggle.setBackgroundResource(R.drawable.bg_activated);
        selectedToggle.setTextColor(Color.WHITE);

        otherToggle.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        otherToggle.setBackgroundResource(R.drawable.bg_search);
    }

    @SuppressLint("SetTextI18n")
    private void initFilters() {
        BottomSheetBehavior.from(findViewById(R.id.filters)).setState(BottomSheetBehavior.STATE_HIDDEN);
        BottomSheetBehavior.from(findViewById(R.id.filters)).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                float elevation =   newState == BottomSheetBehavior.STATE_EXPANDED ||
                                    newState == BottomSheetBehavior.STATE_DRAGGING ||
                                    newState == BottomSheetBehavior.STATE_COLLAPSED ? 0 : 30;
                 navigation.setElevation(elevation);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        GenreAdapter genreAdapter = new GenreAdapter(Genre.getGenres(), this, R.layout.list_genre_3);
        listFiltersGenre.setLayoutManager(ChipsLayoutManagerHelper.build(this));
        listFiltersGenre.setAdapter(genreAdapter);

        filterScoreBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                filterScore.setText(getString(R.string.filters_score_near) + " " + filterScoreBar.getProgress());
            }
        });
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

            String selectedGenres = "";
            for(Genre genre : filterGenres)
                if (genre.isSelected())
                    selectedGenres += genre.getId() + ",";

            selectedGenres = !selectedGenres.equals("") ? selectedGenres.substring(0, selectedGenres.length() - 1) : "";

            //Genre
            Map<String, String> params = fragment.getSearchParams();
            params.put("q", editSearchInput.getText().toString());
            if(!selectedGenres.equals(""))
                params.put("genre", selectedGenres);
            else
                params.remove("genre");

            //Score
            if(!filterScore.getText().toString().equals(getString(R.string.filters_score)))
                params.put("score", String.valueOf(filterScoreBar.getProgress()));
            else
                params.remove("score");



            if (    !selectedGenres.equals("") ||
                    !filterScore.getText().toString().equals(getString(R.string.filters_score)) ||
                    params.get("order_by") != null){
                fragment.searchAnime();

                filtersSet = true;
                filtersText.setText(getString(R.string.filters_result));
                filtersText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                setFilterState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            else{
                setFilterState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
        else
            setFilterState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void onClickFiltersOrderBy(View view){
        SearchFragment fragment = (SearchFragment) navigationUtil.getCurrentPage();
        Map<String, String> params = fragment.getSearchParams();

        view.setSelected(!view.isSelected());
        view.setBackgroundResource(view.isSelected() ? R.drawable.drawable_genre_selected : R.drawable.drawable_genre);
        ((TextView) view).setTextColor(view.isSelected() ?
                ContextCompat.getColor(this, R.color.colorPrimary) :ContextCompat.getColor(this, R.color.colorGrayText));

        switch (view.getTag().toString()){
            case "title":
                params.put("order_by", "title");
                break;
            case "startdate":
                params.put("order_by", "start_date");
                break;
            case "enddate":
                params.put("order_by", "end_date");
                break;
            case "score":
                params.put("order_by", "score");
                break;
            case "type":
                params.put("order_by", "type");
                break;
            case "episodes":
                params.put("order_by", "episodes");
                break;
        }

        if (!view.isSelected())
            params.remove("order_by");
    }

    @OnClick(R.id.filters_clear)
    public void onClickFiltersClear(){
        if (navigationUtil.getCurrentPage() instanceof SearchFragment){
            filterScore.setText(R.string.filters_score);
            ((GenreAdapter) listFiltersGenre.getAdapter()).clear();
            ((SearchFragment) navigationUtil.getCurrentPage()).clearSearchParams();
            ((SearchFragment) navigationUtil.getCurrentPage()).searchAnime();
            setFilterState(BottomSheetBehavior.STATE_HIDDEN);
            filtersSet = false;
        }
    }

    @OnClick(R.id.toolbar_active_genre)
    public void onClickToolbarActiveGenre(){
        setFilterState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void setFilterState(int behaviour){
        BottomSheetBehavior.from(filterDialog).setState(behaviour);
    }

    @OnClick(R.id.back_button)
    public void onClickBack(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        toggles.setVisibility(View.INVISIBLE);
        BottomSheetBehavior bst = BottomSheetBehavior.from(findViewById(R.id.filters));

        if (bst.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bst.setState(!filtersSet ? BottomSheetBehavior.STATE_HIDDEN : BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        if(bst.getState() == BottomSheetBehavior.STATE_COLLAPSED){
            bst.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }

        Fragment fragment = navigationUtil.goBack(navigation.getSelectedItemId());

        if(fragment == null)
            super.onBackPressed();
    }
}
