package com.actia.msauthenticationoauth2.repository;

import com.actia.msauthenticationoauth2.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
