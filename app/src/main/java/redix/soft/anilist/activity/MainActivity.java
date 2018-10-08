package redix.soft.anilist.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import redix.soft.anilist.R;
import redix.soft.anilist.fragment.AnimeFragment;
import redix.soft.anilist.fragment.HomeFragment;
import redix.soft.anilist.fragment.ListFragment;
import redix.soft.anilist.fragment.SearchFragment;
import redix.soft.anilist.util.NavigationUtil;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private boolean start = true;
    private NavigationUtil navigationUtil;

    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.search_bar) View searchBar;
    @BindView(R.id.search_bar_input) EditText editSearchInput;
    @BindView(R.id.back_button) ImageView backbutton;
    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindView(R.id.toolbar_toggles) View toggles;

    public TextView getToolbarTitleView() {
        return toolbarTitle;
    }

    public ImageView getBackbuttonView() {
        return backbutton;
    }

    public View getTogglesView() {
        return toggles;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        navigationUtil = new NavigationUtil(this);

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_home);
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
    }

    public void showSearchBar(){
        backbutton.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.GONE);
        searchBar.setVisibility(View.VISIBLE);
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
            ((SearchFragment) getSupportFragmentManager().findFragmentByTag("searchFragment"))
                    .searchAnime(editSearchInput.getText().toString());
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

    @OnClick(R.id.back_button)
    public void onClickBack(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        toggles.setVisibility(View.INVISIBLE);

        Fragment fragment = navigationUtil.goBack(navigation.getSelectedItemId());

        if(fragment == null){
            super.onBackPressed();
            return;
        }

        if(fragment instanceof SearchFragment)
            showSearchBar();
        if(fragment instanceof HomeFragment) {
            backbutton.setImageResource(R.mipmap.ic_main);
            toolbarTitle.setText("Anilist");
        }
        if(fragment instanceof AnimeFragment)
            toolbarTitle.setText(AnimeFragment.TAG);
    }
}
