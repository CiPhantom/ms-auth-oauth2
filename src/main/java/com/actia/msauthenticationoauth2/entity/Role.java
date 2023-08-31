package com.actia.msauthenticationoauth2.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "ROLE")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer roleId;

    private String role;

    @ManyToMany(mappedBy = "roles",cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private Set<User> users;

    private Long version;

    public Role(String role) {
        this.role = role;
    }

    public Role(Integer roleId, String role, Set<User> users) {
        this.roleId = roleId;
        this.role = role;
        this.users = users;
    }

    public Role() {
    }

    public Role(String role, Set<User> users) {
        this.role = role;
        this.users = users;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
