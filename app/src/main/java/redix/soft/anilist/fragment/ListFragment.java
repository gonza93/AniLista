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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.adapter.CharacterAllAdapter;
import redix.soft.anilist.adapter.EpisodeAdapter;
import redix.soft.anilist.adapter.ThemeAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Character;
import redix.soft.anilist.model.Theme;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListFragment extends Fragment{

    public enum TYPES { THEMES, EPISODES, PICTURES, CHARACTERS }
    public static final String TAG = "ListFragment";

    private TYPES type;

    @BindView(R.id.list_progress) View progress;
    @BindView(R.id.list) RecyclerView list;
    private ThemeAdapter themeAdapter;
    private EpisodeAdapter episodeAdapter;
    private CharacterAllAdapter characterAdapter;

    private Anime anime;
    private List<Character> characters;

    public void setAnime(Anime anime) {
        this.anime = anime;
    }
    public void setCharacters(List<Character> characters) { this.characters = characters; }

    public TYPES getType() {
        return type;
    }
    public void setType(TYPES type) {
        this.type = type;
    }

    private int nextPage = 1;

    private boolean loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);

        ButterKnife.bind(this, view);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        if (type.equals(TYPES.THEMES))
            populateThemes();
        if (type.equals(TYPES.EPISODES)) {
            progress.setVisibility(View.GONE);
            episodeAdapter = new EpisodeAdapter(new ArrayList<>(), getContext());
            list.setAdapter(episodeAdapter);
            populateEpisodes();

            list.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0 && nextPage != 0) //check for scroll down
                    {
                        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        int visibleItemCount = manager.getChildCount();
                        int totalItemCount = manager.getItemCount();
                        int pastVisiblesItems = manager.findFirstVisibleItemPosition();

                        if (!loading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                populateEpisodes();
                            }
                        }
                    }
                }
            });
        }
        if (type.equals(TYPES.PICTURES))
            populatePictures();
        if (type.equals(TYPES.CHARACTERS)){
            populateCharacters();
        }


        return view;
    }

    private void populateEpisodes() {
        episodeAdapter.startLoad();
        loading = true;
        new JikanService()
                .getAnimeEpisodes(anime.getId(), nextPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    episodeAdapter.endLoad();
                    loading = false;

                    episodeAdapter.addEpisodes(response.getEpisodes());

                    if(response.getEpisodesLastPage() > nextPage)
                        nextPage++;
                    else
                        nextPage = 0;
                });
    }

    private void populateThemes(){
        List<Theme> themes = new ArrayList<>();

        themes.addAll(parseThemes(anime.getOpeningThemes(), true));
        themes.addAll(parseThemes(anime.getEndingThemes(), false));

        progress.setVisibility(View.GONE);

        themeAdapter = new ThemeAdapter(themes, getContext());
        ((MainActivity) getContext()).onClickToggleOp(((MainActivity) getContext()).findViewById(R.id.toolbar_toggle_op));
        list.setAdapter(themeAdapter);
    }

    private void populateCharacters() {
        characterAdapter = new CharacterAllAdapter(characters, getContext());
        list.setAdapter(characterAdapter);
        progress.setVisibility(View.GONE);
    }

    private void populatePictures() {

    }

    private List<Theme> parseThemes(List<String> themes, boolean isOpening){
        List<Theme> parsedThemes = new ArrayList<>();
        int order = 1;
        for(String line : themes) {

            Theme theme = new Theme();
            theme.setOpening(isOpening);

            theme.setOrder(String.valueOf(order++));
            int titleIndex = line.lastIndexOf("\"") + 1;

            String title = line.substring(0, titleIndex);
            theme.setTitle(title.replace("\"", ""));
            line = line.replace(title, "").substring(1);

            String author;
            if (line.lastIndexOf("(") == -1) {
                theme.setAuthor(line);
            }
            else {
                author = line.substring(0, line.lastIndexOf("(") - 1);
                theme.setAuthor(author);

                line = line.replace(theme.getAuthor(), "").substring(1);
                theme.setEpisodes(line);
            }

            parsedThemes.add(theme);
        }

        return parsedThemes;
    }

    public void toggleThemes(boolean isOpening){
        themeAdapter.toggleThemes(isOpening);
    }

}
