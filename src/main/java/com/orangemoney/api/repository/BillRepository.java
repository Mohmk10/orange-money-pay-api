package com.orangemoney.api.repository;

import com.orangemoney.api.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findByReference(String reference);

    List<Bill> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}