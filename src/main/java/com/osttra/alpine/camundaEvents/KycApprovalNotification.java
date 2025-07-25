package com.osttra.alpine.camundaEvents;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KycApprovalNotification implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
    //TODO Email Notification
        log.info("Your KYC has been confirmed....");
        delegateExecution.setVariable("approved",true);
    }
}
