package com.mc656.dslearn.services;

import com.mc656.dslearn.dtos.UserInfoDTO;
import com.mc656.dslearn.models.User;
import com.mc656.dslearn.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class UserService {

    private final AuthService authService;
    private final UserRepository userRepository;

    public UserService(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    public UserInfoDTO getUserInfoByToken(String idTokenString) throws GeneralSecurityException, IOException {
        User user = authService.verifyTokenAndGetUserEntity(idTokenString);
        if (user == null) return null;
        return buildUserInfoDTO(user);
    }

    public UserInfoDTO updateUserInfoFromToken(String idTokenString) throws GeneralSecurityException, IOException {
        User user = authService.verifyTokenAndGetUserEntity(idTokenString);
        if (user == null) return null;
        user = userRepository.save(user);
        return buildUserInfoDTO(user);
    }

    private UserInfoDTO buildUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}
