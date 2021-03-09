package redix.soft.anilista.model;

import com.google.gson.annotations.SerializedName;

public class TokenHeader {

    @SerializedName("client_id") String clientId;
    @SerializedName("grant_type") String grantType;
    @SerializedName("code") String authCode;
    @SerializedName("code_verifier") String codeVerifier;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public void setCodeVerifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }
}
