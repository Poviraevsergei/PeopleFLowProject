package com.peopleFlow.peopleFlow.util.kafka.producer;


import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.peopleFlow.peopleFlow.model.dto.EmployeeDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeProducer {

    private final ReplyingKafkaTemplate<String, EmployeeDto, EmployeeDto> requestReplyKafkaTemplate;

    @Value("${kafka.request}")
    private String reqTopic;

    public Optional<EmployeeDto> sendMessage(EmployeeDto message) throws ExecutionException, InterruptedException {
        log.info(String.format("Sending message -> %s", message));

        ProducerRecord<String, EmployeeDto> record = new ProducerRecord<>(reqTopic, null, "employee", message);
        RequestReplyFuture<String, EmployeeDto, EmployeeDto> future = requestReplyKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, EmployeeDto> response = future.get();
        return Optional.of(response.value());
    }
}