package redix.soft.anilist.api;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface JikanAPI {

    @GET("search/anime")
    Observable<Response> searchAnime(@Query("q") String query,
                                     @Query("page") int page);

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

    @GET("top/anime/{page}/airing")
    Observable<Response> getTrendingAnime(@Path("page") int page);

}
