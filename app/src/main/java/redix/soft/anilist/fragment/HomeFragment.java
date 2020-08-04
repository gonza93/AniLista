package redix.soft.anilist.fragment;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.HomeAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.util.DateUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    private static HomeFragment instance;
    public static final String TAG = "Anilist";
    public enum TYPES { SCHEDULE, AIRING, PEOPLE, POPULAR }
    private int retry;

    @BindView(R.id.home_list) RecyclerView listHome;

    private HomeAdapter homeAdapter;

    public static HomeFragment getInstance() {
        if (instance == null)
            instance = new HomeFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        homeAdapter = new HomeAdapter(getContext());
        listHome.setAdapter(homeAdapter);

        getScheduleAnimes();
        new Handler().postDelayed(this::getAiringAnimes, 500);
        new Handler().postDelayed(this::getPopularPeople, 1000);
        new Handler().postDelayed(this::getPopularAnimes, 1500);

        return view;
    }

    private void getScheduleAnimes(){
        new JikanService()
                .getSchedule(DateUtil.getDayOfWeek())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( response    -> fillList(response.getAnimes(), 10, TYPES.SCHEDULE),
                            throwable   -> new Handler().postDelayed(() -> retry(TYPES.SCHEDULE), 700));
    }

    private void getAiringAnimes(){
        new JikanService()
                .getAiringAnime(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( response    -> fillList(response.getAnimes(), 15, TYPES.AIRING),
                            throwable   -> new Handler().postDelayed(() -> retry(TYPES.AIRING), 700));
    }

    private void getPopularPeople(){
        new JikanService()
                .getPopularPeople(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( response    -> fillList(response.getSeiyus(), 20, TYPES.PEOPLE),
                            throwable   -> new Handler().postDelayed(() -> retry(TYPES.PEOPLE), 700));
    }

    private void getPopularAnimes(){
        new JikanService()
                .getPopulareAnime(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( response    -> fillList(response.getAnimes(), 15, TYPES.POPULAR),
                            throwable   -> new Handler().postDelayed(() -> retry(TYPES.POPULAR), 700));
    }

    private void fillList(List dataset, int limit, TYPES type){
        if(dataset == null)
            return;

        List subList = dataset;
        if(subList.size() > limit)
            subList = subList.subList(0, limit);

        if (type.equals(TYPES.SCHEDULE))
            homeAdapter.setScheduleItems(subList);
        if (type.equals(TYPES.AIRING))
            homeAdapter.setAiringItems(subList);
        if (type.equals(TYPES.PEOPLE))
            homeAdapter.setPeopleItems(subList);
        if (type.equals(TYPES.POPULAR))
            homeAdapter.setPopularItems(subList);
    }

    private synchronized void retry(TYPES type) {
        if (retry == 5) {
            Toast.makeText(getContext(), R.string.fetch_error, Toast.LENGTH_SHORT).show();
            return;
        }
        retry++;

        if (type.equals(TYPES.SCHEDULE))
            getScheduleAnimes();
        if (type.equals(TYPES.AIRING))
            getAiringAnimes();
        if (type.equals(TYPES.PEOPLE))
            getPopularPeople();
        if (type.equals(TYPES.POPULAR))
            getPopularAnimes();
    }

}
