package redix.soft.anilista.model;

import androidx.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class Picture implements Serializable {

    private String large;
    private String medium;
    private String small;

    public String getLarge() {
        return large;
    }

    public String getMedium() {
        return medium;
    }

    public String getSmall() {
        return small;
    }

    @BindingAdapter("loadPicture")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .into(view);
    }

}
