package redix.soft.anilista.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.adapter.AnimeAdapter;
import redix.soft.anilista.adapter.CharacterAdapter;
import redix.soft.anilista.adapter.GenreAdapter;
import redix.soft.anilista.adapter.ReviewAdapter;
import redix.soft.anilista.api.JikanService;
import redix.soft.anilista.api.MyAnimeListService;
import redix.soft.anilista.databinding.FragmentAnimeBinding;

import redix.soft.anilista.dialog.BottomDialogFragment;
import redix.soft.anilista.dialog.StatusDialogFragment;
import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.Character;
import redix.soft.anilista.model.Genre;
import redix.soft.anilista.model.Related;
import redix.soft.anilista.model.Relation;
import redix.soft.anilista.model.Review;
import redix.soft.anilista.util.AnimationUtil;
import redix.soft.anilista.util.DataUtil;
import retrofit2.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AnimeFragment extends Fragment {

    public static final String TAG = "Anime Details";

    @BindView(R.id.anime_progress) View progress;
    @BindView(R.id.characters_progress) View progressCharacters;
    @BindView(R.id.anime_progress_status) View progressStatus;
    @BindView(R.id.recommendations_progress) View progressRecom;
    @BindView(R.id.reviews_progress) View progressReview;
    @BindView(R.id.anime_error) View labelError;

    @BindView(R.id.anime_main_layout) View mainLayout;
    @BindView(R.id.anime_nested_scrollview) NestedScrollView scrollView;
    @BindView(R.id.anime_expand_arrow) View expandArrow;
    @BindView(R.id.anime_status_layout) View statusLayout;
    @BindView(R.id.anime_synopsis) ExpandableTextView synopsis;
    @BindView(R.id.anime_reviews_view_all) View buttonAllReviews;

    @BindView(R.id.anime_genre_list) RecyclerView genreList;
    @BindView(R.id.anime_characters_list) RecyclerView characterList;
    @BindView(R.id.anime_adaptation_list) RecyclerView adaptationList;
    @BindView(R.id.anime_prequel_list) RecyclerView prequelList;
    @BindView(R.id.anime_sequel_list) RecyclerView sequelList;
    @BindView(R.id.anime_summary_list) RecyclerView summaryList;
    @BindView(R.id.anime_alternative_list) RecyclerView alternativeList;
    @BindView(R.id.anime_sidestory_list) RecyclerView sidestoryList;
    @BindView(R.id.anime_spinoff_list) RecyclerView spinoffList;
    @BindView(R.id.anime_character_list) RecyclerView relatedCharacterList;
    @BindView(R.id.anime_other_list) RecyclerView otherList;
    @BindView(R.id.anime_recommendations_list) RecyclerView recommendationsList;
    @BindView(R.id.anime_reviews_list) RecyclerView reviewsList;

    @BindView(R.id.anime_adaptation_layout) View adapatationLayout;
    @BindView(R.id.anime_alternative_layout) View alternativeLayout;
    @BindView(R.id.anime_prequel_layout) View prequelLayout;
    @BindView(R.id.anime_sequel_layout) View sequelLayout;
    @BindView(R.id.anime_summary_layout) View summaryLayout;
    @BindView(R.id.anime_sidestory_layout) View sidestoryLayout;
    @BindView(R.id.anime_spinoff_layout) View spinoffLayout;
    @BindView(R.id.anime_character_layout) View relatedCharacterLayout;
    @BindView(R.id.anime_other_layout) View otherLayout;

    @BindView(R.id.anime_reload_character) View reloadCharacterLayout;
    @BindView(R.id.anime_reload_recommendations) View reloadRecommendationsLayout;
    @BindView(R.id.anime_reload_reviews) View reloadReviewsLayout;

    private FragmentAnimeBinding animeBinding;
    private GenreAdapter genreAdapter;
    private CharacterAdapter characterAdapter;
    private AnimeAdapter recommendationsAdapter;
    private ReviewAdapter reviewAdapter;

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

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    ((MainActivity) getContext()).getToolbar().setElevation(scrollY == 0? 0 : 20);
            }
        );

        //Always set elevation 0 on create view
        ((MainActivity) getContext()).getToolbar().setElevation(0);

        //Genres List
        genreAdapter = new GenreAdapter(new ArrayList<>(), getContext(), R.layout.list_genre);
        genreList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        genreList.setAdapter(genreAdapter);

        //Characters List
        characterAdapter = new CharacterAdapter(new ArrayList<>(), getContext());
        characterList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        characterList.setAdapter(characterAdapter);

        //Recommendations List
        recommendationsAdapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_airing);
        recommendationsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendationsList.setAdapter(recommendationsAdapter);

        //Reviews List
        reviewAdapter = new ReviewAdapter(getContext(), new ArrayList<>());
        reviewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewsList.setAdapter(reviewAdapter);

        new Handler().postDelayed(() -> getAnimeInfo(animeId), 500);
        new Handler().postDelayed(() -> getAnimeCharacters(animeId), 1000);
        new Handler().postDelayed(() -> getAnimeRecommendations(animeId), 1500);
        new Handler().postDelayed(() -> getAnimeReviews(animeId), 2000);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getAnimeInfo(int id){
        new JikanService()
                .getAnimeInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            Anime anime = response.getData();

                            this.animeBinding.setAnime(anime);
                            List<Genre> genres = anime.getGenres();
                            if(genres.size() > 5)
                                genres = genres.subList(0, 5);
                            genreAdapter.setItems(genres);

                            setRelatedList();

                            AnimationUtil.translate(progress, 200);
                            AnimationUtil.fadeIn(mainLayout);

                            getAnimeStatus();
                            },
                        throwable -> {
                            Log.d("ERROR", throwable.getMessage());
                            AnimationUtil.collapse(progress, 200, 0);
                            labelError.setVisibility(View.VISIBLE);
                        }
                );
    }

    private void getAnimeStatus() {
        String token = DataUtil.getInstance(getContext()).getString(DataUtil.DATA.TOKEN.toString());
        if (token == null){
            progressStatus.setVisibility(View.GONE);
            statusLayout.setVisibility(View.VISIBLE);
            return;
        }

        new MyAnimeListService(getContext())
                .getAnimeStatus(animeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        anime -> {
                            progressStatus.setVisibility(View.GONE);
                            statusLayout.setVisibility(View.VISIBLE);
                            this.animeBinding.getAnime().setAnimeStatus(anime.getAnimeStatus());
                        },
                        throwable -> {
                            progressStatus.setVisibility(View.GONE);
                            statusLayout.setVisibility(View.VISIBLE);
                        }
                );
    }

    private void getAnimeCharacters(int id){
        progressCharacters.setVisibility(View.VISIBLE);
        reloadCharacterLayout.setVisibility(View.GONE);

        new JikanService()
                .getAnimeCharacters(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            progressCharacters.setVisibility(View.GONE);
                            List<Character> characters = response.getCharacters();
                            characterAdapter.setAllCharacters(new ArrayList<>(characters));
                            if(characters.size() > 9)
                                characters = characters.subList(0, 9);
                            characters.add(null);
                            characterAdapter.setDataSet(characters);
                            reloadCharacterLayout.setVisibility(View.GONE);
                            },
                        throwable -> {
                            Log.d("Anime Characters", throwable.getMessage());
                            progressCharacters.setVisibility(View.GONE);
                            reloadCharacterLayout.setVisibility(View.VISIBLE);
                        }
                );
    }

    private void getAnimeRecommendations(int id){
        progressRecom.setVisibility(View.VISIBLE);
        reloadRecommendationsLayout.setVisibility(View.GONE);

        new JikanService()
                .getAnimeRecommendations(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            List<Anime> animes = response.getAnimes();
                            if(animes.size() > 10)
                                animes = animes.subList(0, 10);
                            recommendationsAdapter.setDataSet(animes);
                            progressRecom.setVisibility(View.GONE);
                            reloadReviewsLayout.setVisibility(View.GONE);
                            },
                        throwable -> {
                            Log.d("ERROR", throwable.getMessage());
                            progressRecom.setVisibility(View.GONE);
                            reloadRecommendationsLayout.setVisibility(View.VISIBLE);
                        }
                );
    }

    private void getAnimeReviews(int animeId) {
        progressReview.setVisibility(View.VISIBLE);
        reloadReviewsLayout.setVisibility(View.GONE);

        new JikanService()
                .getAnimeReviews(animeId, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            List<Review> reviews = response.getReviews();
                            this.animeBinding.getAnime().setReviews(reviews);
                            if(reviews.size() > 5)
                                reviews = reviews.subList(0, 5);
                            reviewAdapter.setDataSet(reviews);
                            progressReview.setVisibility(View.GONE);
                            buttonAllReviews.setVisibility(View.VISIBLE);
                            reloadReviewsLayout.setVisibility(View.GONE);
                        },
                        throwable -> {
                            Log.d("ERROR", throwable.getMessage());
                            progressReview.setVisibility(View.GONE);
                            reloadReviewsLayout.setVisibility(View.VISIBLE);
                        }
                );
    }

    private void setRelatedList() {
        Anime anime = this.animeBinding.getAnime();

        for (Relation relation : anime.getRelations()) {
            AnimeAdapter adapter = new AnimeAdapter(relation.getEntry(), getContext(), R.layout.list_related);

            //Adaptation
            if (relation.getRelation().equalsIgnoreCase("Adaptation")) {
                adaptationList.setAdapter(adapter);
                adapatationLayout.setVisibility(View.VISIBLE);
            }

            //Prequel
            if (relation.getRelation().equalsIgnoreCase("Prequel")) {
                prequelList.setAdapter(adapter);
                prequelLayout.setVisibility(View.VISIBLE);
            }

            //Sequel
            if (relation.getRelation().equalsIgnoreCase("Sequel")) {
                sequelList.setAdapter(adapter);
                sequelLayout.setVisibility(View.VISIBLE);
            }

            //Summary
            if (relation.getRelation().equalsIgnoreCase("Summary")) {
                summaryList.setAdapter(adapter);
                summaryLayout.setVisibility(View.VISIBLE);
            }

            //Alternative Version
            if (relation.getRelation().equalsIgnoreCase("Alternative Version")) {
                alternativeList.setAdapter(adapter);
                alternativeLayout.setVisibility(View.VISIBLE);
            }

            //Side Story
            if (relation.getRelation().equalsIgnoreCase("Side story")) {
                sidestoryList.setAdapter(adapter);
                sidestoryLayout.setVisibility(View.VISIBLE);
            }
            //Spin-Off
            if (relation.getRelation().equalsIgnoreCase("Spin-Off")) {
                spinoffList.setAdapter(adapter);
                spinoffLayout.setVisibility(View.VISIBLE);
            }

            //Character
            if (relation.getRelation().equalsIgnoreCase("Character")) {
                relatedCharacterList.setAdapter(adapter);
                relatedCharacterLayout.setVisibility(View.VISIBLE);
            }

            //Other
            if (relation.getRelation().equalsIgnoreCase("Other")) {
                otherList.setAdapter(adapter);
                otherLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    @OnClick(R.id.anime_status)
    public void onClickStatus() {
        StatusDialogFragment statusDialog = new StatusDialogFragment();
        statusDialog.setAnime(this.animeBinding.getAnime());
        statusDialog.show(getChildFragmentManager(), "StatusDialog");
    }

    @OnClick(R.id.anime_synopsis_layout)
    public void onClickExpand(){
        if(synopsis.isExpanded())
            synopsis.collapse();
        else {
            synopsis.expand();
        }

        AnimationUtil.rotate(expandArrow, 180, 400);
    }

    @OnClick(R.id.anime_themes)
    public void onClickThemes(){
        ThemeFragment fragment = new ThemeFragment();
        fragment.setAnime(animeBinding.getAnime());

        ((MainActivity) getContext()).loadFragment(fragment, ThemeFragment.TAG);
    }

    @OnClick(R.id.anime_episodes)
    public void onClickEpisodes(){
        loadListFragment(ListFragment.TYPES.EPISODES);
    }

    @OnClick(R.id.anime_pictures)
    public void onClickPictures(){
        loadListFragment(ListFragment.TYPES.PICTURES);
    }

    @OnClick(R.id.anime_news)
    public void onClickNews(){
        loadListFragment(ListFragment.TYPES.NEWS);
    }

    @OnClick(R.id.anime_viewing_order)
    public void onClickViewingOrder() {
        loadListFragment(ListFragment.TYPES.RELATED);
    }

    @OnClick(R.id.anime_reviews_view_all)
    public void onClickViewAllReviews(){
        loadListFragment(ListFragment.TYPES.REVIEWS);
    }

    @OnClick(R.id.anime_reload_character)
    public void onClickReloadCharacter(){
        getAnimeCharacters(animeId);
    }

    @OnClick(R.id.anime_reload_recommendations)
    public void onClickReloadRecommendations(){
        getAnimeRecommendations(animeId);
    }

    @OnClick(R.id.anime_reload_reviews)
    public void onClickReloadReviews(){
        getAnimeReviews(animeId);
    }


    private void loadListFragment(ListFragment.TYPES type){
        ListFragment fragment = new ListFragment();
        fragment.setType(type);
        fragment.setAnime(animeBinding.getAnime());

        ((MainActivity) getContext()).loadFragment(fragment, ListFragment.TAG);
    }

}
