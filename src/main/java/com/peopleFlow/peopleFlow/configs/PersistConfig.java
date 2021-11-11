package com.peopleFlow.peopleFlow.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.StateMachinePersister;
import com.peopleFlow.peopleFlow.util.statemachine.event.EmployeeEvent;
import com.peopleFlow.peopleFlow.util.statemachine.state.EmployeeState;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;

@Configuration
public class PersistConfig {
    @Bean
    public StateMachinePersister<EmployeeState, EmployeeEvent, Long> persister(
            StateMachinePersist<EmployeeState, EmployeeEvent, Long> defaultPersist) {
        return new DefaultStateMachinePersister<>(defaultPersist);
    }

    @Bean
    public StateMachineRuntimePersister<EmployeeState, EmployeeEvent, Long> stateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }
}