package com.osttra.alpine.services.impl;

import com.osttra.alpine.dtos.TaskCompletionRequestDto;
import com.osttra.alpine.entities.KycApproverDetails;
import com.osttra.alpine.entities.KycData;
import com.osttra.alpine.repositories.KycApproverDetailsRepository;
import com.osttra.alpine.repositories.KycDataRepository;
import com.osttra.alpine.services.KycDataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class KycDataServiceImpl implements KycDataService {
    private final KycDataRepository kycDataRepository;
    private final KycApproverDetailsRepository kycApproverDetailsRepository;
    @Override
        public void submitKycData(TaskCompletionRequestDto taskCompletionRequestDto) {
            KycData kycData = kycDataRepository
                    .findByProcessId(taskCompletionRequestDto.getProcessId()).orElse(null);
            if(kycData==null)
                kycData = KycData.builder().processId(taskCompletionRequestDto.getProcessId())
                    .data(taskCompletionRequestDto.getFormData()).build();
            else
                kycData.setData(taskCompletionRequestDto.getFormData());
            kycDataRepository.save(kycData);
        }

    @Override
    public void reviewKycData(TaskCompletionRequestDto taskCompletionRequestDto) {
    KycData kycData = kycDataRepository.findByProcessId(taskCompletionRequestDto.getProcessId())
            .orElseThrow(()->new RuntimeException("Process not found with id: "+taskCompletionRequestDto.getTaskId()));
      Boolean isApproved = (Boolean) taskCompletionRequestDto.getFormData().get("isApproved");
    KycApproverDetails kycApproverDetails = KycApproverDetails.builder()
            .kycData(kycData)
            .isApproved(isApproved)
            .comments((String)taskCompletionRequestDto.getFormData().get("comments"))
            .processId(kycData.getProcessId())
            .localDateTime(LocalDateTime.now()).build();
    kycApproverDetailsRepository.save(kycApproverDetails);
    }
}
