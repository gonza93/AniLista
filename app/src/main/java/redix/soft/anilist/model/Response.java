package redix.soft.anilist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName(value = "results", alternate = {"top", "recommendations", "anime", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"})
    private List<Anime> animes;

    private List<Character> characters;

    private List<Episode> episodes;

    private List<News> articles;

    private List<Picture> pictures;

    private List<Review> reviews;

    @SerializedName("last_page")
    private int searchLastPage;

    @SerializedName("episodes_last_page")
    private int episodesLastPage;

    public List<Anime> getAnimes(){
        return this.animes;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public List<News> getArticles() {
        return articles;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public int getSearchLastPage() {
        return searchLastPage;
    }

    public int getEpisodesLastPage() {
        return episodesLastPage;
    }
}
