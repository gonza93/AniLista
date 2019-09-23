package redix.soft.anilist.api;

import java.util.Map;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Response;
import redix.soft.anilist.model.ResponseSeiyu;
import redix.soft.anilist.model.Seiyu;
import redix.soft.anilist.model.User;
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

    public Single<Response> search(String type, String query, int page, int limit){
        return jikanAPI.search(type, query, page, limit);
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

    public Single<Response> getAiringAnime(int page){
        return jikanAPI.getTopAnime( page, "airing");
    }

    public Single<Response> getTopAnime(String subtype, int page){
        return jikanAPI.getTopAnime( page, subtype);
    }

    public Single<Response> getFavoriteAnime(int page){
        return jikanAPI.getTopAnime( page, "favorite");
    }

    public Single<Response> getPopulareAnime(int page){
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
