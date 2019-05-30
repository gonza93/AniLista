package redix.soft.anilist.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class Seiyu {

    @SerializedName("mal_id")
    private int id;
    @SerializedName(value = "name", alternate = {"title"})
    private String name;
    @SerializedName("image_url")
    private String imageURL;
    @SerializedName("website_url")
    private String websiteURL;
    private String language;
    @SerializedName("given_name")
    private String givenName;
    @SerializedName("family_name")
    private String familyName;
    private Date birthday;
    @SerializedName("member_favorites")
    private String memberFavorites;
    private String about;
    @SerializedName("voice_acting_roles")
    private List<Role> voiceRoles;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public String getLanguage() {
        return language;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getFormattedBirthday(){
        return DateFormat.getDateInstance().format(birthday);
    }

    public String getMemberFavorites() {
        return memberFavorites == null ? "" : memberFavorites;
    }

    public String getAbout() {
        return about;
    }
    public void setAbout(String about) {
        this.about = about;
    }

    public List<Role> getVoiceRoles() {
        return voiceRoles;
    }

    @BindingAdapter("loadSeiyuAvatar")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new CropCircleTransformation())
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view);
    }

    @BindingAdapter("loadSeiyuImage")
    public static void loadProfileImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedCornersTransformation(25, 0))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(view);
    }
}
