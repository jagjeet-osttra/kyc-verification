package com.osttra.alpine.camundaEvents;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResubmissionDataNotification implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
    //TODO Email Notification Reminder to resubmit data
        log.info("Notification: Your KYC has not approved as we confirmed earlier. Please review it once and send the data again!");
    }
}
