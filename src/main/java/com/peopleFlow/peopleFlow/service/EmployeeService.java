package com.peopleFlow.peopleFlow.service;

import com.peopleFlow.peopleFlow.model.dto.EmployeeDto;

import java.util.Optional;

public interface EmployeeService {
    Optional<EmployeeDto> addEmployee(EmployeeDto employee);
}
