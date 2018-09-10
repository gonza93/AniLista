package redix.soft.anilist.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Character {

    @SerializedName("image_url")
    private String imageURL;
    private String name;
    private String role;
    @SerializedName("voice_actors")
    private List<Seiyu> seiyus;

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public List<Seiyu> getSeiyus() {
        return seiyus;
    }

    @BindingAdapter("loadAvatar")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .into(view);
    }
}
