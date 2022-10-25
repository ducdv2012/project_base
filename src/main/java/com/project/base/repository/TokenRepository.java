package com.project.base.repository;

import com.project.base.model.Tokens;
import com.project.base.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Tokens, Long> {
    Tokens findByToken(String token);

    @Modifying
    @Query("DELETE FROM Tokens WHERE id = :id ")
    @Transactional
    void deleteToken(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Tokens t WHERE t.id IN (:tokenIds)")
    void deleteAllByTokens(List<Long> tokenIds);

    List<Tokens> findAllByUsers(Users users);
}
