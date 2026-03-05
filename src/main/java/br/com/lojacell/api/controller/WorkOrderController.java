package br.com.lojacell.api.controller;

import br.com.lojacell.api.dto.WorkOrderRequestDTO;
import br.com.lojacell.domain.model.WorkOrder;
import br.com.lojacell.domain.repository.WorkOrderRepository;
import br.com.lojacell.domain.service.WorkOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work-orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class WorkOrderController {

    private final WorkOrderService workOrderService;
    private final WorkOrderRepository workOrderRepository;

    // Rota para listar todas as OS na tela principal
    @GetMapping
    public List<WorkOrder> listAll() {
        return workOrderRepository.findAll();
    }

    // Rota para CRIAR a OS
    @PostMapping
    public ResponseEntity<WorkOrder> create(@RequestBody @Valid WorkOrderRequestDTO request) {
        WorkOrder wo = workOrderService.createWorkOrder(request);
        return ResponseEntity.ok(wo);
    }

    // A PORTA QUE ESTAVA FECHADA: Rota para ATUALIZAR a OS (adicionar peças, pagar)
    @PutMapping("/{id}")
    public ResponseEntity<WorkOrder> updateWorkOrder(
            @PathVariable Long id,
            @RequestBody @Valid WorkOrderRequestDTO request) {
        WorkOrder updatedWorkOrder = workOrderService.updateWorkOrder(id, request);
        return ResponseEntity.ok(updatedWorkOrder);
    }
}