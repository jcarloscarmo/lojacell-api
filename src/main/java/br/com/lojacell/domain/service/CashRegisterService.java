package br.com.lojacell.domain.service;

import br.com.lojacell.api.dto.CashRegisterHistoryDTO;
import br.com.lojacell.api.dto.CloseRegisterDTO;
import br.com.lojacell.api.dto.OpenCashRegisterDTO;
import br.com.lojacell.domain.model.CashRegister;
import br.com.lojacell.domain.model.CashRegisterStatus;
import br.com.lojacell.domain.model.PaymentMethod;
import br.com.lojacell.domain.model.Transaction;
import br.com.lojacell.domain.model.TransactionType;
import br.com.lojacell.domain.model.User;
import br.com.lojacell.domain.repository.CashRegisterRepository;
import br.com.lojacell.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class CashRegisterService {

    @Autowired
    private br.com.lojacell.domain.repository.TransactionRepository transactionRepository;
    private final CashRegisterRepository cashRegisterRepository;
    private final UserRepository userRepository;

    @Transactional
    public CashRegister openCashRegister(OpenCashRegisterDTO dto) {
        if (cashRegisterRepository.findByStatus(CashRegisterStatus.OPEN).isPresent()) {
            throw new IllegalStateException("Já existe um caixa aberto.");
        }

        User operator = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + dto.getUserId()));

        CashRegister cashRegister = new CashRegister();
        cashRegister.setOperator(operator);
        cashRegister.setOpeningBalance(dto.getOpeningBalance() != null ? dto.getOpeningBalance() : BigDecimal.ZERO);
        cashRegister.setOpeningDate(LocalDateTime.now());
        cashRegister.setStatus(CashRegisterStatus.OPEN);

        return cashRegisterRepository.save(cashRegister);
    }

    @Transactional
    public Map<String, Object> closeCashRegister(CloseRegisterDTO dto) {
        CashRegister openRegister = cashRegisterRepository.findByStatus(CashRegisterStatus.OPEN)
                .orElseThrow(() -> new IllegalStateException("Nenhum caixa aberto encontrado para fechar."));

        BigDecimal countedCash = dto.getCountedCash() != null ? dto.getCountedCash() : BigDecimal.ZERO;
        BigDecimal countedPix = dto.getCountedPix() != null ? dto.getCountedPix() : BigDecimal.ZERO;
        BigDecimal countedCredit = dto.getCountedCreditCard() != null ? dto.getCountedCreditCard() : BigDecimal.ZERO;
        BigDecimal countedDebit = dto.getCountedDebitCard() != null ? dto.getCountedDebitCard() : BigDecimal.ZERO;

        BigDecimal expCash = openRegister.getOpeningBalance() != null ? openRegister.getOpeningBalance() : BigDecimal.ZERO;
        BigDecimal expPix = BigDecimal.ZERO;
        BigDecimal expCredit = BigDecimal.ZERO;
        BigDecimal expDebit = BigDecimal.ZERO;

        List<Transaction> transactions = openRegister.getTransactions();
        if (transactions != null) {
            for (Transaction t : transactions) {
                BigDecimal amount = t.getAmount() != null ? t.getAmount() : BigDecimal.ZERO;
                if (t.getPaymentMethod() == PaymentMethod.CASH) {
                    if (t.getType() == TransactionType.INCOME) {
                        expCash = expCash.add(amount);
                    } else if (t.getType() == TransactionType.EXPENSE || t.getType() == TransactionType.REFUND) {
                        expCash = expCash.subtract(amount);
                    }
                } else if (t.getPaymentMethod() == PaymentMethod.PIX) {
                     if (t.getType() == TransactionType.INCOME) {
                         expPix = expPix.add(amount);
                     }
                } else if (t.getPaymentMethod() == PaymentMethod.CREDIT_CARD) {
                    if (t.getType() == TransactionType.INCOME) {
                         expCredit = expCredit.add(amount);
                    }
                } else if (t.getPaymentMethod() == PaymentMethod.DEBIT_CARD) {
                     if (t.getType() == TransactionType.INCOME) {
                         expDebit = expDebit.add(amount);
                     }
                }
            }
        }

        BigDecimal diffCash = countedCash.subtract(expCash);
        BigDecimal diffPix = countedPix.subtract(expPix);
        BigDecimal diffCredit = countedCredit.subtract(expCredit);
        BigDecimal diffDebit = countedDebit.subtract(expDebit);

        BigDecimal totalDifference = diffCash.add(diffPix).add(diffCredit).add(diffDebit);

        openRegister.setExpectedCash(expCash);
        openRegister.setExpectedPix(expPix);
        openRegister.setExpectedCredit(expCredit);
        openRegister.setExpectedDebit(expDebit);
        
        openRegister.setCountedCash(countedCash);
        openRegister.setCountedPix(countedPix);
        openRegister.setCountedCredit(countedCredit);
        openRegister.setCountedDebit(countedDebit);

        openRegister.setTotalDifference(totalDifference);

        openRegister.setClosingBalance(countedCash);
        openRegister.setClosingDate(LocalDateTime.now());
        openRegister.setStatus(CashRegisterStatus.CLOSED);

        cashRegisterRepository.save(openRegister);

        Map<String, Object> result = new HashMap<>();
        
        Map<String, BigDecimal> cashDetails = new HashMap<>();
        cashDetails.put("expected", expCash);
        cashDetails.put("counted", countedCash);
        cashDetails.put("difference", diffCash);
        result.put("cash", cashDetails);

        Map<String, BigDecimal> pixDetails = new HashMap<>();
        pixDetails.put("expected", expPix);
        pixDetails.put("counted", countedPix);
        pixDetails.put("difference", diffPix);
        result.put("pix", pixDetails);

        Map<String, BigDecimal> creditDetails = new HashMap<>();
        creditDetails.put("expected", expCredit);
        creditDetails.put("counted", countedCredit);
        creditDetails.put("difference", diffCredit);
        result.put("credit", creditDetails);

        Map<String, BigDecimal> debitDetails = new HashMap<>();
        debitDetails.put("expected", expDebit);
        debitDetails.put("counted", countedDebit);
        debitDetails.put("difference", diffDebit);
        result.put("debit", debitDetails);

        result.put("totalDifference", totalDifference);
        result.put("message", "Caixa fechado com sucesso.");

        return result;
    }

    @Transactional(readOnly = true)
    public List<CashRegisterHistoryDTO> getHistory() {
        List<CashRegister> closedRegisters = cashRegisterRepository.findByStatusOrderByClosingDateDesc(CashRegisterStatus.CLOSED);
        return closedRegisters.stream().map(cr -> new CashRegisterHistoryDTO(
                cr.getId(),
                cr.getOpeningDate(),
                cr.getClosingDate(),
                cr.getOpeningBalance(),
                cr.getClosingBalance(),
                cr.getTotalDifference()
        )).collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional
    public void addManualTransaction(br.com.lojacell.api.dto.ManualTransactionDTO request) {
        CashRegister openRegister = cashRegisterRepository.findByStatus(CashRegisterStatus.OPEN)
                .orElseThrow(() -> new IllegalStateException("Nenhum caixa aberto encontrado."));

        Transaction transaction = new Transaction();
        transaction.setCashRegister(openRegister);
        transaction.setType(TransactionType.valueOf(request.getType()));
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        transaction.setTransactionDate(java.time.LocalDateTime.now());

        // Note que o work_order_id ficará nulo, pois é uma transação avulsa. Isso é o correto.
        transactionRepository.save(transaction);
    }
}
