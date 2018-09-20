package redix.soft.anilist.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.adapter.CharacterAdapter;
import redix.soft.anilist.adapter.GenreAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.databinding.FragmentAnimeBinding;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Character;
import redix.soft.anilist.util.AnimationUtil;
import redix.soft.anilist.util.ChipsLayoutManagerHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AnimeFragment extends Fragment {

    public static final String TAG = "Anime Details";

    @BindView(R.id.anime_progress) View progress;
    @BindView(R.id.characters_progress) View progressCharacters;
    @BindView(R.id.anime_main_layout) View mainLayout;
    @BindView(R.id.anime_genre_list) RecyclerView genreList;
    @BindView(R.id.anime_characters_list) RecyclerView characterList;

    private FragmentAnimeBinding animeBinding;
    private GenreAdapter genreAdapter;
    private CharacterAdapter characterAdapter;

    private int animeId;

    public void setAnimeId(int animeId) {
        this.animeId = animeId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        animeBinding = DataBindingUtil.inflate( inflater,
                                                R.layout.fragment_anime,
                                                container,
                                                false);
        View view = animeBinding.getRoot();

        ButterKnife.bind(this, view);

        //Genres List
        genreAdapter = new GenreAdapter(new ArrayList<>(), getContext());
        genreList.setLayoutManager(ChipsLayoutManagerHelper.build(getContext()));
        genreList.setAdapter(genreAdapter);

        //Characters List
        characterAdapter = new CharacterAdapter(new ArrayList<>(), getContext());
        characterList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        characterList.setAdapter(characterAdapter);

        getAnimeInfo(animeId);

        return view;
    }

    public void getAnimeInfo(int id){
        new JikanService()
                .getAnimeInfo(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(anime -> {
                    this.animeBinding.setAnime(anime);
                    genreAdapter.setDataSet(anime.getGenres());
                    AnimationUtil.collapse(progress);
                    AnimationUtil.fadeIn(mainLayout);
                    getAnimeCharacters(anime);
                });
    }

    public void getAnimeCharacters(Anime anime){
        new JikanService()
                .getAnimeCharacters(anime.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    progressCharacters.setVisibility(View.GONE);
                    List<Character> characters = response.getCharacters();
                    anime.setCharacters(characters);
                    if(characters.size() >= 9)
                        characters = characters.subList(0, 9);
                    characterAdapter.setDataSet(characters);
                });
    }

    @OnClick(R.id.anime_themes)
    public void onClickThemes(){
        loadListFragment(ListFragment.TYPES.THEMES);
    }

    @OnClick(R.id.anime_episodes)
    public void onClickEpisodes(){
        loadListFragment(ListFragment.TYPES.EPISODES);
    }

    private void loadListFragment(ListFragment.TYPES type){
        ListFragment fragment = new ListFragment();
        fragment.setType(type);
        fragment.setAnime(animeBinding.getAnime());

        ((MainActivity) getContext()).loadFragment(fragment, ListFragment.TAG);
    }
}
