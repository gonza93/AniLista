package redix.soft.anilist.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.AnimeAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.model.Anime;
import redix.soft.anilist.util.DateUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SeasonFragment extends Fragment {

    public static final String TAG = "Seasons";
    private String selectedSeason = "winter";
    private View previousButton;
    private boolean loading;

    @BindView(R.id.season_list) RecyclerView list;
    @BindView(R.id.season_loader) View loader;
    @BindView(R.id.season_winter) View btnWinter;
    @BindView(R.id.season_spring) View btnSpring;
    @BindView(R.id.season_summer) View btnSummer;
    @BindView(R.id.season_autumn) View btnAutumn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_season, container, false);

        ButterKnife.bind(this, view);

        AnimeAdapter adapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_anime);
        list.setAdapter(adapter);

        setSeason();

        return view;
    }

    private void getSeasonAnime(){
        toggleLoad();

        new JikanService()
                .getSeasonAnime(Calendar.getInstance().get(Calendar.YEAR), selectedSeason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            toggleLoad();

                            List<Anime> seasonAnime = response.getAnimes();
                            if(seasonAnime.size() > 40)
                                seasonAnime = seasonAnime.subList(0, 40);
                            ((AnimeAdapter) list.getAdapter()).setDataSet(seasonAnime);
                            },
                        throwable -> Log.d("ERROR", throwable.getMessage())
                );
    }

    private void toggleLoad(){
        loading = !loading;
        loader.setVisibility(loading ? View.VISIBLE : View.GONE);
        list.setVisibility(loading ? View.GONE : View.VISIBLE);
    }

    private void setSeason() {
        selectedSeason = DateUtil.getCurrentSeason();
        if(selectedSeason.equals("winter"))
            previousButton =  btnWinter;
        if(selectedSeason.equals("spring"))
            previousButton = btnSpring;
        if(selectedSeason.equals("summer"))
            previousButton = btnSummer;
        if(selectedSeason.equals("fall"))
            previousButton = btnAutumn;

        toggleButtons(previousButton);
    }

    @OnClick(R.id.season_winter)
    public void onClickWinter(View view){
        toggleButtons(view);
    }

    @OnClick(R.id.season_spring)
    public void onClickSpring(View view){
        toggleButtons(view);
    }

    @OnClick(R.id.season_summer)
    public void onClickSummer(View view){
        toggleButtons(view);
    }

    @OnClick(R.id.season_autumn)
    public void onClickAutumn(View view){
        toggleButtons(view);
    }

    private void toggleButtons(View view){
        if(loading)
            return;

        //Previous
        ((TextView) previousButton.findViewWithTag("text")).setTextColor(Color.GRAY);
        previousButton.setBackgroundResource(R.drawable.bg_rounded);

        //Selected
        TextView selectedTextView = view.findViewWithTag("text");
        selectedTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        view.setBackgroundResource(R.drawable.bg_primary);

        previousButton = view;

        selectedSeason = selectedTextView.getText().toString().toLowerCase();

        getSeasonAnime();
    }
}
