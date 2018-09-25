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

import com.elyeproj.loaderviewlibrary.LoaderImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.AnimeAdapter;
import redix.soft.anilist.api.JikanService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    public static final String TAG = "Anilist";

    @BindView(R.id.home_trending_list) RecyclerView trendingList;
    @BindView(R.id.home_loader_trending) View loaderTrending;

    private AnimeAdapter trendingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        ButterKnife.bind(this, view);

        //Trending animes list
        trendingAdapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_trending);
        trendingList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        trendingList.setAdapter(trendingAdapter);

        getTrendingAnimes();

        return view;
    }

    private void getTrendingAnimes(){
        new JikanService()
                .getTrendingAnime(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    trendingAdapter.setDataSet(response.getAnimes().subList(0, 15));
                    loaderTrending.setVisibility(View.GONE);
                });
    }
}
