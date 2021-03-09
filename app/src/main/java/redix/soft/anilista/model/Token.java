package redix.soft.anilista.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import redix.soft.anilista.util.DataUtil;

public class Token {

    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires_in")
    private int expiresIn;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;

    public String getTokenType() {
        return tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void save(Context context) {
        DataUtil.getInstance(context).saveString(DataUtil.DATA.TOKEN.toString(), this.accessToken);
        DataUtil.getInstance(context).saveString(DataUtil.DATA.REFRESH.toString(), this.refreshToken);
        DataUtil.getInstance(context).saveInt(DataUtil.DATA.EXPIRE.toString(), this.expiresIn);
    }
}
