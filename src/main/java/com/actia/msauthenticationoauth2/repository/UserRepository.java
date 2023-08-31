package com.actia.msauthenticationoauth2.repository;

import com.actia.msauthenticationoauth2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByMatricule(String matricule);
}
