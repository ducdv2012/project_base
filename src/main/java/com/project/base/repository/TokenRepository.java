package com.project.base.repository;

import com.project.base.model.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Tokens, Long> {
    Tokens findByToken(String token);

    @Modifying
    @Query("DELETE FROM Tokens WHERE id = :id ")
    @Transactional
    void deleteToken(Long id);
}
