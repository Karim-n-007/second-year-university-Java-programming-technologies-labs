package ru.nursafin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nursafin.dto.OperationView;
import ru.nursafin.model.OperationType;
import ru.nursafin.service.OperationService;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
public class OperationController {

    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping
     public List<OperationView> getOperations(
             @RequestParam(required = false) OperationType operationType,
             @RequestParam(required = false) Long accountId
    ) {
         return operationService.getOperationsByFilter(operationType, accountId);
    }
}
