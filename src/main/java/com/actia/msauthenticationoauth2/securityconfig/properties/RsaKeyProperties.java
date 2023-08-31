package com.actia.msauthenticationoauth2.securityconfig.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "oauth2")
public record RsaKeyProperties(RSAPrivateKey rsaPrivateKey, RSAPublicKey rsaPublicKey) {
}
