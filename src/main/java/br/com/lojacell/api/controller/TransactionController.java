package br.com.lojacell.api.controller;

import br.com.lojacell.api.dto.CheckoutRequestDTO;
import br.com.lojacell.api.dto.TransactionResponseDTO;
import br.com.lojacell.domain.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/checkout")
    public ResponseEntity<Void> processCheckout(@RequestBody CheckoutRequestDTO request) {
        transactionService.processCheckout(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/work-order/{workOrderId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByWorkOrder(@PathVariable Long workOrderId) {
        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByWorkOrder(workOrderId);
        return ResponseEntity.ok(transactions);
    }
}
