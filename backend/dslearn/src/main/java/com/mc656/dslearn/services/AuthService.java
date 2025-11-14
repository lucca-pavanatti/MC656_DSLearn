package com.mc656.dslearn.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.mc656.dslearn.config.GoogleConfig;
import com.mc656.dslearn.dtos.UserInfoDTO;
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final GoogleConfig googleConfig;

    public AuthService(UserRepository userRepository, GoogleConfig googleConfig) {
        this.userRepository = userRepository;
        this.googleConfig = googleConfig;
    }

    public void verifyAndCreateUser(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = retrieveGoogleInfo(idTokenString);
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        Optional<User> opt = userRepository.findByEmail(email);
        User user;
        user = opt.orElseGet(() -> User.builder()
                .name(name)
                .email(email)
                .build());

        userRepository.save(user);
    }

    private GoogleIdToken.Payload retrieveGoogleInfo(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleConfig.getClientId()))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken == null) {
            throw new GeneralSecurityException("Invalid ID token");
        }
        return idToken.getPayload();

    }

    public User verifyTokenAndGetUserEntity(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = retrieveGoogleInfo(idTokenString);
        String email = payload.getEmail();

        Optional<User> opt = userRepository.findByEmail(email);
        return opt.orElse(null);
    }

}
