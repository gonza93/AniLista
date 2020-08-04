package redix.soft.anilist.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.adapter.AnimeAdapter;
import redix.soft.anilist.adapter.CharacterAllAdapter;
import redix.soft.anilist.adapter.EpisodeAdapter;
import redix.soft.anilist.adapter.NewsAdapter;
import redix.soft.anilist.adapter.PictureAdapter;
import redix.soft.anilist.adapter.RankingAdapter;
import redix.soft.anilist.adapter.ReviewAdapter;
import redix.soft.anilist.adapter.ThemeAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Character;
import redix.soft.anilist.model.Genre;
import redix.soft.anilist.model.News;
import redix.soft.anilist.model.Theme;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListFragment extends Fragment{

    public enum TYPES { THEMES, EPISODES, PICTURES, NEWS, CHARACTERS, RANKING, UPCOMING, GENRE, USER_LIST, REVIEWS }
    public static final String TAG = "ListFragment";

    private TYPES type;

    @BindView(R.id.list_progress) View progress;
    @Nullable @BindView(R.id.list) RecyclerView list;

    private Anime anime;
    private List<Character> characters;
    private Genre genre;
    private String username;
    private String filter;
    private String orderBy, sort;

    public void setAnime(Anime anime) {
        this.anime = anime;
    }
    public void setCharacters(List<Character> characters) { this.characters = characters; }
    public void setGenre(Genre genre) { this.genre = genre; }
    public void setUser(String username) { this.username = username; }
    public void setNextPage(int nextPage) { this.nextPage = nextPage; }
    public void setFilter(String filter) { this.filter = filter; }
    public void setOrderBy(String orderBy) { this.orderBy = orderBy; }
    public void setSort(String sort) { this.sort = sort; }

    public TYPES getType() {
        return type;
    }
    public void setType(TYPES type) {
        this.type = type;
    }

    private int nextPage = 1;

    private boolean loading;

    public ListFragment withType(TYPES type){
        this.type = type;
        return this;
    }

    public ListFragment withUser(String user){
        this.username = user;
        return this;
    }

    public ListFragment withFilter(String filter){
        this.filter = filter;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ButterKnife.bind(this, view);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        onRestoreState(savedInstanceState);

        if (type.equals(TYPES.THEMES))
            populateThemes();
        if (type.equals(TYPES.EPISODES))
            populateEpisodes();
        if (type.equals(TYPES.PICTURES))
            populatePictures();
        if (type.equals(TYPES.CHARACTERS))
            populateCharacters();
        if (type.equals(TYPES.RANKING))
            populateTop("");
        if (type.equals(TYPES.UPCOMING))
            populateTop("upcoming");
        if (type.equals(TYPES.NEWS))
            populateNews();
        if (type.equals(TYPES.GENRE))
            populateGenre(genre);
        if (type.equals(TYPES.USER_LIST))
            populateUserList();
        if (type.equals(TYPES.REVIEWS))
            populateReviews();

        return view;
    }

    public void onRestoreState(Bundle savedInstanceState){
        if (savedInstanceState != null){
            switch (savedInstanceState.getString("type").toUpperCase()){
                case "THEMES":
                    type = TYPES.THEMES;
                    break;
                case "EPISODES":
                    type = TYPES.EPISODES;
                    break;
                case "PICTURES":
                    type = TYPES.PICTURES;
                    break;
                case "CHARACTERS":
                    type = TYPES.CHARACTERS;
                    break;
                case "RANKING":
                    type = TYPES.RANKING;
                    break;
                case "UPCOMING":
                    type = TYPES.UPCOMING;
                    break;
                case "NEWS":
                    type = TYPES.NEWS;
                    break;
                case "GENRE":
                    type = TYPES.GENRE;
                    break;
                case "USER_LIST":
                    type = TYPES.USER_LIST;
                    break;
            }

            if (anime == null){
                anime = new Anime();
                anime.setId(savedInstanceState.getInt("anime_id"));
                anime.setOpeningThemes(savedInstanceState.getStringArrayList("theme_op"));
                anime.setEndingThemes(savedInstanceState.getStringArrayList("theme_ed"));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("anime_id", anime.getId());
        outState.putString("type", type.toString());
        outState.putStringArrayList("theme_op", (ArrayList<String>) anime.getOpeningThemes());
        outState.putStringArrayList("theme_ed", (ArrayList<String>) anime.getEndingThemes());
    }

    private void populateEpisodes() {
        progress.setVisibility(View.GONE);
        EpisodeAdapter episodeAdapter = new EpisodeAdapter(new ArrayList<>(), getContext());
        list.setAdapter(episodeAdapter);

        loading = true;
        ((EpisodeAdapter) list.getAdapter()).startLoad();

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && nextPage != 0)
                {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = manager.getChildCount();
                    int totalItemCount = manager.getItemCount();
                    int pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;
                            ((EpisodeAdapter) list.getAdapter()).startLoad();
                            fetchEpisodes();
                        }
                    }
                }
            }
        });

        fetchEpisodes();
    }

    private void fetchEpisodes(){
        new JikanService()
                .getAnimeEpisodes(anime.getId(), nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            ((EpisodeAdapter) list.getAdapter()).endLoad();
                            loading = false;

                            ((EpisodeAdapter) list.getAdapter()).addEpisodes(response.getEpisodes());

                            if(response.getEpisodesLastPage() > nextPage)
                                nextPage++;
                            else
                                nextPage = 0;
                            },
                        throwable -> {
                            Log.d("ERROR", throwable.getMessage());
                            loading = false;
                            ((EpisodeAdapter) list.getAdapter()).endLoad();
                        }
                );
    }

    private void populateThemes(){
        List<Theme> themes = new ArrayList<>();

        themes.addAll(parseThemes(anime.getOpeningThemes(), true));
        themes.addAll(parseThemes(anime.getEndingThemes(), false));

        progress.setVisibility(View.GONE);

        ThemeAdapter themeAdapter = new ThemeAdapter(themes, getContext());
        list.setAdapter(themeAdapter);
        ((MainActivity) getContext()).onClickToggleOp(((MainActivity) getContext()).findViewById(R.id.toolbar_toggle_op));
    }

    private void populateCharacters() {
        if (characters == null){
            progress.setVisibility(View.VISIBLE);
            new JikanService()
                    .getAnimeCharacters(anime.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {
                                CharacterAllAdapter characterAdapter = new CharacterAllAdapter(characters, getContext());
                                list.setAdapter(characterAdapter);
                                progress.setVisibility(View.GONE);
                            },
                            throwable -> Log.d("ERROR CHAR RESTORE", throwable.getMessage())
                    );
        }
        else {
            CharacterAllAdapter characterAdapter = new CharacterAllAdapter(characters, getContext());
            list.setAdapter(characterAdapter);
            progress.setVisibility(View.GONE);
        }
    }

    private void populatePictures() {
        PictureAdapter pictureAdapter = new PictureAdapter(new ArrayList<>(), getContext());

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        list.setLayoutManager(gridLayoutManager);
        list.setAdapter(pictureAdapter);

        new JikanService()
                .getAnimePictures(anime.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            ((PictureAdapter) list.getAdapter()).addPictures(response.getPictures());
                            progress.setVisibility(View.GONE);
                            },
                        throwable -> Log.d("ERROR", throwable.getMessage())
                );
    }

    private void populateTop(String subtype){
        progress.setVisibility(View.GONE);
        RankingAdapter adapter = new RankingAdapter(new ArrayList<>(), getContext());
        list.setAdapter(adapter);

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && nextPage != 0)
                {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = manager.getChildCount();
                    int totalItemCount = manager.getItemCount();
                    int pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            fetchTop(subtype);
                        }
                    }
                }
            }
        });

        fetchTop(subtype);
    }

    private void fetchTop(String subtype){
        ((RankingAdapter) list.getAdapter()).startLoad();
        loading = true;
        new JikanService()
                .getTopAnime(subtype, nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            ((RankingAdapter) list.getAdapter()).endLoad();
                            loading = false;

                            ((RankingAdapter) list.getAdapter()).addAnimes(response.getAnimes());

                            nextPage++;
                            },
                        throwable -> {
                            Log.d("ERROR " + subtype, throwable.getMessage());
                            Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
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
        ((ThemeAdapter) list.getAdapter()).toggleThemes(isOpening);
    }

    private void populateNews() {
        NewsAdapter adapter = new NewsAdapter(new ArrayList<>(), getContext());
        list.setAdapter(adapter);

        new JikanService()
                .getAnimeNews(anime.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    progress.setVisibility(View.GONE);
                    List<News> news = response.getArticles();
                    ((NewsAdapter) list.getAdapter()).setDataSet(news);
                });
    }

    public void populateGenre(Genre genre){
        progress.setVisibility(View.VISIBLE);
        ((MainActivity) getContext()).getToolbarGenre().setText(genre.getName());
        AnimeAdapter adapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_anime_genre);
        list.setLayoutManager(new GridLayoutManager(getContext(), 3));
        list.setAdapter(adapter);

        new JikanService()
                .getGenreAnime(genre.getId(), 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    progress.setVisibility(View.GONE);
                    ((AnimeAdapter) list.getAdapter()).setDataSet(response.getAnimes());
                });
    }

    public void populateUserList(){
        progress.setVisibility(View.GONE);
        list.setAdapter(new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_user_list));
        ((MainActivity) getContext()).getToolbar().setElevation(0);
        list.clearOnScrollListeners();

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && nextPage != 0 && !loading)
                {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = manager.getChildCount();
                    int totalItemCount = manager.getItemCount();
                    int pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        fetchUserList();
                }
            }
        });

        fetchUserList();
    }

    public void fetchUserList(){
        loading = true;
        AnimeAdapter adapter = (AnimeAdapter) list.getAdapter();

        if (nextPage == 1)
            adapter.clear();

        adapter.startLoad();

        new JikanService()
                .getUserAnimeList(username, filter, nextPage, orderBy, sort)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            loading = false;
                            progress.setVisibility(View.GONE);
                            ((AnimeAdapter) list.getAdapter()).endLoad();
                            ((AnimeAdapter) list.getAdapter()).addAnime(response.getAnimes());

                            nextPage = response.getAnimes().size() > 0? ++nextPage : 0;
                        },
                        throwable -> {
                            progress.setVisibility(View.GONE);
                            ((AnimeAdapter) list.getAdapter()).endLoad();
                            Log.d("ERROR UserList", throwable.getMessage());
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                );
    }

    public void populateReviews(){
        progress.setVisibility(View.GONE);
        list.setAdapter(new ReviewAdapter(getContext(), new ArrayList<>(anime.getReviews())));
        nextPage = 2;

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && nextPage != 0 && !loading)
                {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = manager.getChildCount();
                    int totalItemCount = manager.getItemCount();
                    int pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        fetchReviews();
                }
            }
        });
    }

    public void fetchReviews(){
        loading = true;
        ((ReviewAdapter) list.getAdapter()).startLoad();

        new JikanService()
                .getAnimeReviews(anime.getId(), nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            ((ReviewAdapter) list.getAdapter()).endLoad();
                            loading = false;

                            ((ReviewAdapter) list.getAdapter()).addReviews(response.getReviews());

                            nextPage = response.getReviews().size() > 0? ++nextPage : 0;
                        },
                        throwable -> {
                            Log.d("ERROR", throwable.getMessage());
                            loading = false;
                            ((ReviewAdapter) list.getAdapter()).endLoad();
                        }
                );
    }

}
