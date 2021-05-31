package redix.soft.anilista.api;

import redix.soft.anilista.model.Token;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;

public class MyAnimeListAuthService {

    private MyAnimeListAuthAPI myAnimeListAPI;
    public static int apiDelay = 2000;

    public MyAnimeListAuthService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://myanimelist.net/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        myAnimeListAPI = retrofit.create(MyAnimeListAuthAPI.class);
    }

    public Single<Token> getToken(String authCode, String codeVerifier, String grantType, String refreshToken){
        return myAnimeListAPI.getToken(
                "9384b6b211e93a675362739dd10d1ff4",
                grantType,
                authCode,
                codeVerifier,
                refreshToken);
    }

}
