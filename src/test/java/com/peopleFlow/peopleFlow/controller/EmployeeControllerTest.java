package com.peopleFlow.peopleFlow.controller;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.MockitoJUnitRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.peopleFlow.peopleFlow.model.dto.ChangeState;
import com.peopleFlow.peopleFlow.model.dto.EmployeeDto;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.peopleFlow.peopleFlow.service.EmployeeStateService;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.peopleFlow.peopleFlow.util.kafka.producer.EmployeeProducer;
import com.peopleFlow.peopleFlow.util.statemachine.state.EmployeeState;
import com.peopleFlow.peopleFlow.util.kafka.producer.EmployeeStateProducer;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class EmployeeControllerTest {

    MockMvc mvc;

    @Mock
    EmployeeProducer employeeProducer;

    @Mock
    EmployeeStateProducer employeeStateProducer;

    @Mock
    EmployeeStateService stateService;

    @InjectMocks
    EmployeeController employeeController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void createEmployee() throws Exception {
        when(employeeProducer.sendMessage(any(EmployeeDto.class))).thenReturn(Optional.of(new EmployeeDto()));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(new EmployeeDto());

        MvcResult result = mvc.perform(post("/employee/add").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), allOf(notNullValue()));
        verify(employeeProducer, times(1)).sendMessage(any());
    }

    @Test
    void changeTheStateOfEmployee() throws Exception {
        when(employeeStateProducer.sendEvent(any(ChangeState.class))).thenReturn(anyBoolean());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(new ChangeState());

        MvcResult result = mvc.perform(post("/employee/1").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), allOf(notNullValue()));
        verify(employeeStateProducer, times(1)).sendEvent(new ChangeState());
    }

    @Test
    void getTheStateOfEmployee() throws Exception {
        when(stateService.getStateForEmployee(anyLong())).thenReturn(Optional.of(EmployeeState.ADDED));
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(anyLong());

        MvcResult result = mvc.perform(get("/employee/1").contentType(APPLICATION_JSON)
                .content(requestJson))
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), allOf(notNullValue()));
        verify(stateService, times(1)).getStateForEmployee(anyLong());
    }
}