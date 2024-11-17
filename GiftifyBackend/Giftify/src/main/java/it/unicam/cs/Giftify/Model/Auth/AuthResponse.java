package it.unicam.cs.Giftify.Model.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class AuthResponse {
    @Getter
    @JsonProperty("access_token")
    private String accessToken;
    @Getter
    @JsonProperty("refresh_token")
    private String refreshToken;
    @Getter
    @JsonProperty("message")
    private String message;

    public AuthResponse(String accessToken, String refreshToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
        this.refreshToken = refreshToken;
    }


}
