package com.yenyasoft.email_plus_otp.repositories;

import com.yenyasoft.email_plus_otp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Author: Subash
 * Created: 8/25/2026
 * File: UserRepository.java
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);

}
