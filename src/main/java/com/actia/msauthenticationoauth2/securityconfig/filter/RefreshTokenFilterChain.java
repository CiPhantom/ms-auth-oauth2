package com.actia.msauthenticationoauth2.securityconfig.filter;

import com.actia.msauthenticationoauth2.securityconfig.authentications.RefreshTokenAuthentication;
import com.actia.msauthenticationoauth2.securityconfig.providers.RefreshTokenAuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RefreshTokenFilterChain extends OncePerRequestFilter {

    private final RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider;

    public RefreshTokenFilterChain(RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider) {
        this.refreshTokenAuthenticationProvider = refreshTokenAuthenticationProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!isRefreshRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String refreshTokenString = request.getHeader("x-refresh-token");
        RefreshTokenAuthentication token = refreshTokenAuthenticationProvider.authenticate(new RefreshTokenAuthentication(refreshTokenString));
        if(token == null) {
            filterChain.doFilter(request, response);
        } else {
            var newSecurityContext = SecurityContextHolder.createEmptyContext();
            newSecurityContext.setAuthentication(token);
            SecurityContextHolder.setContext(newSecurityContext);
            filterChain.doFilter(request, response);
        }
    }

    private boolean isRefreshRequest(HttpServletRequest request) {
        return request.getRequestURI().equals("/refresh-authentication");
    }

}
