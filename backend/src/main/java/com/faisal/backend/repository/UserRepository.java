package com.faisal.backend.repository;

import com.faisal.backend.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInformation, Long> {

    Optional<UserInformation> findByUsername(String username);
}
