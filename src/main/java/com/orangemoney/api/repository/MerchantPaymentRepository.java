package com.orangemoney.api.repository;

import com.orangemoney.api.entity.MerchantPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantPaymentRepository extends JpaRepository<MerchantPayment, Long> {

    Optional<MerchantPayment> findByReference(String reference);

    @Query("SELECT m FROM MerchantPayment m WHERE m.payerAccount.id = :accountId OR m.merchantAccount.id = :accountId ORDER BY m.createdAt DESC")
    List<MerchantPayment> findByAccountId(@Param("accountId") Long accountId);
}