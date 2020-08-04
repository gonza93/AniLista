package redix.soft.anilist.model;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Episode {

    @SerializedName("episode_id")
    private int id;
    private String title;
    @SerializedName("title_japanese")
    private String titleJapanese;
    @SerializedName("title_romanji")
    private String titleRomaji;
    private Date aired;
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

    public String getAired() {
        return aired != null ? DateFormat.getDateInstance().format(aired) : null;
    }

    public boolean isFiller() {
        return filler;
    }

    public boolean isRecap() {
        return recap;
    }
}
