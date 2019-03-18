package redix.soft.anilist.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.AnimeAdapter;
import redix.soft.anilist.api.JikanService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    private static HomeFragment instance;
    public static final String TAG = "Anilist";

    @BindView(R.id.home_airing_list) RecyclerView airingList;
    @BindView(R.id.home_anime_schedule_list) RecyclerView scheduleList;
    @BindView(R.id.home_loader_airing) View loaderTrending;
    @BindView(R.id.home_loader_anime_schedule) View loaderSchedule;

    private AnimeAdapter scheduleAdapter;
    private AnimeAdapter airingAdapter;

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
        scheduleList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        scheduleList.setAdapter(scheduleAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(scheduleList);

        //Airing animes list
        airingAdapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_airing);
        airingList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        airingList.setAdapter(airingAdapter);

        getScheduleAnimes();
        getTrendingAnimes();

        return view;
    }

    private void getScheduleAnimes(){
        new JikanService()
                .getSchedule(getDayOfWeek())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    scheduleAdapter.setDataSet(response.getAnimes().subList(0, 10));
                    loaderSchedule.setVisibility(View.GONE);
                });
    }

    private void getTrendingAnimes(){
        new JikanService()
                .getAiringAnime(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    airingAdapter.setDataSet(response.getAnimes().subList(0, 15));
                    loaderTrending.setVisibility(View.GONE);
                });
    }

    private String getDayOfWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        return sdf.format(new Date()).toLowerCase();
    }
}
