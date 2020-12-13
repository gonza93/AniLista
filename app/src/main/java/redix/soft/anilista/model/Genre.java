package redix.soft.anilista.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Genre {

    public Genre(int id, String name){
        this.id = id;
        this.name = name;
    }

    @SerializedName("mal_id")
    private int id;
    @SerializedName("url")
    private String URL;
    private String name;
    private boolean selected;

    public int getId() {
        return id;
    }

    public String getURL() {
        return URL;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void select(boolean state) {
        this.selected = state;
    }

    public static List<Genre> getGenres(){
        List<Genre> genres = new ArrayList<>();

        genres.add(new Genre(1, "Action"));
        genres.add(new Genre(2, "Adventure"));
        genres.add(new Genre(3, "Cars"));
        genres.add(new Genre(4, "Comedy"));
        genres.add(new Genre(5, "Dementia"));
        genres.add(new Genre(6, "Demons"));
        genres.add(new Genre(7, "Mystery"));
        genres.add(new Genre(8, "Drama"));
        genres.add(new Genre(9, "Ecchi"));
        genres.add(new Genre(10, "Fantasy"));
        genres.add(new Genre(11, "Game"));
        genres.add(new Genre(12, "Hentai"));
        genres.add(new Genre(13, "Historical"));
        genres.add(new Genre(14, "Horror"));
        genres.add(new Genre(15, "Kids"));
        genres.add(new Genre(16, "Magic"));
        genres.add(new Genre(17, "Martial Arts"));
        genres.add(new Genre(18, "Mecha"));
        genres.add(new Genre(19, "Music"));
        genres.add(new Genre(20, "Parody"));
        genres.add(new Genre(21, "Samurai"));
        genres.add(new Genre(22, "Romance"));
        genres.add(new Genre(23, "School"));
        genres.add(new Genre(24, "Sci-Fi"));
        genres.add(new Genre(25, "Shoujo"));
        genres.add(new Genre(26, "Shoujo Ai"));
        genres.add(new Genre(27, "Shounen"));
        genres.add(new Genre(28, "Shounen Ai"));
        genres.add(new Genre(29, "Space"));
        genres.add(new Genre(30, "Sports"));
        genres.add(new Genre(31, "Super Power"));
        genres.add(new Genre(32, "Vampire"));
        genres.add(new Genre(33, "Yaoi"));
        genres.add(new Genre(34, "Yuri"));
        genres.add(new Genre(35, "Harem"));
        genres.add(new Genre(36, "Slice of Life"));
        genres.add(new Genre(37, "Supernatural"));
        genres.add(new Genre(38, "Military"));
        genres.add(new Genre(39, "Police"));
        genres.add(new Genre(40, "Psychological"));
        genres.add(new Genre(41, "Thriller"));
        genres.add(new Genre(42, "Seinen"));
        genres.add(new Genre(43, "Josei"));

        return genres;
    }
}
