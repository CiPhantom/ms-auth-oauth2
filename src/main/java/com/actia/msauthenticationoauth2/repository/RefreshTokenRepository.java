package com.actia.msauthenticationoauth2.repository;

import com.actia.msauthenticationoauth2.entity.RefreshToken;
import com.actia.msauthenticationoauth2.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    void deleteByUser(User user);

    RefreshToken findByUser(User user);

    RefreshToken findRefreshTokenByRefreshToken(String refreshTokenString);
}
