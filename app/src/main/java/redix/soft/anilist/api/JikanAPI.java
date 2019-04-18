package redix.soft.anilist.api;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Response;
import redix.soft.anilist.model.ResponseSeiyu;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface JikanAPI {

    @GET("search/{type}")
    Observable<Response> search(@Path("type") String type,
                                @Query("q") String query,
                                @Query("page") int page,
                                @Query("limit") int limit);

    @GET("anime/{id}")
    Observable<Anime> getAnimeInfo(@Path("id") int id);

    @GET("anime/{id}/characters_staff")
    Observable<Response> getAnimeCharacters(@Path("id") int id);

    @GET("anime/{id}/news")
    Observable<Response> getAnimeNews(@Path("id") int id);

    @GET("anime/{id}/episodes/{page}")
    Observable<Response> getAnimeEpisodes(@Path("id") int id,
                                          @Path("page") int page);

    @GET("anime/{id}/pictures")
    Observable<Response> getAnimePictures(@Path("id") int id);

    @GET("anime/{id}/recommendations")
    Observable<Response> getAnimeRecommendations(@Path("id") int id);

    @GET("top/anime/{page}/{subtype}")
    Observable<Response> getTopAnime(@Path("page") int page,
                                     @Path("subtype") String subtype);

    @GET("top/people/{page}")
    Observable<ResponseSeiyu> getTopPeople(@Path("page") int page);

    @GET("schedule/{day}")
    Observable<Response> getSchedule(@Path("day") String day);

    @GET("season/{year}/{season}")
    Observable<Response> getSeasonAnime(@Path("year") int year,
                                        @Path("season") String season);

}
