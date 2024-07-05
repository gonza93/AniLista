package redix.soft.anilista.model;

import com.google.gson.annotations.SerializedName;

public class ImageJPG {

    @SerializedName(value = "image_url")
    private String imageURL;
    @SerializedName(value = "small_image_url")
    private String smallImageURL;
    @SerializedName(value = "large_image_url")
    private String largeImageURL;

    public String getImageUrl() {
        return imageURL;
    }

    public String getSmallImageUrl() {
        return smallImageURL;
    }

    public String getLargeImageUrl() {
        return largeImageURL;
    }
}
