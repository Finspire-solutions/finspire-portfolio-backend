package com.finspire.repository;


import com.finspire.entity.Token;
import com.finspire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {

    Optional<Token> findByToken(String token);

    Optional<Token> findByUserEmail(String email);

    Optional<Token> findByUser(User user);
}
