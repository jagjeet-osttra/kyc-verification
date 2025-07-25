package com.osttra.alpine.repositories;

import com.osttra.alpine.entities.KycData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KycDataRepository extends JpaRepository<KycData,Long> {
    Optional<KycData> findByProcessId(String processId);
}
