package redix.soft.anilist.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.AnimeAdapter;
import redix.soft.anilist.adapter.SeiyuAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Seiyu;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    private static HomeFragment instance;
    public static final String TAG = "Anilist";

    @BindView(R.id.home_airing_list) RecyclerView airingList;
    @BindView(R.id.home_anime_schedule_list) RecyclerView scheduleList;
    @BindView(R.id.home_popular_list) RecyclerView popularList;
    @BindView(R.id.home_popular_seiyu) RecyclerView popularSeiyuList;

    @BindView(R.id.home_loader_airing) View loaderAiring;
    @BindView(R.id.home_loader_anime_schedule) View loaderSchedule;
    @BindView(R.id.home_loader_popular) View loaderPopular;
    @BindView(R.id.home_loader_seiyu) View loaderSeiyu;

    private AnimeAdapter scheduleAdapter;
    private AnimeAdapter airingAdapter;
    private AnimeAdapter popularAdapter;
    private SeiyuAdapter popularSeiyuAdapter;

    public static HomeFragment getInstance(){
        if(instance == null)
            instance = new HomeFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        ButterKnife.bind(this, view);

        //Schedule animes list
        scheduleAdapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_anime_day);
        scheduleList.setAdapter(scheduleAdapter);

        //Popular animes list
        popularAdapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_airing);
        popularList.setAdapter(popularAdapter);

        //Popular person list
        popularSeiyuAdapter = new SeiyuAdapter(new ArrayList<>(), getContext(), R.layout.list_seiyu);
        popularSeiyuList.setAdapter(popularSeiyuAdapter);

        //Airing animes list
        airingAdapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_airing);
        airingList.setAdapter(airingAdapter);

        getScheduleAnimes();
        new Handler().postDelayed(this::getPopularAnimes, 1000);
        new Handler().postDelayed(this::getAiringAnimes, 2000);
        new Handler().postDelayed(this::getPopularPeople, 3000);

        return view;
    }

    private void getScheduleAnimes(){
        new JikanService()
                .getSchedule(getDayOfWeek())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> fillList(scheduleList.getAdapter(), response.getAnimes(), 10, loaderSchedule));
    }

    private void getAiringAnimes(){
        new JikanService()
                .getAiringAnime(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> fillList(airingAdapter, response.getAnimes(), 15, loaderAiring));
    }

    private void getPopularAnimes(){
        new JikanService()
                .getPopulareAnime(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> fillList(popularAdapter, response.getAnimes(), 15, loaderPopular));
    }

    private void getPopularPeople(){
        new JikanService()
                .getPopularPeople(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> fillList(popularSeiyuAdapter, response.getSeiyus(), 20, loaderSeiyu));
    }

    private void fillList(RecyclerView.Adapter adapter, List dataset, int limit, View loader){
        List subList = dataset;
        if(subList.size() > limit)
            subList = subList.subList(0, limit);

        if(adapter instanceof AnimeAdapter)
            ((AnimeAdapter) adapter).setDataSet(subList);
        else
            ((SeiyuAdapter) adapter).setDataSet(subList);

        loader.setVisibility(View.GONE);
    }

    private String getDayOfWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        return sdf.format(new Date()).toLowerCase();
    }
}
