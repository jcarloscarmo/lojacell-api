package br.com.lojacell.api.controller;

import br.com.lojacell.domain.model.Payment;
import br.com.lojacell.domain.model.WorkOrder;
import br.com.lojacell.domain.repository.PaymentRepository;
import br.com.lojacell.domain.repository.WorkOrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final WorkOrderRepository workOrderRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment add(@Valid @RequestBody Payment payment) {
        return paymentRepository.save(payment);
    }

    @GetMapping("/work-order/{workOrderId}")
    public Map<String, Object> getPaymentStatus(@PathVariable Long workOrderId) {
        WorkOrder os = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("OS não encontrada"));

        List<Payment> payments = paymentRepository.findByWorkOrderId(workOrderId);

        // Soma TUDO que foi registrado (Pagamentos + Descontos)
        BigDecimal totalAbatido = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Saldo = Valor total da OS - (Entradas + Descontos)
        BigDecimal saldo = os.getServiceTotal().subtract(totalAbatido);

        return Map.of(
                "valorTotalOS", os.getServiceTotal(),
                "totalAbatido", totalAbatido,
                "saldoRestante", saldo,
                "historico", payments
        );
    }
}