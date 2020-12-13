package redix.soft.anilista.model;

import java.util.List;

public class Favorites {

    private List<Anime> anime;
    private List<Anime> manga;
    private List<Character> characters;
    private List<Seiyu> people;

    public List<Anime> getAnime() {
        return anime;
    }

    public void setAnime(List<Anime> anime) {
        this.anime = anime;
    }

    public List<Anime> getManga() {
        return manga;
    }

    public void setManga(List<Anime> manga) {
        this.manga = manga;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public List<Seiyu> getPeople() {
        return people;
    }

    public void setPeople(List<Seiyu> people) {
        this.people = people;
    }
}
