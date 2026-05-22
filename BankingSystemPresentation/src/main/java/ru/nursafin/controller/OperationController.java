package ru.nursafin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nursafin.dto.response.OperationResponse;
import ru.nursafin.mapper.ApiMapper;
import ru.nursafin.model.OperationType;
import ru.nursafin.service.OperationService;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
@AllArgsConstructor
@Tag(name = "Operations")
@PreAuthorize("hasRole('ADMIN')")
public class OperationController {
    private final OperationService operationService;
    private final ApiMapper apiMapper;

    @Operation(summary = "Get operations with optional type or accountId filters")
    @ApiResponse(responseCode = "200", description = "Operations returned")
    @ApiResponse(responseCode = "400", description = "invalid request")
    @ApiResponse(responseCode = "401", description = "invalid credentials")
    @GetMapping
    public List<OperationResponse> getOperations(
             @RequestParam(required = false) OperationType operationType,
             @RequestParam(required = false) Long accountId
    ) {
         return operationService.getOperationsByFilter(operationType, accountId)
                 .stream()
                 .map(apiMapper::toResponse)
                 .toList();
    }
}
