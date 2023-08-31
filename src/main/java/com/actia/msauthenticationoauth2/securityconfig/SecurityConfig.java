package com.actia.msauthenticationoauth2.securityconfig;

import com.actia.msauthenticationoauth2.securityconfig.filter.RefreshTokenFilterChain;
import com.actia.msauthenticationoauth2.securityconfig.properties.RsaKeyProperties;
import com.actia.msauthenticationoauth2.securityconfig.providers.AgcAuthenticationProvider;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final RsaKeyProperties rsaKeyProperties;

    private final AgcAuthenticationProvider authenticationProvider;

    private final RefreshTokenFilterChain refreshTokenFilterChain;

    public SecurityConfig(RsaKeyProperties rsaKeyProperties, AgcAuthenticationProvider authenticationProvider, RefreshTokenFilterChain refreshTokenFilterChain) {
        this.rsaKeyProperties = rsaKeyProperties;
        this.authenticationProvider = authenticationProvider;
        this.refreshTokenFilterChain = refreshTokenFilterChain;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /**
        * Securisation des routes.
        * 1 - J'authorise que les routes /authenticate, /refresh-authentication (routes de connexions) sans être authentifier
        * 2 - Toutes les routes de la ressources /actions/** seront authentifiées obligatoirement
        * 3 -
        * */
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/authenticate", "/refresh-authentication").permitAll());
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(antMatcher("/actions/**")).authenticated());
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(antMatcher("/h2-console/**")).permitAll());

        /**
         * Pour les besoins du poc desactivation de csrf.
         * A réactivité dans un cas réel
         **/
        httpSecurity.csrf(csrf-> csrf.disable());
        /**
         * utilisation du bean par défaut pour les cors cf la méthode ci-dessous:  CorsConfigurationSource corsConfigurationSource()
         *  la configuration parle d'elle même. Les headers à authorisé, l'origine des requête à authorisé
         */
        httpSecurity.cors(Customizer.withDefaults());

        /**
         * configuration sur les headers, par nécessaire, juste pour accéder à la base h2
         */
        httpSecurity.headers(header -> header.frameOptions(Customizer.withDefaults()).disable());

        /**
         * le provider d'authentification que j'utilise.
         */
        httpSecurity.authenticationProvider(authenticationProvider);

        /**
        * Utilisé pour l'authentification avec login et mot de passe, on retrouve cette authentification avec le header Authorization: Basic xxxxxxx==
        * potentiellement à retirer
         */
        httpSecurity.httpBasic(Customizer.withDefaults());

        /**
         * Pas de session utilisée
         */
        httpSecurity.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /**
         * Server d'authentification OAuth2.0 pour utiliser les token JWT. Utilise le décodeur et encoder injecté en bean dans cette classe
         * voir : buildJwtDecoder(), buildJwtEncoder()
         */
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        /**
         * Filter qui gère seulement le cas du refreshToken.
         */
        httpSecurity.addFilterAfter(refreshTokenFilterChain,  BearerTokenAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /**
     * Utilise les propriétés clé privé et public, ligne 1 et 2 du fichier application.properties, pour générer un décodeur
     * de token JWT et c'est ce qui permet de valider la signature du token et s'assurer que la donnée n'a pas été altérée.
     * @return JwtDecoder
     */
    @Bean
    public JwtDecoder buildJwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeyProperties.rsaPublicKey()).build();
    }

    /**
     * Utilise les propriétés clé privé et public, ligne 1 et 2 du fichier application.properties, pour générer un encodeur
     * de token JWT et c'est ce qui permet de valider la signature du token et s'assurer que la donnée n'a pas été altérée.
     * @return JwtDecoder
     */
    @Bean
    public JwtEncoder buildJwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeyProperties.rsaPublicKey()).privateKey(rsaKeyProperties.rsaPrivateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type", "x-refresh-token"));
        configuration.setExposedHeaders(Arrays.asList("X-Get-Header"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
