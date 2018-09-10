package redix.soft.anilist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("results")
    private List<Anime> animes;

    private List<Character> characters;

    private int resultLastPage;

    public List<Anime> getAnimes(){
        return this.animes;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public int getResultLastPage() {
        return resultLastPage;
    }
}
