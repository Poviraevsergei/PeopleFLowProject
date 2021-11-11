package com.peopleFlow.peopleFlow.controller;

import com.peopleFlow.peopleFlow.model.dto.ChangeState;
import com.peopleFlow.peopleFlow.service.EmployeeStateService;
import com.peopleFlow.peopleFlow.util.kafka.producer.EmployeeStateProducer;
import com.peopleFlow.peopleFlow.util.statemachine.state.EmployeeState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import com.peopleFlow.peopleFlow.model.dto.EmployeeDto;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import com.peopleFlow.peopleFlow.util.kafka.producer.EmployeeProducer;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeController {

    private final EmployeeProducer kafkaProducer;
    private final EmployeeStateService stateService;
    private final EmployeeStateProducer stateProducer;

    @Operation(summary = "Add new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)})
    @PostMapping("/add")
    public ResponseEntity<EmployeeDto> add(@RequestBody EmployeeDto employee) throws ExecutionException, InterruptedException {
        return kafkaProducer.sendMessage(employee)
                .map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Set new state for employee")
    @PostMapping("{id}")
    public ResponseEntity<String> setState(@RequestBody ChangeState request) throws ExecutionException, InterruptedException {
        return stateProducer.sendEvent(request) ? ResponseEntity.ok(request.getEvent().name()) : ResponseEntity.badRequest().body("Not Accepted");
    }

    @Operation(summary = "Get employee state with useing id")
    @GetMapping("{id}")
    public ResponseEntity<EmployeeState> getState(@PathVariable("id") Long id) {
        return stateService.getStateForEmployee(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}