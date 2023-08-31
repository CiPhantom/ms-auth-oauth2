package com.actia.msauthenticationoauth2.controller;

import com.actia.msauthenticationoauth2.service.RefreshTokenService;
import com.actia.msauthenticationoauth2.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller pour gérer l'access token.
 */
@RestController
public class TokenController {
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    /**
     * Prévoir configuration Lombok pour utiliser @RequiredArgsConstructor
     */
    public TokenController(TokenService tokenService, RefreshTokenService refreshTokenService) {
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/authenticate")
    public TokenResponse getAuthenticationToken(Authentication authentication) {
        String token = tokenService.generateToken(authentication);
        refreshTokenService.deleteOldRefreshTokenIfExists(authentication);
        String refreshToken = refreshTokenService.generateRefreshToken(authentication);
        return new TokenResponse(token, refreshToken);
    }

    @GetMapping("/refresh-authentication")
    public TokenResponse refreshAuthentication(Authentication authentication) {
        String token = tokenService.generateToken(authentication);
        String refreshToken = refreshTokenService.getUserRefreshToken(authentication);
        return new TokenResponse(token, refreshToken);
    }
}
