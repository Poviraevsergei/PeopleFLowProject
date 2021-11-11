package com.peopleFlow.peopleFlow.util.statemachine.listener;

import com.peopleFlow.peopleFlow.util.statemachine.event.EmployeeEvent;
import com.peopleFlow.peopleFlow.util.statemachine.state.EmployeeState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmployeeStateListener extends StateMachineListenerAdapter<EmployeeState, EmployeeEvent> {
    @Override
    public void stateChanged(State<EmployeeState, EmployeeEvent> state, State<EmployeeState, EmployeeEvent> state1) {
        log.info("State changed from {} to {}", state == null ? "NULL" : state.getId(), state1.getId());
    }

    @Override
    public void stateMachineStarted(StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
        log.info("State machine started");
    }

    @Override
    public void stateMachineStopped(StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
        log.info("State machine stopped");
    }

    @Override
    public void eventNotAccepted(Message<EmployeeEvent> event) {
        log.info("Event not accepted: " + event);
    }

    @Override
    public void stateMachineError(StateMachine<EmployeeState, EmployeeEvent> stateMachine, Exception exception) {
        log.error(String.valueOf(exception));
    }
}