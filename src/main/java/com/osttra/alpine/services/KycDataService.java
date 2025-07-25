package com.osttra.alpine.services;

import com.osttra.alpine.dtos.TaskCompletionRequestDto;

public interface KycDataService {
    void submitKycData(TaskCompletionRequestDto taskCompletionRequestDto);

    void reviewKycData(TaskCompletionRequestDto taskCompletionRequestDto);
}
