package com.osttra.alpine.controllers;


import com.osttra.alpine.dtos.KycTasks;
import com.osttra.alpine.dtos.TaskCompletionRequestDto;
import com.osttra.alpine.services.KycHandlerService;
import lombok.AllArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/kyc")
@CrossOrigin(origins = "*")
public class KycController {

    @Autowired
    RuntimeService runtimeService;

    private final TaskService taskService;

    @Autowired
    RestTemplate restTemplate;

    private final KycHandlerService kycHandlerService;

    @GetMapping("/test")
    public ResponseEntity<String> testController()
    {
        throw new RuntimeException("Hey! Got the stuck!");
    }

    @PostMapping("/start")
    public ResponseEntity<String> startKyc()
    {
        Map<String,Object> variables = Map.of("firstTimeSubmission",true);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("Kyc-workflow",variables);
        return ResponseEntity.ok(instance.getId());
    }

    @GetMapping("/get-task-id/{processInstanceId}")
    public ResponseEntity<String> taskId(@PathVariable(name = "processInstanceId") String processInstanceId)
    {
        String camundaUrl = "http://localhost:8080/engine-rest/task?processInstanceId="+processInstanceId;
        ResponseEntity<KycTasks[]> response = restTemplate.getForEntity(camundaUrl, KycTasks[].class);
        KycTasks[] kycTasks = response.getBody();
        return ResponseEntity.ok(kycTasks[0].getId());
    }

    @GetMapping("/get-process-variables/{processInstanceId}")
    public ResponseEntity<Object> getProcessVariables(@PathVariable(name = "processInstanceId") String processInstanceId)
    {
        String camundaFetchVariablesUrl = "http://localhost:8080/engine-rest/process-instance/"+processInstanceId+"/variables";
        ResponseEntity<Object> response = restTemplate.getForEntity(camundaFetchVariablesUrl, Object.class);
        return ResponseEntity.ok(response.getBody());
    }
    @PostMapping("/complete-task/{taskId}")
    public ResponseEntity<String> completeTask(@PathVariable(name = "taskId")String taskId,
                                               @RequestBody TaskCompletionRequestDto taskCompletionRequestDto)
    {
        taskCompletionRequestDto.setTaskId(taskId);
        kycHandlerService.handleUserTask(taskCompletionRequestDto);
//        taskService.complete(taskId);
        return ResponseEntity.ok("Task has been completed!");
    }
}
