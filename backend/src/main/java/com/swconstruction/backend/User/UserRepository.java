package com.swconstruction.backend.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);

    @Query(value = "ALTER TABLE user AUTO_INCREMENT=1",nativeQuery = true)
    void resetUsers();


}
