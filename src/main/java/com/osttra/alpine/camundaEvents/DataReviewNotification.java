package com.osttra.alpine.camundaEvents;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataReviewNotification implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
    //TODO Send Remainder Notification to review the data
        log.info("Notification: There are tasks in your bucket list! Kindly check!");
    }
}
