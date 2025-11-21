package com.orangemoney.api.repository;

import com.orangemoney.api.entity.LinkedBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkedBankAccountRepository extends JpaRepository<LinkedBankAccount, Long> {

    List<LinkedBankAccount> findByUserIdAndActiveTrue(Long userId);

    List<LinkedBankAccount> findByUserId(Long userId);
}