package redix.soft.anilista.api;

import redix.soft.anilista.model.Response;
import redix.soft.anilista.model.Token;
import redix.soft.anilista.model.TokenHeader;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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
