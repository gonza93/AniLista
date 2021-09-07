package redix.soft.anilista.api;

import redix.soft.anilista.model.Token;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Single;

public interface MyAnimeListAuthAPI {

    @POST("oauth2/token")
    @FormUrlEncoded
    Single<Token> getToken(@Field("client_id") String clientId,
                           @Field("grant_type") String grantType,
                           @Field("code") String authCode,
                           @Field("code_verifier") String codeVerifier,
                           @Field("refresh_token") String refreshToken);

}
