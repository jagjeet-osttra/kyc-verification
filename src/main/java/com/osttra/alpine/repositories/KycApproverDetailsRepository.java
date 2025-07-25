package com.osttra.alpine.repositories;

import com.osttra.alpine.entities.KycApproverDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycApproverDetailsRepository extends JpaRepository<KycApproverDetails,Long> {
}
