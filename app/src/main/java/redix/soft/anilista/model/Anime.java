package redix.soft.anilista.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Anime extends BaseObservable {

    @SerializedName(value = "mal_id", alternate = "id")
    private int id;
    @SerializedName("url")
    private String URL;
    private Image images;
    @SerializedName("image_url")
    private String imageURL;
    @SerializedName(value = "title", alternate = "name")
    private String title;
    @SerializedName(value = "title_japanese")
    private String titleJapanese;
    @SerializedName(value = "title_english")
    private String titleEnglish;
    private String synopsis;
    private String type;
    private float score;
    @SerializedName(value = "episodes", alternate = "chapters")
    private int episodes;
    private int volumes;
    private int members;
    private String premiered;
    private Aired aired;
    private int rank;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    private Related related;

    private Theme theme;

    //Arrays
    private List<Studio> studios;
    private List<Genre> genres;
    private List<Character> characters;
    private List<Review> reviews;
    private List<Relation> relations;

    //User anime list
    @SerializedName("watching_status")
    private int watchingStatus;
    @SerializedName("watched_episodes")
    private int watchedEpisodes;
    @SerializedName("total_episodes")
    private int totalEpisodes;
    @SerializedName("my_list_status")
    private AnimeStatus animeStatus;
    @SerializedName("main_picture")
    private Picture picture;

    //Additional
    private boolean selected;

    public Anime() {
        this.animeStatus = new AnimeStatus();
    }

    public int getId() {
        return id;
    }

    public String getURL() {
        return URL;
    }

    public Image getImages() {
        return images;
    }

    public String getTitleJapanese() {
        return titleJapanese;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Bindable
    public String getTitle() {
        return title;
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

    public int getVolumes() {
        return volumes;
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

    public Aired getAired() {
        return aired;
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

    public Theme getTheme() {
        return theme;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Related getRelated() {
        return related;
    }

    public int getWatchingStatus() {
        return watchingStatus;
    }

    public int getWatchedEpisodes() {
        return watchedEpisodes;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Bindable
    public AnimeStatus getAnimeStatus() {
        return animeStatus;
    }

    public void setAnimeStatus(AnimeStatus animeStatus) {
        this.animeStatus = animeStatus;
        this.animeStatus.setEpisodes(episodes);
        notifyPropertyChanged(BR.animeStatus);
    }

    public Picture getPicture() {
        return picture;
    }

    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @BindingAdapter("loadThumbnail")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(25, 0))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view);
    }

    @BindingAdapter("loadThumbTrending")
    public static void loadImageTrend(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(25, 0))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view);
    }

    @BindingAdapter("loadThumbRanking")
    public static void loadImageRanking(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(35, 0, RoundedCornersTransformation.CornerType.LEFT))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view);
    }

    /*@BindingAdapter("setAnimeStatus")
    public static void setAnimeStatus(TextView view, int status) {
        switch (status){
            case 1: //Watching
                view.setBackgroundResource(R.drawable.bg_canon);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorCanonText));
                view.setText(R.string.watching);
                break;
            case 2: //Completed
                view.setBackgroundResource(R.drawable.bg_primary);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
                view.setText(R.string.completed);
                break;
            case 3: //On Hold
                view.setBackgroundResource(R.drawable.bg_yellow);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorYellowText));
                view.setText(R.string.on_hold);
                break;
            case 4: //Dropped
                view.setBackgroundResource(R.drawable.bg_red);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorRedText));
                view.setText(R.string.dropped);
                break;
            case 6: //Plan to Watch
                view.setBackgroundResource(R.drawable.bg_gray);
                view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorGrayText));
                view.setText(R.string.plan_to_watch);
                break;
        }
    }*/
}
