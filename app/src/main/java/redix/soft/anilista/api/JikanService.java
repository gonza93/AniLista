package redix.soft.anilista.api;

import java.util.Map;

import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.Data;
import redix.soft.anilista.model.ResponseModel;
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
                .baseUrl("https://api.jikan.moe/v4/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        jikanAPI = retrofit.create(JikanAPI.class);
    }

    public Single<ResponseModel> search(Map<String, String> params){
        return jikanAPI.search(params);
    }

    public Single<Data> getAnimeInfo(int id){
        return jikanAPI.getAnimeInfo(id);
    }

    public Single<ResponseModel> getAnimeCharacters(int id){
        return jikanAPI.getAnimeCharacters(id);
    }

    public Single<ResponseModel> getAnimeNews(int id){
        return jikanAPI.getAnimeNews(id);
    }

    public Single<ResponseModel> getAnimeEpisodes(int id, int page){
        return jikanAPI.getAnimeEpisodes(id, page);
    }

    public Single<ResponseModel> getAnimePictures(int id){
        return jikanAPI.getAnimePictures(id);
    }

    public Single<ResponseModel> getAnimeRecommendations(int id) {
        return jikanAPI.getAnimeRecommendations(id);
    }

    public Single<ResponseModel> getAnimeReviews(int id, int page){
        return jikanAPI.getAnimeReviews(id, page);
    }

    public Single<ResponseModel> getTopAnime(int page){
        return jikanAPI.getTopAnime(page);
    }

    public Single<ResponseModel> getTopAnime(String filter, int page){
        return jikanAPI.getTopAnime(page, filter);
    }

    public Single<ResponseSeiyu> getPopularPeople(int page){
        return jikanAPI.getTopPeople(page);
    }

    public Single<ResponseModel> getSchedule(String day){
        return jikanAPI.getSchedule(day);
    }

    public Single<ResponseModel> getSeasonAnime(int year, String season){
        return jikanAPI.getSeasonAnime(year, season);
    }

    public Single<Seiyu> getPerson(int id){
        return jikanAPI.getPerson(id);
    }

    public Single<ResponseModel> getGenreAnime(int idGenre, int page){
        return jikanAPI.getGenreAnime(idGenre, page);
    }

    public Single<User> getUser(String username){
        return jikanAPI.getUser(username);
    }

    public Single<ResponseModel> getUserAnimeList(String username, String filter, int page, String orderBy, String sort){
        return jikanAPI.getUserAnimeList(username, filter, page, orderBy, sort);
    }
}
