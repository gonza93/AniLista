package redix.soft.anilist.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Anime extends BaseObservable {

    @SerializedName("mal_id")
    private int id;
    @SerializedName("url")
    private String URL;
    @SerializedName("image_url")
    private String imageURL;
    @SerializedName(value = "title", alternate = "name")
    private String title;
    private String synopsis;
    private String type;
    private float score;
    private int episodes;
    private int members;
    private String premiered;
    private Aired aired;
    private int rank;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    private Related related;

    //Arrays
    private List<Studio> studios;
    private List<Genre> genres;
    @SerializedName("opening_themes")
    private List<String> openingThemes;
    @SerializedName("ending_themes")
    private List<String> endingThemes;
    private List<Character> characters;

    public int getId() {
        return id;
    }

    public String getURL() {
        return URL;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public float getScore() {
        return score;
    }

    public int getEpisodes() {
        return episodes;
    }

    public int getMembers() {
        return members;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getPremiered() {
        return premiered;
    }

    public Aired getAired() {
        return aired;
    }

    public int getRank() {
        return rank;
    }

    public List<Studio> getStudios() {
        return studios;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<String> getOpeningThemes() {
        return openingThemes;
    }

    public List<String> getEndingThemes() {
        return endingThemes;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Related getRelated() {
        return related;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    @BindingAdapter("loadThumbnail")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(25, 0))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view);
    }

    @BindingAdapter("loadThumbTrending")
    public static void loadImageTrend(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(25, 0))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view);
    }
}
