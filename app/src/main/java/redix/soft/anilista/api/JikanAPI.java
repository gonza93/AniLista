package redix.soft.anilista.api;

import java.util.Map;

import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.Response;
import redix.soft.anilista.model.ResponseSeiyu;
import redix.soft.anilista.model.Seiyu;
import redix.soft.anilista.model.User;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Single;

public interface JikanAPI {

    @GET("search/{type}")
    Single<Response> search(@Path("type") String type,
                            @Query("q") String query,
                            @Query("page") int page,
                            @Query("limit") int limit);

    @GET("search/{type}")
    Single<Response> search(@Path("type") String type,
                            @QueryMap Map<String, String> params);

    @GET("anime/{id}")
    Single<Anime> getAnimeInfo(@Path("id") int id);

    @GET("anime/{id}/characters_staff")
    Single<Response> getAnimeCharacters(@Path("id") int id);

    @GET("anime/{id}/news")
    Single<Response> getAnimeNews(@Path("id") int id);

    @GET("anime/{id}/episodes/{page}")
    Single<Response> getAnimeEpisodes(@Path("id") int id,
                                      @Path("page") int page);

    @GET("anime/{id}/pictures")
    Single<Response> getAnimePictures(@Path("id") int id);

    @GET("anime/{id}/recommendations")
    Single<Response> getAnimeRecommendations(@Path("id") int id);

    @GET("anime/{id}/reviews/{page}")
    Single<Response> getAnimeReviews(@Path("id") int id,
                                     @Path("page") int page);

    @GET("top/anime/{page}/{subtype}")
    Single<Response> getTopAnime(@Path("page") int page,
                                 @Path("subtype") String subtype);

    @GET("top/people/{page}")
    Single<ResponseSeiyu> getTopPeople(@Path("page") int page);

    @GET("schedule/{day}")
    Single<Response> getSchedule(@Path("day") String day);

    @GET("season/{year}/{season}")
    Single<Response> getSeasonAnime(@Path("year") int year,
                                    @Path("season") String season);

    @GET("person/{id}")
    Single<Seiyu> getPerson(@Path("id") int id);

    @GET("genre/anime/{id}/{page}")
    Single<Response> getGenreAnime(@Path("id") int idGenre,
                                   @Path("page") int page);

    @GET("user/{username}")
    Single<User> getUser(@Path("username") String username);

    @GET("user/{username}/animelist/{filter}")
    Single<Response> getUserAnimeList(@Path("username") String username,
                                      @Path("filter") String filter,
                                      @Query("page") int page,
                                      @Query("order_by") String orderBy,
                                      @Query("sort") String sort);

}
