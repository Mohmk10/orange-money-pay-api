package com.orangemoney.api.repository;

import com.orangemoney.api.dto.projection.AccountBalanceProjection;
import com.orangemoney.api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByUserId(Long userId);

    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT a.accountNumber AS accountNumber, " +
            "a.balance AS balance, " +
            "a.dailyLimit AS dailyLimit, " +
            "a.status AS status " +
            "FROM Account a " +
            "WHERE a.user.id = :userId")
    Optional<AccountBalanceProjection> findBalanceByUserId(@Param("userId") Long userId);
}