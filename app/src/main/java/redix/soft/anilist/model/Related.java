package redix.soft.anilist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Related {

    @SerializedName("Adaptation")
    private List<Anime> adaptation;
    @SerializedName("Other")
    private List<Anime> other;
    @SerializedName("Prequel")
    private List<Anime> prequel;
    @SerializedName("Sequel")
    private List<Anime> sequel;
    @SerializedName("Side story")
    private List<Anime> sideStory;
    @SerializedName("Summary")
    private List<Anime> summary;
    @SerializedName("Alternative version")
    private List<Anime> alternativeVersion;
    @SerializedName("Character")
    private List<Anime> character;
    @SerializedName("Spin-off")
    private List<Anime> spinOff;

    public List<Anime> getAdaptation() {
        return adaptation;
    }

    public List<Anime> getOther() {
        return other;
    }

    public List<Anime> getPrequel() {
        return prequel;
    }

    public List<Anime> getSequel() {
        return sequel;
    }

    public List<Anime> getSideStory() {
        return sideStory;
    }

    public List<Anime> getSummary() {
        return summary;
    }

    public List<Anime> getAlternativeVersion() {
        return alternativeVersion;
    }

    public List<Anime> getCharacter() {
        return character;
    }

    public List<Anime> getSpinOff() {
        return spinOff;
    }
}
