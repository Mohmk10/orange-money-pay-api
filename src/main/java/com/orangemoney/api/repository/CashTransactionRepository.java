package com.orangemoney.api.repository;

import com.orangemoney.api.entity.CashTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashTransactionRepository extends JpaRepository<CashTransaction, Long> {

    Optional<CashTransaction> findByReference(String reference);

    List<CashTransaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}