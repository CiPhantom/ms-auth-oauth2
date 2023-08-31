package com.actia.msauthenticationoauth2.securityconfig.providers;

import com.actia.msauthenticationoauth2.entity.User;
import com.actia.msauthenticationoauth2.securityconfig.data.UserMatriculeResponse;
import com.actia.msauthenticationoauth2.service.RoleService;
import com.actia.msauthenticationoauth2.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
public class AgcAuthenticationProvider implements AuthenticationProvider {

    private RestTemplate restTemplate = new RestTemplate();

    private final RoleService roleService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final UserService userService;

    public AgcAuthenticationProvider(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var auth = (UsernamePasswordAuthenticationToken) authentication;
            //CONNEXION SYNAPSE
        String response = restTemplate.postForObject("http://localhost:3001/synapse/ldap/user/"+ authentication.getName(), null, String.class);
        try {
            UserMatriculeResponse matricule = objectMapper.reader().forType(UserMatriculeResponse.class).readValue(response);
            if(StringUtils.hasLength(matricule.getMatricule())) {
                User user = userService.findUserByMatricule(matricule.getMatricule());
                var grantedRoles = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole())).toList();
                return new UsernamePasswordAuthenticationToken(matricule.getMatricule(), null, grantedRoles);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
