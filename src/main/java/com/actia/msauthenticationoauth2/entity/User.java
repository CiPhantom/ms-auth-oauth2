package com.actia.msauthenticationoauth2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "USER_MATRICULE")
public class User {

    @Id
    private String matricule;

    @JoinTable(
            name="USER_ROLE",
            joinColumns = @JoinColumn(name = "matricule"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    @ManyToMany(fetch = FetchType.EAGER) //Explorer la possibilité du Lazy, simplification en EAGER
    private Set<Role> roles;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    private long version;

    public User(String matricule, Set<Role> roles) {
        this.matricule = matricule;
        this.roles = roles;
    }

    public User(String matricule) {
        this.matricule = matricule;
    }

    public User() {
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
