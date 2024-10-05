package com.galaxy.trabajofinal.security.repository;

import com.galaxy.trabajofinal.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String username);

    @Query(value = "select u from User u where u.code2f=:code2f")
    Optional<User> findByCode(@Param("code2f") String code2f);

    @Modifying
    @Transactional
    @Query( nativeQuery = true,value = "UPDATE user SET code_2f = :code2f WHERE user_name = :userName")
    void updateCode2f(@Param("userName") String userName, @Param("code2f") String code2f);
}
