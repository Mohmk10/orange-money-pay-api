package com.orangemoney.api.repository;

import com.orangemoney.api.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Optional<Transfer> findByReference(String reference);

    @Query("SELECT t FROM Transfer t WHERE t.senderAccount.id = :accountId OR t.receiverAccount.id = :accountId ORDER BY t.createdAt DESC")
    List<Transfer> findByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT COALESCE(SUM(t.totalAmount), 0) FROM Transfer t " +
            "WHERE t.senderAccount.id = :accountId " +
            "AND t.status = 'COMPLETED' " +
            "AND t.createdAt >= :startOfDay")
    BigDecimal getTotalSentToday(@Param("accountId") Long accountId, @Param("startOfDay") LocalDateTime startOfDay);

    @Query("SELECT COUNT(t) FROM Transfer t " +
            "WHERE t.senderAccount.id = :accountId " +
            "AND t.status = 'COMPLETED' " +
            "AND t.amount BETWEEN :minAmount AND :maxAmount " +
            "AND t.createdAt >= :startOfDay")
    Long countFreeTransfersToday(@Param("accountId") Long accountId,
                                 @Param("minAmount") BigDecimal minAmount,
                                 @Param("maxAmount") BigDecimal maxAmount,
                                 @Param("startOfDay") LocalDateTime startOfDay);
}