package com.orangemoney.api.repository;

import com.orangemoney.api.entity.BankTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankTransferRepository extends JpaRepository<BankTransfer, Long> {

    Optional<BankTransfer> findByReference(String reference);

    List<BankTransfer> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}