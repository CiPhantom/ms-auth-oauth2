package com.actia.msauthenticationoauth2.service;



import com.actia.msauthenticationoauth2.entity.Role;
import com.actia.msauthenticationoauth2.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository repository) {
        roleRepository = repository;
    }



    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
