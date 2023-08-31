package com.actia.msauthenticationoauth2.runner;

import com.actia.msauthenticationoauth2.entity.Role;
import com.actia.msauthenticationoauth2.entity.User;
import com.actia.msauthenticationoauth2.service.RoleService;
import com.actia.msauthenticationoauth2.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

/*
* L'objectif de ce runner est d'initialiser des données de tests en base de données pour tester les droits d'accès.
* */
@Component
public class InitInsertDatabaseRunner implements ApplicationRunner {
    private final RoleService roleService;
    private final UserService userService;
    public InitInsertDatabaseRunner(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        //CREATE ROLES
        Role a = new Role("ROLE_A");
        Role b = new Role("ROLE_B");
        Role c = new Role("ROLE_C");
        Role d = new Role("ROLE_D");
        Role e = new Role("ROLE_E");
        Role f = new Role("ROLE_F");
        Role g = new Role("ROLE_G");

        roleService.saveRole(a);
        roleService.saveRole(b);
        roleService.saveRole(c);
        roleService.saveRole(d);
        roleService.saveRole(f);
        roleService.saveRole(e);
        roleService.saveRole(g);

        //CREATE USER
        User user1 = new User("EG238383");
        User user2 = new User("AZ002");

        user1.setRoles(Set.of(a,b, c, d, e, f, g));
        user2.setRoles(Set.of(b,c));
        userService.registerUser(user1);
        userService.registerUser(user2);
    }
}
