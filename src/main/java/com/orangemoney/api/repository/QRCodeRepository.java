package com.orangemoney.api.repository;

import com.orangemoney.api.entity.QRCodeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCodeData, Long> {

    Optional<QRCodeData> findByQrToken(String qrToken);
}