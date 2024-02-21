package com.mindera.api.repository;

import com.mindera.api.domain.User;
import com.mindera.api.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByGender(Gender gender);
    Optional<User> findByVatNumber(String vatNumber);
    Optional<User> findByUsernameAndPassword(String username, String password);
}
