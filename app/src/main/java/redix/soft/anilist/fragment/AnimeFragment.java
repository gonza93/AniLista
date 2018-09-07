package redix.soft.anilist.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.GenreAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.databinding.FragmentAnimeBinding;

import redix.soft.anilist.util.AnimationUtil;
import redix.soft.anilist.util.ChipsLayoutManagerHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AnimeFragment extends Fragment {

    public static final String TAG = "Anime Details";
    public static final String TITLE = "Anime Details";

    @BindView(R.id.anime_progress) View progress;
    @BindView(R.id.anime_main_layout) View mainLayout;
    @BindView(R.id.anime_genre_list) RecyclerView genreList;

    private FragmentAnimeBinding animeBinding;
    private GenreAdapter genreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        animeBinding = DataBindingUtil.inflate( inflater,
                                                R.layout.fragment_anime,
                                                container,
                                                false);
        View view = animeBinding.getRoot();

        ButterKnife.bind(this, view);

        genreAdapter = new GenreAdapter(new ArrayList<>(), getContext());
        genreList.setLayoutManager(ChipsLayoutManagerHelper.build(getContext()));
        genreList.setAdapter(genreAdapter);

        int animeId = getArguments().getInt("id", 1);
        getAnimeInfo(animeId);

        return view;
    }

    public void getAnimeInfo(int id){
        new JikanService().getAnimeInfo(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(anime -> {
                    this.animeBinding.setAnime(anime);
                    genreAdapter.setDataSet(anime.getGenres().subList(0, 4));
                    AnimationUtil.collapse(progress);
                    AnimationUtil.fadeIn(mainLayout);
                });
    }
}
