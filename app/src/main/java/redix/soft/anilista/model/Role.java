package redix.soft.anilista.model;

import com.google.gson.annotations.SerializedName;

public class Role {

    private String role;
    private String position;
    private Anime anime;
    private Character character;

    public String getRole() {
        return role;
    }

    public String getPosition() {
        return position;
    }

    public Anime getAnime() {
        return anime;
    }

    public Character getCharacter() {
        return character;
    }
}
