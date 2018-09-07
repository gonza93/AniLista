package redix.soft.anilist.api;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface JikanAPI {

    @GET("/search/anime")
    Observable<Response> searchAnime(@Query("q") String query,
                                     @Query("page") int page);

    @GET("/anime/{id}")
    Observable<Anime> getAnimeInfo(@Path("id") int id);

}
