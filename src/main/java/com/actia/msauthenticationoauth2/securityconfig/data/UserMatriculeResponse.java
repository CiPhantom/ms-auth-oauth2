package com.actia.msauthenticationoauth2.securityconfig.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserMatriculeResponse {
    @JsonProperty("matricule")
    private String matricule;

    public UserMatriculeResponse() {
    }

    public UserMatriculeResponse(String matricule) {
        this.matricule = matricule;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
}