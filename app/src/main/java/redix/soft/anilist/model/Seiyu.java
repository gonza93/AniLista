package redix.soft.anilist.model;

import com.google.gson.annotations.SerializedName;

public class Seiyu {

    private String name;
    @SerializedName("image_url")
    private String imageURL;
    private String language;

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getLanguage() {
        return language;
    }


}
