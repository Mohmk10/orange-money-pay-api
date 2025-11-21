package com.orangemoney.api.repository;

import com.orangemoney.api.dto.projection.UserSummaryDTO;
import com.orangemoney.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    @Query("SELECT new com.orangemoney.api.dto.projection.UserSummaryDTO(" +
            "u.id, u.firstName, u.lastName, u.phoneNumber, a.accountNumber) " +
            "FROM User u " +
            "LEFT JOIN u.account a " +
            "WHERE u.id = :userId")
    Optional<UserSummaryDTO> findUserSummaryById(@Param("userId") Long userId);

    @Query("SELECT new com.orangemoney.api.dto.projection.UserSummaryDTO(" +
            "u.id, u.firstName, u.lastName, u.phoneNumber, a.accountNumber) " +
            "FROM User u " +
            "LEFT JOIN u.account a " +
            "WHERE u.active = true")
    List<UserSummaryDTO> findAllActiveUsersSummary();
}