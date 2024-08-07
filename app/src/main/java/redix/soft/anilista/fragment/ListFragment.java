package redix.soft.anilista.fragment;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.adapter.AnimeAdapter;
import redix.soft.anilista.adapter.CharacterAllAdapter;
import redix.soft.anilista.adapter.EpisodeAdapter;
import redix.soft.anilista.adapter.NewsAdapter;
import redix.soft.anilista.adapter.PictureAdapter;
import redix.soft.anilista.adapter.RankingAdapter;
import redix.soft.anilista.adapter.ReviewAdapter;
import redix.soft.anilista.adapter.SongAdapter;
import redix.soft.anilista.adapter.UserAnimeListAdapter;
import redix.soft.anilista.api.JikanService;
import redix.soft.anilista.api.MyAnimeListService;
import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.Character;
import redix.soft.anilista.model.Genre;
import redix.soft.anilista.model.News;
import redix.soft.anilista.model.Song;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListFragment extends Fragment{

    public enum TYPES { THEMES, EPISODES, PICTURES, NEWS, CHARACTERS, RANKING, UPCOMING, GENRE, USER_LIST, REVIEWS, RELATED }
    public static final String TAG = "ListFragment";

    private TYPES type;

    @BindView(R.id.list_progress) View progress;
    @BindView(R.id.list) RecyclerView list;

    private Anime anime;
    private List<Character> characters, charactersBuffer;
    private Genre genre;
    private String username;
    private String filter;
    private String orderBy, sort;
    private Map<String, String> params;

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
    public void setThemeOp(boolean isOpening){ isThemeOp = isOpening; }
    public void setParams(Map<String, String> params) { this.params = params; }
    public Map<String, String> getParams() {
        return params;
    }

    public TYPES getType() {
        return type;
    }
    public void setType(TYPES type) {
        this.type = type;
    }

    private int nextPage = 1;

    private boolean isThemeOp;
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

        if (type.equals(TYPES.THEMES))
            populateThemes();
        if (type.equals(TYPES.EPISODES))
            populateEpisodes();
        if (type.equals(TYPES.PICTURES))
            populatePictures();
        if (type.equals(TYPES.CHARACTERS))
            populateCharacters();
        if (type.equals(TYPES.RANKING))
            populateTop(null);
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
        if (type.equals(TYPES.RELATED))
            populateViewingOrder();

        return view;
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

    public void filterEpisodes(String filter) {
        ((EpisodeAdapter) list.getAdapter()).filterEpisodes(filter);
    }

    private void populateThemes(){
        List<Song> songs = new ArrayList<>();

        if (isThemeOp)
            songs.addAll(parseThemes(anime.getTheme().getOpenings(), true));
        else
            songs.addAll(parseThemes(anime.getTheme().getEndings(), false));

        progress.setVisibility(View.GONE);

        SongAdapter songAdapter = new SongAdapter(songs, getContext());
        list.setAdapter(songAdapter);
    }

    private void populateCharacters() {
        charactersBuffer = new ArrayList<>();
        charactersBuffer.addAll(characters);

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

    public void filterCharacters(String query) {
        if (query.length() == 0) {
            characters.clear();
            characters.addAll(charactersBuffer);
            return;
        }
        if (query.length() < 3)
            return;

        characters.clear();
        query = query.toLowerCase();
        for (Character character : charactersBuffer)
            if (character.getName().toLowerCase().contains(query))
                characters.add(character);

        if (list.getAdapter() != null)
            list.getAdapter().notifyDataSetChanged();
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

        if (subtype == null) {
            new JikanService()
                    .getTopAnime(nextPage)
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
                                Log.d("ERROR RANKKING", throwable.getMessage());
                                Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                    );
        }
        else {
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
    }

    private List<Song> parseThemes(List<String> themes, boolean isOpening){
        List<Song> parsedSongs = new ArrayList<>();
        int order = 1;
        for(String line : themes) {

            Song song = new Song();
            song.setOpening(isOpening);

            song.setOrder(String.valueOf(order++));
            int titleIndex = line.lastIndexOf("\"") + 1;

            String title = line.substring(0, titleIndex);
            song.setTitle(title.replace("\"", ""));
            line = line.replace(title, "").substring(1);

            String author;
            if (line.lastIndexOf("(") == -1) {
                song.setAuthor(line);
            }
            else {
                author = line.substring(0, line.lastIndexOf("(") - 1);
                song.setAuthor(author);

                line = line.replace(song.getAuthor(), "").substring(1);
                song.setEpisodes(line);
            }

            parsedSongs.add(song);
        }

        return parsedSongs;
    }

    private void populateNews() {
        NewsAdapter adapter = new NewsAdapter(new ArrayList<>(), getContext());
        list.setAdapter(adapter);

        new JikanService()
                .getAnimeNews(anime.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            progress.setVisibility(View.GONE);
                            List<News> news = response.getArticles();
                            ((NewsAdapter) list.getAdapter()).setDataSet(news);
                        },
                        throwable -> {
                            progress.setVisibility(View.GONE);
                            Log.d("ERROR NEWS", throwable.getMessage());
                            Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                .subscribe(
                        response -> {
                            progress.setVisibility(View.GONE);
                            ((AnimeAdapter) list.getAdapter()).setDataSet(response.getAnimes());
                        },
                        throwable -> {
                            progress.setVisibility(View.GONE);
                            Log.d("ERROR GENRE", throwable.getMessage());
                            Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
    }

    public void populateUserList(){
        progress.setVisibility(View.GONE);
        list.setAdapter(new UserAnimeListAdapter(new ArrayList<>(), getContext(), R.layout.list_user_list));
        ((MainActivity) getContext()).getToolbar().setElevation(0);
        list.clearOnScrollListeners();

        nextPage = 0;
        params = new HashMap<>();
        params.put("fields", "list_status");
        params.put("limit", "100");
        if (!filter.equals("all"))
            params.put("status", filter);

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && nextPage != -1 && !loading)
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
        UserAnimeListAdapter adapter = (UserAnimeListAdapter) list.getAdapter();

        if (nextPage == -1)
            return;

        adapter.startLoad();

        /*new JikanService()
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
                );*/

        new MyAnimeListService(getContext())
                .getUserAnimeList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            loading = false;
                            progress.setVisibility(View.GONE);

                            ((UserAnimeListAdapter) list.getAdapter()).endLoad();
                            ((UserAnimeListAdapter) list.getAdapter()).addAnime(response.getData());

                            nextPage = response.getData().size() > 0 ? nextPage + 100 : -1;
                            params.put("offset", String.valueOf(nextPage));
                        },
                        throwable -> {
                            progress.setVisibility(View.GONE);
                            ((UserAnimeListAdapter) list.getAdapter()).endLoad();
                            Log.d("ERROR UserList", throwable.getMessage());
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                );
    }

    public void clearUserList() {
        nextPage = 0;
        ((UserAnimeListAdapter) list.getAdapter()).clear();
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

    private void populateViewingOrder() {
        progress.setVisibility(View.GONE);
        anime.setSelected(true);

        List<Anime> relatedAnime = new ArrayList<>();
        relatedAnime.add(anime);

        AnimeAdapter adapter = new AnimeAdapter(relatedAnime, getContext(), R.layout.list_related_order);
        list.setAdapter(adapter);

        if (anime.getRelated().getSequel() != null)
            getSequels(anime.getRelated().getSequel().get(0).getId());
        else if (anime.getRelated().getPrequel() != null)
            getPrequels(anime.getRelated().getPrequel().get(0).getId());
    }

    private void getSequels(int animeId) {
        ((AnimeAdapter) list.getAdapter()).startLoad();

        new JikanService()
                .getAnimeInfo(animeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            ((AnimeAdapter) list.getAdapter()).endLoad();
                            ((AnimeAdapter) list.getAdapter()).addAnime(response.getData());

                            if (response.getData().getRelated().getSequel() != null)
                                getSequels(response.getData().getRelated().getSequel().get(0).getId());
                            else if (anime.getRelated().getPrequel() != null)
                                getPrequels(anime.getRelated().getPrequel().get(0).getId());
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            loading = false;
                        }
                );
    }

    private void getPrequels(int animeId) {
        ((AnimeAdapter) list.getAdapter()).startLoadInverse();

        new JikanService()
                .getAnimeInfo(animeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            ((AnimeAdapter) list.getAdapter()).endLoadInverse();
                            ((AnimeAdapter) list.getAdapter()).putAnime(response.getData());

                            if (response.getData().getRelated().getPrequel() != null)
                                getPrequels(response.getData().getRelated().getPrequel().get(0).getId());
                        },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            loading = false;
                        }
                );
    }
}
