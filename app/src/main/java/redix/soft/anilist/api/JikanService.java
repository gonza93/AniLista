package redix.soft.anilist.api;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class JikanService {

    private JikanAPI jikanAPI;

    public JikanService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.jikan.moe/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        jikanAPI = retrofit.create(JikanAPI.class);
    }

    public Observable<Response> searchAnime(String query, int page){
        return jikanAPI.searchAnime(query, page);
    }

    public Observable<Anime> getAnimeInfo(int id){
        return jikanAPI.getAnimeInfo(id);
    }

    public Observable<Response> getAnimeCharacters(int id){
        return jikanAPI.getAnimeCharacters(id);
    }

    public Observable<Response> getAnimeNews(int id){
        return jikanAPI.getAnimeNews(id);
    }

    public Observable<Response> getAnimeEpisodes(int id, int page){
        return jikanAPI.getAnimeEpisodes(id, page);
    }

    public Observable<Response> getAnimePictures(int id){
        return jikanAPI.getAnimePictures(id);
    }

    public Observable<Response> getTrendingAnime(int page){
        return jikanAPI.getTrendingAnime(page);
    }
}
