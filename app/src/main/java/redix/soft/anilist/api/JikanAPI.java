package redix.soft.anilist.api;

import java.util.Map;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Response;
import redix.soft.anilist.model.ResponseSeiyu;
import redix.soft.anilist.model.Seiyu;
import redix.soft.anilist.model.User;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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

}
