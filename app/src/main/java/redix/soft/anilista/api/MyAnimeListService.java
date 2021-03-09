package redix.soft.anilista.api;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.AnimeStatus;
import redix.soft.anilista.model.User;
import redix.soft.anilista.util.DataUtil;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;

public class MyAnimeListService {

    private MyAnimeListAPI myAnimeListAPI;
    public static int apiDelay = 2000;

    public MyAnimeListService(Context context){
        String token = DataUtil.getInstance(context).getString(DataUtil.DATA.TOKEN.toString());

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.myanimelist.net/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        myAnimeListAPI = retrofit.create(MyAnimeListAPI.class);
    }

    public Single<User> getUser(){
        return myAnimeListAPI.getUser("anime_statistics");
    }

    public Single<Anime> getAnimeStatus(int id){
        return myAnimeListAPI.getAnimeStatus(id, "my_list_status");
    }

    public Single<AnimeStatus> updateAnimeStatus(int animeId, String status, int score, int episodesWatched){
        return myAnimeListAPI.updateAnimeStatus(
                animeId,
                status,
                false,
                score,
                episodesWatched
        );
    }

}
