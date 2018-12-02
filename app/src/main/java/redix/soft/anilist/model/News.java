package redix.soft.anilist.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.util.Date;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class News {

    @SerializedName("url")
    private String URL;
    private String title;
    private Date date;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("image_url")
    private String imageURL;
    private int comments;
    private String intro;


    public String getURL() {
        return URL;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getComments() {
        return comments;
    }

    public String getIntro() {
        return intro;
    }

    @BindingAdapter("loadThumbNews")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(25, 0))
                .into(view);
    }
}
