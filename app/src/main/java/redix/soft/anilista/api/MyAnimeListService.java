package redix.soft.anilista.api;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.AnimeStatus;
import redix.soft.anilista.model.ResponseModel;
import redix.soft.anilista.model.ResponseModelMAL;
import redix.soft.anilista.model.User;
import redix.soft.anilista.util.DataUtil;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyAnimeListService {

    private MyAnimeListAPI myAnimeListAPI;
    private Context context;
    public static int apiDelay = 2000;

    public MyAnimeListService(Context context){
        this.context = context;
        String token = DataUtil.getInstance(context).getString(DataUtil.DATA.TOKEN.toString());

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                })
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    if (response.code() == 401)
                        refreshToken();
                    return response;
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.myanimelist.net/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        myAnimeListAPI = retrofit.create(MyAnimeListAPI.class);
    }

    public void refreshToken(){
        String refreshToken = DataUtil.getInstance(context).getString(DataUtil.DATA.REFRESH.toString());

        new MyAnimeListAuthService()
                .getToken(null, null, "refresh_token", refreshToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        token -> token.save(context),
                        throwable   -> Log.d("RefreshToken", throwable.getMessage())
                );
    }

    public Single<User> getUser(){
        return myAnimeListAPI.getUser("anime_statistics");
    }

    public Single<ResponseModelMAL> getUserAnimeList(Map<String, String> params) {
        return myAnimeListAPI.getUserAnimeList(params);
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

    public Single<Object> deleteAnimeStatus(int animeId) {
        return myAnimeListAPI.deleteAnimeStatus(animeId);
    }

}
