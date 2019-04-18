package redix.soft.anilist.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Seiyu {

    @SerializedName(value = "name", alternate = {"title"})
    private String name;
    @SerializedName(value = "image_url")
    private String imageURL;
    private String language;

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getLanguage() {
        return language;
    }

    @BindingAdapter("loadSeiyuAvatar")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new CropCircleTransformation())
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view);
    }
}
