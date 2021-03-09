package redix.soft.anilista.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AnimeStats {

    @SerializedName("days_watched")
    private float datsWatched;
    @SerializedName("mean_score")
    private float meanScore;
    @SerializedName(value = "watching", alternate = "num_items_watching")
    private int watching;
    @SerializedName(value = "completed", alternate = "num_items_completed")
    private int completed;
    @SerializedName(value = "on_hold", alternate = "num_items_on_hold")
    private int onHold;
    private int dropped;
    @SerializedName("plan_to_watch")
    private int planToWatch;
    @SerializedName("total_entries")
    private int totalEntries;
    private int rewatched;
    @SerializedName("episodes_watched")
    private int episodesWatched;

    public float getDatsWatched() {
        return datsWatched;
    }

    public float getMeanScore() {
        return meanScore;
    }

    public int getWatching() {
        return watching;
    }

    public int getCompleted() {
        return completed;
    }

    public int getOnHold() {
        return onHold;
    }

    public int getDropped() {
        return dropped;
    }

    public int getPlanToWatch() {
        return planToWatch;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public int getRewatched() {
        return rewatched;
    }

    public int getEpisodesWatched() {
        return episodesWatched;
    }

}
