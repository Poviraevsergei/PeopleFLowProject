package com.peopleFlow.peopleFlow.util.statemachine.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import com.peopleFlow.peopleFlow.util.statemachine.event.EmployeeEvent;
import com.peopleFlow.peopleFlow.util.statemachine.state.EmployeeState;

@Slf4j
public class AddedAction implements Action<EmployeeState, EmployeeEvent> {

    @Override
    public void execute(StateContext<EmployeeState, EmployeeEvent> stateContext) {
        final Long employeeId = stateContext.getExtendedState().get("employeeId", Long.class);
        log.info("Employee id={} in state {}", employeeId, stateContext.getEvent());
    }
}