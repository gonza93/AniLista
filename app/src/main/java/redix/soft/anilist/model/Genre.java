package redix.soft.anilist.model;

import com.google.gson.annotations.SerializedName;

public class Genre {

    @SerializedName("url")
    private String URL;
    private String name;

    public String getURL() {
        return URL;
    }

    public String getName() {
        return name;
    }

}