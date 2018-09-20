package redix.soft.anilist.model;

import com.google.gson.annotations.SerializedName;

public class Episode {

    @SerializedName("episode_id")
    private int id;
    private String title;
    @SerializedName("titile_japanese")
    private String titleJapanese;
    @SerializedName("titile_romaji")
    private String titleRomaji;
    private Aired aired;
    private boolean filler;
    private boolean recap;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleJapanese() {
        return titleJapanese;
    }

    public String getTitleRomaji() {
        return titleRomaji;
    }

    public Aired getAired() {
        return aired;
    }

    public boolean isFiller() {
        return filler;
    }

    public boolean isRecap() {
        return recap;
    }
}
