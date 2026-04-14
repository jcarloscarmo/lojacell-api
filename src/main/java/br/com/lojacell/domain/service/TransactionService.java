package br.com.lojacell.domain.service;

import br.com.lojacell.api.dto.CheckoutRequestDTO;
import br.com.lojacell.api.dto.DailyFinancialSummaryDTO;
import br.com.lojacell.api.dto.PaymentItemDTO;
import br.com.lojacell.api.dto.TransactionResponseDTO;
import br.com.lojacell.domain.model.CashRegister;
import br.com.lojacell.domain.model.CashRegisterStatus;
import br.com.lojacell.domain.model.PaymentMethod;
import br.com.lojacell.domain.model.Transaction;
import br.com.lojacell.domain.model.TransactionType;
import br.com.lojacell.domain.model.WorkOrder;
import br.com.lojacell.domain.repository.CashRegisterRepository;
import br.com.lojacell.domain.repository.TransactionRepository;
import br.com.lojacell.domain.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final CashRegisterRepository cashRegisterRepository;
    private final TransactionRepository transactionRepository;
    private final WorkOrderRepository workOrderRepository;

    @Transactional
    public void processCheckout(CheckoutRequestDTO request) {
        CashRegister openCashRegister = cashRegisterRepository.findByStatus(CashRegisterStatus.OPEN)
                .orElseThrow(() -> new IllegalStateException("Nenhum caixa aberto encontrado."));

        WorkOrder workOrder = null;
        if (request.getWorkOrderId() != null) {
            workOrder = workOrderRepository.findById(request.getWorkOrderId())
                    .orElse(null);
        }

        List<Transaction> transactions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        if (request.getPayments() != null) {
            for (PaymentItemDTO paymentItem : request.getPayments()) {
                Transaction transaction = new Transaction();
                transaction.setCashRegister(openCashRegister);
                transaction.setWorkOrder(workOrder);
                transaction.setType(TransactionType.INCOME);
                transaction.setDescription(request.getDescription());
                transaction.setAmount(paymentItem.getAmount());
                transaction.setPaymentMethod(PaymentMethod.valueOf(paymentItem.getPaymentMethod()));
                transaction.setTransactionDate(now);
                
                transactions.add(transaction);
            }
        }

        transactionRepository.saveAll(transactions);
    }
    
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionsByWorkOrder(Long workOrderId) {
        List<Transaction> transactions = transactionRepository.findByWorkOrderId(workOrderId);

        return transactions.stream().map(t -> new TransactionResponseDTO(
                t.getId(),
                t.getAmount(),
                t.getPaymentMethod().name(),
                t.getTransactionDate()
        )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DailyFinancialSummaryDTO> getMonthlySummary(int year, int month) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDateTime start = firstDay.atStartOfDay();
        LocalDateTime end = firstDay.plusMonths(1).atStartOfDay().minusNanos(1);

        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(start, end);
        Map<LocalDate, BigDecimal[]> totalsByDate = new LinkedHashMap<>();

        for (Transaction transaction : transactions.stream()
                .filter(t -> t.getTransactionDate() != null)
                .sorted(Comparator.comparing(Transaction::getTransactionDate))
                .toList()) {
            LocalDate date = transaction.getTransactionDate().toLocalDate();
            BigDecimal amount = transaction.getAmount() != null ? transaction.getAmount() : BigDecimal.ZERO;
            BigDecimal[] totals = totalsByDate.computeIfAbsent(date, key -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});

            if (transaction.getType() == TransactionType.INCOME) {
                totals[0] = totals[0].add(amount);
            } else if (transaction.getType() == TransactionType.EXPENSE) {
                totals[1] = totals[1].add(amount);
            }
        }

        return totalsByDate.entrySet().stream()
                .map(entry -> new DailyFinancialSummaryDTO(
                        entry.getKey(),
                        entry.getValue()[0],
                        entry.getValue()[1],
                        entry.getValue()[0].subtract(entry.getValue()[1])
                ))
                .collect(Collectors.toList());
    }
}
