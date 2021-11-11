package com.peopleFlow.peopleFlow.service;

import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import com.peopleFlow.peopleFlow.model.dto.ChangeState;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import com.peopleFlow.peopleFlow.util.statemachine.event.EmployeeEvent;
import com.peopleFlow.peopleFlow.util.statemachine.state.EmployeeState;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
class EmployeeStateServiceImplTest {

    final Long firstEmployeeId = 1L;

    @Autowired
    private EmployeeStateService employeeStateService;

    @Test
    void testStateMachinePersisting() {
        final Long secondEmployeeId = 2L;
        employeeStateService.createStatemachineForEmployee(firstEmployeeId);
        employeeStateService.createStatemachineForEmployee(firstEmployeeId + 1);
        employeeStateService.sendEvent(ChangeState.builder().employeeId(1L).event(EmployeeEvent.INCHECK_EVENT).build());

        Optional<EmployeeState> firstEmployeeState = employeeStateService.getStateForEmployee(firstEmployeeId);
        Optional<EmployeeState> secondEmployeeState = employeeStateService.getStateForEmployee(secondEmployeeId);

        assertThat(firstEmployeeState.isPresent()).isTrue();
        assertThat(firstEmployeeState.get()).isEqualTo(EmployeeState.INCHECK);
        assertThat(secondEmployeeState.isPresent()).isTrue();
        assertThat(secondEmployeeState.get()).isEqualTo(EmployeeState.ADDED);
    }
}