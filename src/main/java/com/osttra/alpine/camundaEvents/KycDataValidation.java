package com.osttra.alpine.camundaEvents;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KycDataValidation implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
    //TODO Data Validation Logic
    log.info("Data Validation Step....");
    delegateExecution.setVariable("validationSuccess",true);
    }
}
