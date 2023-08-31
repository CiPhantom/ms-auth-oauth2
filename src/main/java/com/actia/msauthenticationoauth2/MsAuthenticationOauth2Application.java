package com.actia.msauthenticationoauth2;

import com.actia.msauthenticationoauth2.securityconfig.properties.RsaKeyProperties;
import com.actia.msauthenticationoauth2.service.properties.TokensExpirationDelay;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({RsaKeyProperties.class, TokensExpirationDelay.class})
@SpringBootApplication
public class MsAuthenticationOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(MsAuthenticationOauth2Application.class, args);
    }

}
