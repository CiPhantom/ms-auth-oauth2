package com.actia.msauthenticationoauth2.service;

import com.actia.msauthenticationoauth2.entity.RefreshToken;
import com.actia.msauthenticationoauth2.entity.User;
import com.actia.msauthenticationoauth2.repository.RefreshTokenRepository;
import com.actia.msauthenticationoauth2.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public String generateRefreshToken(Authentication authentication) {
        Long exp = Instant.now().plusSeconds(604800).toEpochMilli();
        String uuid = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(uuid);
        refreshToken.setUser(new User(authentication.getName()));
        refreshToken.setExpirationTimeStamp(exp);
        return refreshTokenRepository.save(refreshToken).getRefreshToken();
    }

    @Transactional
    public void deleteOldRefreshTokenIfExists(Authentication authentication) {
        User user = userRepository.findUserByMatricule(authentication.getName());
        refreshTokenRepository.deleteByUser(user);
    }

    public String getUserRefreshToken(Authentication authentication) {
        User user = userRepository.findUserByMatricule(authentication.getName());
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user);
        if(Instant.ofEpochMilli(refreshToken.getExpirationTimeStamp()).isBefore(Instant.now())) {
            deleteOldRefreshTokenIfExists(authentication);
            throw new RuntimeException("Refresh Token not usable ! Already expired !");
        }
        return refreshToken.getRefreshToken();
    }

    public User getUserAssociatedToRefreshToken(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByRefreshToken(refreshTokenString);
        if(refreshToken != null) {
            return refreshToken.getUser();
        }
        return null;
    }
}
