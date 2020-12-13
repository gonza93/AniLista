package redix.soft.anilista.api;

import java.util.Map;

import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.Response;
import redix.soft.anilista.model.ResponseSeiyu;
import redix.soft.anilista.model.Seiyu;
import redix.soft.anilista.model.User;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;

public class JikanService {

    private JikanAPI jikanAPI;
    public static int apiDelay = 2000;

    public JikanService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.jikan.moe/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        jikanAPI = retrofit.create(JikanAPI.class);
    }

    public Single<Response> search(String type, Map<String, String> params){
        return jikanAPI.search(type, params);
    }

    public Single<Anime> getAnimeInfo(int id){
        return jikanAPI.getAnimeInfo(id);
    }

    public Single<Response> getAnimeCharacters(int id){
        return jikanAPI.getAnimeCharacters(id);
    }

    public Single<Response> getAnimeNews(int id){
        return jikanAPI.getAnimeNews(id);
    }

    public Single<Response> getAnimeEpisodes(int id, int page){
        return jikanAPI.getAnimeEpisodes(id, page);
    }

    public Single<Response> getAnimePictures(int id){
        return jikanAPI.getAnimePictures(id);
    }

    public Single<Response> getAnimeRecommendations(int id) {
        return jikanAPI.getAnimeRecommendations(id);
    }

    public Single<Response> getAnimeReviews(int id, int page){
        return jikanAPI.getAnimeReviews(id, page);
    }

    public Single<Response> getAiringAnime(int page){
        return jikanAPI.getTopAnime( page, "airing");
    }

    public Single<Response> getTopAnime(String subtype, int page){
        return jikanAPI.getTopAnime( page, subtype);
    }

    public Single<Response> getPopularAnime(int page){
        return jikanAPI.getTopAnime( page, "bypopularity");
    }

    public Single<ResponseSeiyu> getPopularPeople(int page){
        return jikanAPI.getTopPeople(page);
    }

    public Single<Response> getSchedule(String day){
        return jikanAPI.getSchedule(day);
    }

    public Single<Response> getSeasonAnime(int year, String season){
        return jikanAPI.getSeasonAnime(year, season);
    }

    public Single<Seiyu> getPerson(int id){
        return jikanAPI.getPerson(id);
    }

    public Single<Response> getGenreAnime(int idGenre, int page){
        return jikanAPI.getGenreAnime(idGenre, page);
    }

    public Single<User> getUser(String username){
        return jikanAPI.getUser(username);
    }

    public Single<Response> getUserAnimeList(String username, String filter, int page, String orderBy, String sort){
        return jikanAPI.getUserAnimeList(username, filter, page, orderBy, sort);
    }
}
