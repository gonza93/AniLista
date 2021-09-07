package redix.soft.anilista.model;

import com.google.gson.annotations.SerializedName;

public class DataAnime {

    private Anime node;
    @SerializedName("list_status")
    private AnimeStatus status;

    public Anime getNode() {
        return node;
    }

    public AnimeStatus getStatus() {
        return status;
    }
}

