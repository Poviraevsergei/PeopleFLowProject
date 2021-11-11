package com.peopleFlow.peopleFlow.service.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.peopleFlow.peopleFlow.model.Employee;
import com.peopleFlow.peopleFlow.model.dto.EmployeeDto;
import com.peopleFlow.peopleFlow.service.EmployeeService;
import com.peopleFlow.peopleFlow.service.EmployeeStateService;
import com.peopleFlow.peopleFlow.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import javax.transaction.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeStateService employeeStateService;

    @Override
    @Transactional
    public Optional<EmployeeDto> addEmployee(EmployeeDto employee) {
        final Optional<Employee> savedEmployee = Optional.of(employeeRepository.save(mapEmployee(employee)));
        log.info("Save employee " + savedEmployee);
        savedEmployee.map(Employee::getId).ifPresent(employeeStateService::createStatemachineForEmployee);

        return savedEmployee.map(this::mapEmployeeDto);
    }

    public EmployeeDto mapEmployeeDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .age(employee.getAge())
                .email(employee.getEmail())
                .build();
    }

    public Employee mapEmployee(EmployeeDto employee) {
        return Employee.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .age(employee.getAge())
                .email(employee.getEmail())
                .build();
    }
}