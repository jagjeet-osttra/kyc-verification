package com.osttra.alpine.services.impl;

import com.osttra.alpine.dtos.TaskCompletionRequestDto;
import com.osttra.alpine.exceptions.TaskDefinitionNotFound;
import com.osttra.alpine.repositories.KycDataRepository;
import com.osttra.alpine.services.KycDataService;
import com.osttra.alpine.services.KycHandlerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class KycHandlerServiceImpl implements KycHandlerService {

    private final ModelMapper modelMapper;
    private final KycDataService kycDataService;
    private final TaskService taskService;


    @Override
    public void handleUserTask(TaskCompletionRequestDto taskCompletionRequestDto) {
        Task task = taskService.createTaskQuery()
                .taskId(taskCompletionRequestDto.getTaskId()).singleResult();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        String processId = task.getProcessInstanceId();
        log.info("Task Definition name: {}",taskDefinitionKey);;
        log.info("Process Id: {}",processId);;
        taskCompletionRequestDto.setProcessId(processId);
        switch (taskDefinitionKey)
        {
            case "SubmitKycData", "ResubmitKycData":
                kycDataService.submitKycData(taskCompletionRequestDto);
                taskService.complete(taskCompletionRequestDto.getTaskId());
                break;
            case "ReviewKycData":
                Boolean isApproved = (Boolean) taskCompletionRequestDto.getFormData().get("isApproved");
                kycDataService.reviewKycData(taskCompletionRequestDto);
                Map<String,Object> variables = Map.of("approved",isApproved);
                taskService.complete(taskCompletionRequestDto.getTaskId(),variables);
                break;
            default:
                throw new TaskDefinitionNotFound("Task Definition Not Found!");
        }
    }
}
