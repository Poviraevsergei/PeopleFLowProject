package com.peopleFlow.peopleFlow.service;

import com.peopleFlow.peopleFlow.model.dto.ChangeState;
import com.peopleFlow.peopleFlow.util.statemachine.state.EmployeeState;

import java.util.Optional;

public interface EmployeeStateService {

    boolean sendEvent(ChangeState request);

    Optional<EmployeeState> getStateForEmployee(Long employeeId);

    void createStatemachineForEmployee(Long employeeId);
}
