package redix.soft.anilista.model;

import com.google.gson.annotations.SerializedName;

public class Role {

    @SerializedName(value = "role", alternate = "position")
    private String role;
    private Anime anime;
    private Character character;

    public String getRole() {
        return role;
    }

    public Anime getAnime() {
        return anime;
    }

    public Character getCharacter() {
        return character;
    }
}
