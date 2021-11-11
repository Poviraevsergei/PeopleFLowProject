package com.peopleFlow.peopleFlow.util.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.peopleFlow.peopleFlow.model.dto.EmployeeDto;
import com.peopleFlow.peopleFlow.service.EmployeeService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeConsumer {

    private final EmployeeService employeeService;

    @KafkaListener(topics = "${kafka.request}", groupId = "${kafka.group.employee}")
    @SendTo()
    public EmployeeDto consume(EmployeeDto employee) {
        Optional<EmployeeDto> res = employeeService.addEmployee(employee);

        return res.orElse(null);
    }
}