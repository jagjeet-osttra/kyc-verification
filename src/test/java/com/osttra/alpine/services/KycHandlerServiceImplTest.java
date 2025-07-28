package com.osttra.alpine.services;

import com.osttra.alpine.dtos.TaskCompletionRequestDto;
import com.osttra.alpine.exceptions.TaskDefinitionNotFound;
import com.osttra.alpine.services.impl.KycHandlerServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KycHandlerServiceImplTest {

    @Mock
    TaskService taskService;

    @Mock
    KycDataService kycDataService;

    @InjectMocks
    KycHandlerServiceImpl kycHandlerService;

    TaskCompletionRequestDto taskCompletionRequestDto;
    TaskQuery mockTaskQuery = Mockito.mock(TaskQuery.class);
    Task mockTask = Mockito.mock(Task.class);

    void setUpDataForSubmitKyc()
    {
        Map<String,Object> data = Map.of("legalName","JP Morgan");
        taskCompletionRequestDto = TaskCompletionRequestDto.builder()
                .taskId("12J3573-2663v-36827").formData(data).build();
    }

    void setUpDataForReviewKycData()
    {
        Map<String,Object> data = Map.of("isApproved",true,"comments","Good to go!");
        taskCompletionRequestDto = TaskCompletionRequestDto.builder()
                .taskId("3363-75273-536787").formData(data).build();
    }

    @Test
    void testHandleUserTask_whenTaskIsSubmitKycData_thenStoreKycData()
    {
        //arrange
        setUpDataForSubmitKyc();
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId("12J3573-2663v-36827")).thenReturn(mockTaskQuery); // Returns itself
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(mockTask.getTaskDefinitionKey()).thenReturn("SubmitKycData");
        when(mockTask.getProcessInstanceId()).thenReturn("12");

        //act
        kycHandlerService.handleUserTask(taskCompletionRequestDto);

        //assert
        assertEquals("12", taskCompletionRequestDto.getProcessId());
        verify(taskService,times(1)).createTaskQuery();
        verify(taskService,times(1)).complete("12J3573-2663v-36827");
        verify(kycDataService,atLeast(1)).submitKycData(taskCompletionRequestDto);

    }
    @Test
    void testHandleUserTask_whenTaskIsReSubmitKycData_thenKycDataIsUpdated()
    {
        //arrange
        setUpDataForSubmitKyc();
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId("12J3573-2663v-36827")).thenReturn(mockTaskQuery); // Returns itself
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(mockTask.getTaskDefinitionKey()).thenReturn("ResubmitKycData");
        when(mockTask.getProcessInstanceId()).thenReturn("34");

        //act
        kycHandlerService.handleUserTask(taskCompletionRequestDto);

        //assert
        assertEquals("34", taskCompletionRequestDto.getProcessId());
        assertEquals("ResubmitKycData", mockTask.getTaskDefinitionKey());
        verify(taskService,times(1)).createTaskQuery();
        verify(taskService,times(1)).complete("12J3573-2663v-36827");
        verify(kycDataService,atLeast(1)).submitKycData(taskCompletionRequestDto);

    }

    @Test
    void testHandleUserTask_whenTaskIsReviewKycData_thenApproveKyc()
    {
        //arrange
        setUpDataForReviewKycData();
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId("3363-75273-536787")).thenReturn(mockTaskQuery); // Returns itself
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(mockTask.getTaskDefinitionKey()).thenReturn("ReviewKycData");
        when(mockTask.getProcessInstanceId()).thenReturn("45");

        //act
        kycHandlerService.handleUserTask(taskCompletionRequestDto);

        //assert
        assertEquals("45", taskCompletionRequestDto.getProcessId());
        assertEquals("ReviewKycData", mockTask.getTaskDefinitionKey());
        verify(taskService,times(1)).createTaskQuery();
        verify(taskService,times(1)).complete(eq("3363-75273-536787"),anyMap());
        verify(kycDataService,atLeast(1)).reviewKycData(taskCompletionRequestDto);
    }

    @Test
    void testHandleUserTask_whenTaskIsUndefined_thenThrowException()
    {
        //arrange
        setUpDataForReviewKycData();
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId("3363-75273-536787")).thenReturn(mockTaskQuery); // Returns itself
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(mockTask.getTaskDefinitionKey()).thenReturn("SomeUndefinedTask");
        when(mockTask.getProcessInstanceId()).thenReturn("45");

        //act & assert
        assertThatThrownBy(()-> kycHandlerService.handleUserTask(taskCompletionRequestDto)
        ).isInstanceOf(TaskDefinitionNotFound.class)
                .hasMessage("Task Definition Not Found!");
    }



}
