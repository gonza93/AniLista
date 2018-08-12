package redix.soft.anilist.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import redix.soft.anilist.BR;

public class Anime extends BaseObservable {

    @SerializedName("mal_id")
    private int id;
    private String URL;
    @SerializedName("image_url")
    private String imageURL;
    private String title;
    private String description;
    private String synopsis;
    private String type;
    private float score;
    private int episodes;
    private int members;
    private String premiered;
    @SerializedName("aired_string")
    private String airedString;
    private int rank;

    //Arrays
    @SerializedName("studio")
    private List<Studio> studios;
    @SerializedName("genre")
    private List<Genre> genres;

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

    public String getDescription() {
        return description;
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

    public String getAiredString() {
        return airedString;
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

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(15, 0))
                .into(view);
    }
}
