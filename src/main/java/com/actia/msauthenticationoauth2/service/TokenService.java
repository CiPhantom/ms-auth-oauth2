package com.actia.msauthenticationoauth2.service;

import com.actia.msauthenticationoauth2.entity.RefreshToken;
import com.actia.msauthenticationoauth2.entity.User;
import com.actia.msauthenticationoauth2.repository.RefreshTokenRepository;
import com.actia.msauthenticationoauth2.repository.UserRepository;
import com.actia.msauthenticationoauth2.service.properties.TokensExpirationDelay;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    private final TokensExpirationDelay tokensExpirationDelay;

    private final UserRepository userRepository;
    public TokenService(JwtEncoder jwtEncoder, TokensExpirationDelay tokensExpirationDelay, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.tokensExpirationDelay = tokensExpirationDelay;
        this.userRepository = userRepository;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokensExpirationDelay.accessTokenDelay()))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
