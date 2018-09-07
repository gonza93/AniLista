package redix.soft.anilist.api;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class JikanService {

    private static final String BASE_URL = "https://api.jikan.moe/v3/";
    private JikanAPI jikanAPI;

    public JikanService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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

}
