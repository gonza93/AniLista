package redix.soft.anilista.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import redix.soft.anilista.R;

public class AnimeStatus {

    private String status;
    private int score;
    @SerializedName(value = "num_episodes_watched", alternate = "num_watched_episodes")
    private int episodesWatched;
    @SerializedName("is_rewatching")
    private boolean isRewatching;
    @SerializedName("updated_at")
    private Date updatedAt;
    private int episodes;

    public String getStatus() {
        if (status == null)
            return status = "Add to List";

        return status.substring(0, 1).toUpperCase() + status.substring(1).replace("_", " ");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScore() {
        return String.valueOf(score);
    }
    public int getScoreInt() { return score; }

    public void setScore(int score) {
        this.score = score;
    }

    public int getEpisodesWatched() {
        return episodesWatched;
    }
    public String getEpisodesWatchedByTotal() {
        if (episodes >= 100)
            return episodesWatched + "/" + episodes;
        else
            return "Ep: " + episodesWatched + "/" + episodes;
    }
    public int getEpisodesWatchedInt() { return episodesWatched; }

    public void setEpisodesWatched(int episodesWatched) {
        this.episodesWatched = episodesWatched;
    }

    public boolean isRewatching() {
        return isRewatching;
    }

    public void setRewatching(boolean rewatching) {
        isRewatching = rewatching;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    @BindingAdapter("showAddIcon")
    public static void showAddIcon(ImageView imageView, String status) {
        if (status == null)
            return;

        if (status.equals("Add to List"))
            imageView.setImageResource(R.drawable.ic_add);
        else
            imageView.setImageResource(R.drawable.ic_back);
    }

    @BindingAdapter("setStatusColor")
    public static void setStatusColor(View view, String status) {
        if (status == null)
            return;

        switch (status) {
            case "Completed":
                view.setBackgroundResource(R.drawable.bg_button);
                break;
            case "On hold":
                view.setBackgroundResource(R.drawable.bg_button_yellow);
                break;
            case "Dropped":
                view.setBackgroundResource(R.drawable.bg_button_red);
                break;
            case "Plan to watch":
                view.setBackgroundResource(R.drawable.bg_button_gray);
                break;
            default:
                view.setBackgroundResource(R.drawable.bg_button_green);
                break;
        }
    }

    @BindingAdapter("setAnimeStatus")
    public static void setAnimeStatus(TextView view, String status) {
        if (status == null)
            return;

        switch (status){
            case "Watching":
                view.setBackgroundResource(R.drawable.bg_canon);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorCanonText));
                view.setText(R.string.watching);
                break;
            case "Completed":
                view.setBackgroundResource(R.drawable.bg_primary);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
                view.setText(R.string.completed);
                break;
            case "On hold":
                view.setBackgroundResource(R.drawable.bg_yellow);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorYellowText));
                view.setText(R.string.on_hold);
                break;
            case "Dropped":
                view.setBackgroundResource(R.drawable.bg_red);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorRedText));
                view.setText(R.string.dropped);
                break;
            case "Plan to watch":
                view.setBackgroundResource(R.drawable.bg_gray);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorGrayText));
                view.setText(R.string.plan_to_watch);
                break;
        }
    }

}
