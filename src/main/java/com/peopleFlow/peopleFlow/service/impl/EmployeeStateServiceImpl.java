package com.peopleFlow.peopleFlow.service.impl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.region.Region;
import com.peopleFlow.peopleFlow.model.dto.ChangeState;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.peopleFlow.peopleFlow.service.EmployeeStateService;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import com.peopleFlow.peopleFlow.util.statemachine.event.EmployeeEvent;
import com.peopleFlow.peopleFlow.util.statemachine.state.EmployeeState;

import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeStateServiceImpl implements EmployeeStateService {
    final ReadWriteLock locker = new ReentrantReadWriteLock();

    private final StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;
    private final StateMachinePersister<EmployeeState, EmployeeEvent, Long> persister;

    @Override
    public Optional<EmployeeState> getStateForEmployee(Long employeeId) {
        return Optional.ofNullable(getPersistedStateMachine(employeeId))
                .map(Region::getState)
                .map(State::getId);
    }

    @Override
    public boolean sendEvent(ChangeState request) {
        Mono<Message<EmployeeEvent>> mono = Mono.just(MessageBuilder.withPayload(request.getEvent()).build());
        final StateMachine<EmployeeState, EmployeeEvent> stateMachine = getPersistedStateMachine(request.getEmployeeId());
        final Boolean[] result = new Boolean[1];
        if (stateMachine != null) {
            stateMachine.sendEvent(mono).subscribe(response -> {
                if (response.getResultType().equals(StateMachineEventResult.ResultType.ACCEPTED)) {
                    result[0] = true;
                }
            });
            if (result[0]) {
                stateMachine.getExtendedState().getVariables().put("employeeId", request.getEmployeeId());
                saveStateMachineToStorage(request.getEmployeeId(), stateMachine);
            }
        }

        return result[0];
    }

    private StateMachine<EmployeeState, EmployeeEvent> getPersistedStateMachine(Long employeeId) {
        StateMachine<EmployeeState, EmployeeEvent> stateMachine = stateMachineFactory.getStateMachine();
        locker.readLock().lock();
        try {
            stateMachine = persister.restore(stateMachine, employeeId);
            return stateMachine.getId() == null ? null : stateMachine;
        } catch (Exception e) {
            log.error("Unable to restore state machine with uuid ={} and employee id = {}", stateMachine.getUuid(), employeeId);
        } finally {
            locker.readLock().unlock();
        }
        return null;
    }

    @Override
    public void createStatemachineForEmployee(Long employeeId) {
        final StateMachine<EmployeeState, EmployeeEvent> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(employeeId));
        saveStateMachineToStorage(employeeId, stateMachine);
    }

    private void saveStateMachineToStorage(Long employeeId, StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
        locker.writeLock().lock();
        try {
            stateMachine.getExtendedState().getVariables().put("employeeId", employeeId);
            persister.persist(stateMachine, employeeId);
        } catch (Exception e) {
            log.error("Unable to persist state machine with uuid ={} and employee id = {}", stateMachine.getUuid(), employeeId);
        } finally {
            locker.writeLock().unlock();
        }
    }
}