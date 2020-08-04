package redix.soft.anilist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSeiyu {

    @SerializedName(value = "top")
    private List<Seiyu> seiyus;

    public List<Seiyu> getSeiyus() {
        return seiyus;
    }
}
