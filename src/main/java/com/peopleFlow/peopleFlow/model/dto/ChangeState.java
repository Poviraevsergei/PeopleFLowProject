package com.peopleFlow.peopleFlow.model.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.peopleFlow.peopleFlow.util.statemachine.event.EmployeeEvent;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeState {
    @JsonProperty("id")
    private Long employeeId;
    @JsonProperty("event")
    private EmployeeEvent event;
}