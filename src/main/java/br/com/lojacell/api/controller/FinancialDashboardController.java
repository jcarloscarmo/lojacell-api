package br.com.lojacell.api.controller;

import br.com.lojacell.api.dto.DailyFinancialSummaryDTO;
import br.com.lojacell.domain.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class FinancialDashboardController {

    private final TransactionService transactionService;

    @GetMapping("/monthly")
    public ResponseEntity<List<DailyFinancialSummaryDTO>> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(transactionService.getMonthlySummary(year, month));
    }
}
