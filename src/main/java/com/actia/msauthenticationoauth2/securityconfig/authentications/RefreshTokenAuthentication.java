package com.actia.msauthenticationoauth2.securityconfig.authentications;

import com.actia.msauthenticationoauth2.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Token personnalisé d'authentification. Utilisé comme Token d'authentification avec Spring Security.
 * Voir RefreshTokenFilterChain pour plus d'informations.
 * */
public class RefreshTokenAuthentication implements Authentication {


    private User user;
    private Set<GrantedAuthority> authoritySet;
    private boolean isAuthenticated;

    private String refreshToken;

    public RefreshTokenAuthentication(User user) {
        this.user = user;
        this.authoritySet = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toSet());
        this.isAuthenticated = (this.authoritySet.size() > 0);
    }

    public RefreshTokenAuthentication(String refreshTokenString) {
        this.refreshToken = refreshTokenString;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authoritySet;
    }

    @Override
    public String getCredentials() {
        return this.refreshToken;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user.getMatricule();
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public boolean equals(Object another) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getName() {
        return user.getMatricule();
    }
}
