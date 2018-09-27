package redix.soft.anilist.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.AnimeAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchFragment extends Fragment {

    private static SearchFragment instance;
    public static final String TAG = "searchFragment";

    @BindView(R.id.search_list) RecyclerView listAnime;
    @BindView(R.id.search_progress) View progress;

    private AnimeAdapter animeAdapter;

    public static SearchFragment getInstance(){
        if(instance == null)
            instance = new SearchFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);

        ButterKnife.bind(this, view);

        animeAdapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_anime);

        listAnime.setLayoutManager(new LinearLayoutManager(getContext()));
        listAnime.setAdapter(animeAdapter);

        searchAnime("Air");

        return view;
    }

    public void searchAnime(String query){
        animeAdapter.clear();
        progress.setVisibility(View.VISIBLE);

        new JikanService()
                .searchAnime(query, 1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    animeAdapter.setDataSet(response.getAnimes());
                    progress.setVisibility(View.GONE);
                });
    }
}
