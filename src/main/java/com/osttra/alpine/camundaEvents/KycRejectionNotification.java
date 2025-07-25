package com.osttra.alpine.camundaEvents;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KycRejectionNotification implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
    //TODO Email Notifcation
        log.info("Your KYC has been rejected. Kindly review and submit form again....");
        delegateExecution.setVariable("firstTimeSubmission",false);
    }
}
