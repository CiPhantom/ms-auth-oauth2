package com.actia.msauthenticationoauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ButtonActions {

    /**
     * Juste des méthodes pour testers les droits d'accès sur un endpoints.
     * @param authentication
     */
    @PreAuthorize("hasAuthority(\"SCOPE_ROLE_A\")")
    @GetMapping("/actions/outline")
    public void outlineButtonAction(Authentication authentication) {
        System.out.println("Action from Outline button, with : " + authentication.getName());
    }
}
