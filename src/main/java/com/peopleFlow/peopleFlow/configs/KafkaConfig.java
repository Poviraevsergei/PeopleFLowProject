package com.peopleFlow.peopleFlow.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import com.peopleFlow.peopleFlow.model.dto.ChangeState;
import com.peopleFlow.peopleFlow.model.dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
public class KafkaConfig {
    @Value("${kafka.group.employee}")
    private String groupIdEmpl;
    @Value("${kafka.group.state}")
    private String groupIdState;
    @Value("${kafka.reply}")
    private String replyTopic;
    @Value("${kafka.reply.state}")
    private String replyTopicState;

    @Bean
    public ReplyingKafkaTemplate<String, EmployeeDto, EmployeeDto> replyingKafkaTemplate(ProducerFactory<String, EmployeeDto> pf,
                                                                                         ConcurrentKafkaListenerContainerFactory<String, EmployeeDto> factory) {
        ConcurrentMessageListenerContainer<String, EmployeeDto> replyContainer = factory.createContainer(replyTopic);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(groupIdEmpl);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public ReplyingKafkaTemplate<String, ChangeState, Boolean> replyingKafkaTemplateState(ProducerFactory<String, ChangeState> pf,
                                                                                          ConcurrentKafkaListenerContainerFactory<String, Boolean> factory) {
        ConcurrentMessageListenerContainer<String, Boolean> replyContainer = factory.createContainer(replyTopicState);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(groupIdState);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public KafkaTemplate<String, EmployeeDto> replyTemplate(ProducerFactory<String, EmployeeDto> producerFactory,
                                                            ConcurrentKafkaListenerContainerFactory<String, EmployeeDto> factory) {
        KafkaTemplate<String, EmployeeDto> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, ChangeState> replyTemplateState(ProducerFactory<String, ChangeState> producerFactory,
                                                                 ConcurrentKafkaListenerContainerFactory<String, ChangeState> factory) {
        KafkaTemplate<String, ChangeState> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }
}