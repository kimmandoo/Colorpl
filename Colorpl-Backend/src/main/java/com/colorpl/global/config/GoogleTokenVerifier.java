package com.colorpl.global.config;
import com.colorpl.global.common.exception.InvalidGoogleIdTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleTokenVerifier {

    @Value("${google.client-id}") private String defaultClientId;

    private final GooglePublicKeysManager publicKeysManager;

    public GoogleTokenVerifier() {
        this.publicKeysManager = new GooglePublicKeysManager.Builder(
            new NetHttpTransport(), GsonFactory.getDefaultInstance()
        ).build();
    }

    public String verifyToken(String idTokenString, String clientId) throws IOException, GeneralSecurityException {

        if (clientId == null || clientId.isEmpty()) {
            clientId = defaultClientId;
        }
        GoogleIdTokenVerifier idTokenVerifier = new GoogleIdTokenVerifier.Builder(
            publicKeysManager
        )
            .setAudience(Collections.singletonList(clientId)) // 클라이언트 ID 설정
            .build();

        // ID 토큰 검증
        GoogleIdToken idToken = idTokenVerifier.verify(idTokenString);
        if (idToken == null) {
            throw new InvalidGoogleIdTokenException();
        }

        // 토큰의 페이로드에서 이메일 추출
        GoogleIdToken.Payload payload = idToken.getPayload();
        return payload.getEmail();
    }
}
