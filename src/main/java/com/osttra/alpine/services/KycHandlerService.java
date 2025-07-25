package com.osttra.alpine.services;

import com.osttra.alpine.dtos.TaskCompletionRequestDto;

public interface KycHandlerService {
    void handleUserTask(TaskCompletionRequestDto taskCompletionRequestDto);
}
