package com.peopleFlow.peopleFlow.service;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import com.peopleFlow.peopleFlow.model.dto.EmployeeDto;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeServiceImplTest {
    @Autowired
    private EmployeeService sut;

    private EmployeeDto employeeDto;

    @Before
    public void setUp() {
        this.employeeDto = EmployeeDto.builder()
                .age(28)
                .firstName("Bill")
                .lastName("Gates")
                .email("programm@mail.ru")
                .build();
    }

    @Test
    public void addEmployeeTest() {
        Optional<EmployeeDto> result = sut.addEmployee(employeeDto);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isNotNull();
    }
}