package com.actia.msauthenticationoauth2.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token.expiration")
public record TokensExpirationDelay(Long accessTokenDelay, Long refreshTokenDelay) {
}
