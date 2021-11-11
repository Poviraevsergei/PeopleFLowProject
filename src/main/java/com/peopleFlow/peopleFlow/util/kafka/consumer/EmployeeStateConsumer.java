package com.peopleFlow.peopleFlow.util.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.peopleFlow.peopleFlow.model.dto.ChangeState;
import org.springframework.kafka.annotation.KafkaListener;
import com.peopleFlow.peopleFlow.service.EmployeeStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeStateConsumer {

    private final EmployeeStateService employeeStateService;

    @KafkaListener(topics = "${kafka.request.state}", groupId = "${kafka.group.state}")
    @SendTo()
    public boolean consume(ChangeState request) {

        return employeeStateService.sendEvent(request);
    }
}