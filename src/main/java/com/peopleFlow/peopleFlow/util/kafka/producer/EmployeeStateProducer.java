package com.peopleFlow.peopleFlow.util.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.peopleFlow.peopleFlow.model.dto.ChangeState;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeStateProducer {
    private final ReplyingKafkaTemplate<String, ChangeState, Boolean> requestReplyKafkaTemplate;

    @Value("${kafka.request.state}")
    private String REQ_TOPIC_SET;

    public Boolean sendEvent(ChangeState request) throws ExecutionException, InterruptedException {
        log.info("Producing event -> {} for employee with id={}", request.getEvent(), request.getEmployeeId());
        ProducerRecord<String, ChangeState> record = new ProducerRecord<>(REQ_TOPIC_SET, null, String.valueOf(request.getEmployeeId()), request);
        RequestReplyFuture<String, ChangeState, Boolean> future = requestReplyKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Boolean> response = future.get();
        return response.value() != null && response.value();
    }
}