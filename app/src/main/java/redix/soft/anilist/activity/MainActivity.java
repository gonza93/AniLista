package redix.soft.anilist.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.BottomBarAdapter;
import redix.soft.anilist.fragment.HomeFragment;
import redix.soft.anilist.fragment.SearchFragment;
import redix.soft.anilist.util.NoSwipePager;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {

    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.search_bar) View searchBar;
    @BindView(R.id.search_bar_input) EditText editSearchInput;
    @BindView(R.id.back_button) View backbutton;
    @BindView(R.id.navigation) BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                loadFragment(new HomeFragment(), HomeFragment.TAG);
                break;
            case R.id.navigation_search:
                loadFragment(new SearchFragment(), SearchFragment.TAG);
                break;
            case R.id.navigation_account:
                break;
        }

        return true;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {

    }

    public void loadFragment(Fragment fragment, String tag) {
        if(tag.equals(HomeFragment.TAG))
            backbutton.setVisibility(View.GONE);
        else
            backbutton.setVisibility(View.VISIBLE);

        if(tag.equals(SearchFragment.TAG))
            showSearchBar();
        else
            hideSearchBar();

        toolbarTitle.setText(tag);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if(tag.equals(HomeFragment.TAG) || tag.equals(SearchFragment.TAG))
            transaction.replace(R.id.fragment_container, fragment, tag);
        else {
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            transaction.add(R.id.fragment_container, fragment, tag);
            transaction.addToBackStack(tag);
        }

        transaction.commit();
    }

    private void hideSearchBar(){
        toolbarTitle.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.GONE);
    }

    private void showSearchBar(){
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

    @OnClick(R.id.back_button)
    public void onClickBack(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();

        /*if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof SearchFragment) {
            showSearchBar();
        }*/
    }
}
