package com.osttra.alpine.services;

import com.osttra.alpine.dtos.TaskCompletionRequestDto;
import com.osttra.alpine.entities.KycApproverDetails;
import com.osttra.alpine.entities.KycData;
import com.osttra.alpine.repositories.KycApproverDetailsRepository;
import com.osttra.alpine.repositories.KycDataRepository;
import com.osttra.alpine.services.impl.KycDataServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KycDataServiceImplTest {

    @Mock
    KycDataRepository kycDataRepository;

    @Mock
    KycApproverDetailsRepository kycApproverDetailsRepository;
    @InjectMocks
    KycDataServiceImpl kycDataService;

    TaskCompletionRequestDto taskCompletionRequestDto;
    KycData kycData;
    KycApproverDetails kycApproverDetails;

    void setUpTaskToSubmitKycDataFirstTime()
    {
        Map<String,Object> data = Map.of("legalName","JP Morgan");
        taskCompletionRequestDto = TaskCompletionRequestDto.builder()
                .taskId("123-4654-2365")
                .processId("416353tf-32638-87623h")
                .formData(data).build();
    }

    void setUpTaskToSubmitKycDataAgain()
    {
        Map<String,Object> data = Map.of("isApproved",false,"comments","Not Good to go!..");
        taskCompletionRequestDto = TaskCompletionRequestDto.builder()
                .taskId("123-4654-2365")
                .processId("416353tf-32638-87623h")
                .formData(data).build();
        kycApproverDetails = KycApproverDetails.builder()
                .id(1L)
                .processId(taskCompletionRequestDto.getProcessId())
                .isApproved((Boolean)data.get("isApproved"))
                .localDateTime(LocalDateTime.now()).comments((String)data.get("comments"))
                .build();

        kycData = KycData.builder().processId(taskCompletionRequestDto.getProcessId())
                                .id(1L).data(data)
                .kycApproverDetails(List.of(kycApproverDetails)).build();
    }

    void setUpTaskToReviewKycData()
    {
        Map<String,Object> data = Map.of("isApproved",true,"comments","Good to go!..");
        taskCompletionRequestDto = TaskCompletionRequestDto.builder()
                .taskId("123-4654-2365")
                .processId("416353tf-32638-87623h")
                .formData(data).build();
        kycApproverDetails = KycApproverDetails.builder()
                .id(1L)
                .processId(taskCompletionRequestDto.getProcessId())
                .isApproved((Boolean)data.get("isApproved"))
                .localDateTime(LocalDateTime.now()).comments((String)data.get("comments"))
                .build();

        kycData = KycData.builder().processId(taskCompletionRequestDto.getProcessId())
                .id(1L).data(data)
                .kycApproverDetails(List.of(kycApproverDetails)).build();
    }

    @Test
    void testSubmitKycData_whenKycDataIsSubmittedFirstTime_thenStoreDataInDatabase()
    {
        //arrange
        setUpTaskToSubmitKycDataFirstTime();
        when(kycDataRepository.findByProcessId(anyString())).thenReturn(Optional.empty());
        ArgumentCaptor<KycData> kycDataArgumentCaptor = ArgumentCaptor.forClass(KycData.class);
        //act
        kycDataService.submitKycData(taskCompletionRequestDto);

        //assert
        verify(kycDataRepository,times(1)).findByProcessId(taskCompletionRequestDto.getProcessId());
        verify(kycDataRepository,times(1)).save(kycDataArgumentCaptor.capture());
        KycData savedKycData = kycDataArgumentCaptor.getValue();
        assertEquals(taskCompletionRequestDto.getProcessId(),savedKycData.getProcessId());
    }

    @Test
    void testSubmitKycData_whenKycDataIsSubmittedAgain_thenStoreDataInDatabase()
    {
        //arrange
        setUpTaskToSubmitKycDataAgain();
        when(kycDataRepository.findByProcessId(anyString())).thenReturn(Optional.of(kycData));

        //act
        kycDataService.submitKycData(taskCompletionRequestDto);

        //assert
        assertEquals(false, kycData.getKycApproverDetails().get(0).getIsApproved());
        assertEquals(taskCompletionRequestDto.getFormData().get("comments"),kycData.getKycApproverDetails().get(0).getComments());
        verify(kycDataRepository,times(1)).findByProcessId(taskCompletionRequestDto.getProcessId());
        verify(kycDataRepository,times(1)).save(kycData);

    }

    @Test
    void testReviewKycData_whenReviewIsSubmitted_thenStoreInDatabase()
    {
        //arrange
        setUpTaskToReviewKycData();
        when(kycDataRepository.findByProcessId(anyString())).thenReturn(Optional.of(kycData));
        ArgumentCaptor<KycApproverDetails> kycApproverDetailsArgumentCaptor = ArgumentCaptor.forClass(KycApproverDetails.class);

        //act
        kycDataService.reviewKycData(taskCompletionRequestDto);

        //assert
        verify(kycApproverDetailsRepository,times(1)).save(kycApproverDetailsArgumentCaptor.capture());
        KycApproverDetails savedKycApproverDetails = kycApproverDetailsArgumentCaptor.getValue();
        assertEquals(savedKycApproverDetails.getIsApproved(),taskCompletionRequestDto.getFormData().get("isApproved"));

    }

}
