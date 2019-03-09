package redix.soft.anilist.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Picture {

    private String large;
    private String small;

    public String getLarge() {
        return large;
    }

    public String getSmall() {
        return small;
    }

    @BindingAdapter("loadPictureThumbnail")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .into(view);
    }

}
