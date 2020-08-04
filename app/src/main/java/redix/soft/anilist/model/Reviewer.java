package redix.soft.anilist.model;

import android.content.res.ColorStateList;
import androidx.databinding.BindingAdapter;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import redix.soft.anilist.R;

public class Reviewer {

    @SerializedName("url")
    private String URL;
    @SerializedName("image_url")
    private String imageURL;
    private String username;
    @SerializedName("episodes_seen")
    private String episodesSeen;
    private Scores scores;

    public String getURL() {
        return URL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUsername() {
        return username;
    }

    public String getEpisodesSeen() {
        return episodesSeen;
    }

    public Scores getScores() {
        return scores;
    }

    @BindingAdapter("loadReviewerAvatar")
    public static void loadReviewerAvatar(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(25, 0))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view);
    }

    @BindingAdapter("setScoreColor")
    public static void setScoreColor(View view, int score) {
        int backgroud = score >= 6? R.drawable.bg_primary : R.drawable.bg_red;
        view.setBackgroundResource(backgroud);
    }

    @BindingAdapter("setBarColor")
    public static void setBarColor(View seekBar, int score) {
        ColorStateList colorRed = ColorStateList.valueOf(ContextCompat.getColor(seekBar.getContext(), R.color.colorRedText));
        ColorStateList colorPrimary = ColorStateList.valueOf(ContextCompat.getColor(seekBar.getContext(), R.color.colorPrimary));
        ((SeekBar) seekBar).setProgressTintList(score >= 6? colorPrimary : colorRed);
    }

    @BindingAdapter("setScoreTextColor")
    public static void setScoreTextColor(TextView textView, int score) {
        int color = ContextCompat.getColor(textView.getContext(), R.color.colorRedText);
        if (score < 6)
            textView.setTextColor(color);
    }

}
