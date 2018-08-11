package redix.soft.anilist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("result")
    private List<Anime> animes;

    private int resultLastPage;

    public List<Anime> getAnimes(){
        return this.animes;
    }

}
