package redix.soft.anilist.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.adapter.AnimeAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Response;
import redix.soft.anilist.util.DataUtil;
import redix.soft.anilist.util.ElevationRecyclerView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchFragment extends Fragment {

    private static SearchFragment instance;
    public static final String TAG = "searchFragment";

    @BindView(R.id.search_list) RecyclerView listAnime;
    @BindView(R.id.search_progress) View progress;

    private AnimeAdapter animeAdapter;
    private HashMap<String, String> searchParams;

    public void setQuery(String query) {
        searchParams.put("q", query);
    }
    public HashMap<String, String> getSearchParams() {
        return searchParams;
    }
    public void clearSearchParams(){
        String q = searchParams.get("q");
        searchParams.clear();
        searchParams.put("q", q);
        searchAnime();
    }
    public void setListPaddingBottom(int padding){
        this.listAnime.setPadding(0, 0, 0, padding);
    }

    public static SearchFragment getInstance(){
        if(instance == null)
            instance = new SearchFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        animeAdapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_anime);

        listAnime.setLayoutManager(new LinearLayoutManager(getContext()));
        listAnime.setAdapter(animeAdapter);

        searchParams = new HashMap<>();
        searchParams.put("q", "Air");
        searchParams.put("page", "1");
        searchParams.put("limit", "30");

        searchAnime();

        return view;
    }

    public void searchAnime(){
        animeAdapter.clear();
        progress.setVisibility(View.VISIBLE);

        new JikanService()
                .search("anime", searchParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            animeAdapter.setDataSet(response.getAnimes());
                            progress.setVisibility(View.GONE);
                            },
                        throwable -> Log.d("ERROR", throwable.getMessage())
                );
    }
}
