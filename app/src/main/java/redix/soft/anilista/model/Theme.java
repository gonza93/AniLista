package redix.soft.anilista.model;

public class Theme {

    private String order;
    private String title;
    private String author;
    private String episodes;
    private boolean isOpening;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEpisodes() {
        return episodes;
    }

    public void setEpisodes(String episodes) {
        this.episodes = episodes;
    }

    public boolean isOpening() {
        return isOpening;
    }

    public void setOpening(boolean opening) {
        isOpening = opening;
    }
}
