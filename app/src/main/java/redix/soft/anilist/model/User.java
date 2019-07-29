package redix.soft.anilist.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class User {

    private String username;
    @SerializedName("image_url")
    private String imageURL;
    private String gender;
    private Date birthday;
    private String location;
    @SerializedName("anime_stats")
    private AnimeStats animeStats;
    private Favorites favorites;
    private String about;

    public String getUsername() {
        return username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getFormattedBirthday(){
        return DateFormat.getDateInstance().format(birthday);
    }

    public String getLocation() {
        return location;
    }

    public AnimeStats getAnimeStats() {
        return animeStats;
    }

    public Favorites getFavorites() {
        return favorites;
    }

    public String getAbout() {
        return about;
    }

    @BindingAdapter("loadUserAvatar")
    public static void loadUserAvatar(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(25, 0))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view);
    }
}
