package br.com.lojacell.api.controller;

import br.com.lojacell.api.dto.DashboardSummaryDTO;
import br.com.lojacell.domain.model.OrderStatus;
import br.com.lojacell.domain.model.Payment;
import br.com.lojacell.domain.model.WorkOrder;
import br.com.lojacell.domain.repository.PaymentRepository;
import br.com.lojacell.domain.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final WorkOrderRepository workOrderRepository;
    private final PaymentRepository paymentRepository;

    @GetMapping("/summary")
    public DashboardSummaryDTO getSummary() {
        List<WorkOrder> allOrders = workOrderRepository.findAll();
        List<Payment> allPayments = paymentRepository.findAll();

        // 1. Contagem por Status
        Map<String, Long> statusCount = allOrders.stream()
                .collect(Collectors.groupingBy(os -> os.getStatus().name(), Collectors.counting()));

        // 2. Total Recebido Hoje (Filtrando pagamentos de hoje 00:00 até 23:59)
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        BigDecimal totalToday = allPayments.stream()
                .filter(p -> p.getPaidAt().isAfter(startOfDay) && p.getPaidAt().isBefore(endOfDay))
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Total de OS "Ativas" (Tudo menos Entregue/Cancelada)
        long openOrders = allOrders.stream()
                .filter(os -> os.getStatus() != OrderStatus.ENTREGUE && os.getStatus() != OrderStatus.CANCELADA)
                .count();

        return DashboardSummaryDTO.builder()
                .statusCount(statusCount)
                .totalReceivedToday(totalToday)
                .openOrdersCount(openOrders)
                .build();
    }
}