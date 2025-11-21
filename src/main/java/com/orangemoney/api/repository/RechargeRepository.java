package com.orangemoney.api.repository;

import com.orangemoney.api.entity.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {

    Optional<Recharge> findByReference(String reference);

    List<Recharge> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}