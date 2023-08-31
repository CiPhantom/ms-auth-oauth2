package com.actia.msauthenticationoauth2.securityconfig.providers;

import com.actia.msauthenticationoauth2.entity.User;
import com.actia.msauthenticationoauth2.securityconfig.authentications.RefreshTokenAuthentication;
import com.actia.msauthenticationoauth2.service.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {

    private final RefreshTokenService refreshTokenService;

    public RefreshTokenAuthenticationProvider(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }


    @Override
    public RefreshTokenAuthentication authenticate(Authentication authentication) throws AuthenticationException {
        var auth = (RefreshTokenAuthentication) authentication;
        User user = refreshTokenService.getUserAssociatedToRefreshToken(auth.getCredentials());
       if(user != null) {
           return new RefreshTokenAuthentication(user);
       }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthentication.class.isAssignableFrom(authentication);
    }
}
