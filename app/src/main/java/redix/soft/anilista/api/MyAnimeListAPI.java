package redix.soft.anilista.api;

import org.json.JSONArray;

import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.AnimeStatus;
import redix.soft.anilista.model.Token;
import redix.soft.anilista.model.User;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;

public interface MyAnimeListAPI {

    @GET("users/@me")
    Single<User> getUser(@Query("fields") String fields);

    @GET("anime/{id}")
    Single<Anime> getAnimeStatus(@Path("id") int id,
                                 @Query("fields") String fields);

    @PUT("anime/{id}/my_list_status")
    @FormUrlEncoded
    Single<AnimeStatus> updateAnimeStatus(@Path("id") int animeId,
                                          @Field("status") String status,
                                          @Field("is_rewatching") boolean isRewatching,
                                          @Field("score") int score,
                                          @Field("num_watched_episodes") int watchedEpisodes);

    @DELETE("anime/{id}/my_list_status")
    Single<Object> deleteAnimeStatus(@Path("id") int animeId);

}
