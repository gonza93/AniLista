package redix.soft.anilista.model;

import com.google.gson.annotations.SerializedName;

public class Studio {

    private String name;
    @SerializedName("url")
    private String URL;

    public String getName() {
        return name;
    }
    public String getURL() {
        return URL;
    }

}
