package redix.soft.anilista.model;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.util.Date;

public class Review {

    @SerializedName("mal_id")
    private int id;
    @SerializedName("url")
    private String URL;
    @SerializedName("helpful_count")
    private int helpfulCount;
    private Date date;
    private Reviewer reviewer;
    private String content;

    public int getId() {
        return id;
    }

    public String getURL() {
        return URL;
    }

    public int getHelpfulCount() {
        return helpfulCount;
    }

    public Date getDate() {
        return date;
    }

    public Reviewer getReviewer() {
        return reviewer;
    }

    public String getContent() {
        return content.replace("\\n", "\n");
    }

    public String getFormattedDate(){
        return DateFormat.getDateInstance().format(date);
    }

    public void setContent(String content) {
        this.content = content;
    }
}
