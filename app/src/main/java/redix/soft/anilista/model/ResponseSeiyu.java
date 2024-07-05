package redix.soft.anilista.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSeiyu {

    @SerializedName(value = "data")
    private List<Seiyu> seiyus;

    public List<Seiyu> getSeiyus() {
        return seiyus;
    }
}
