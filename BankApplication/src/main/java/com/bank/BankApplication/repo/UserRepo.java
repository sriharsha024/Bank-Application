package com.bank.BankApplication.repo;

import com.bank.BankApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);

    Optional<User> findByEmail(String username);
}
