package redix.soft.anilista.api;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import redix.soft.anilista.model.Token;
import redix.soft.anilista.model.TokenHeader;
import redix.soft.anilista.util.DataUtil;
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

    public Single<Token> getToken(String authCode, String codeVerifier){
        TokenHeader header = new TokenHeader();
        header.setClientId("9384b6b211e93a675362739dd10d1ff4");
        header.setGrantType("authorization_code");
        header.setAuthCode(authCode);
        header.setCodeVerifier(codeVerifier);

        //return myAnimeListAPI.getToken(header);
        return myAnimeListAPI.getToken("9384b6b211e93a675362739dd10d1ff4", "authorization_code", authCode, codeVerifier);
    }

}
