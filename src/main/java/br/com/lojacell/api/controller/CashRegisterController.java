package br.com.lojacell.api.controller;

import br.com.lojacell.api.dto.CashRegisterHistoryDTO;
import br.com.lojacell.api.dto.CloseRegisterDTO;
import br.com.lojacell.api.dto.OpenCashRegisterDTO;
import br.com.lojacell.domain.model.CashRegister;
import br.com.lojacell.domain.model.CashRegisterStatus;
import br.com.lojacell.domain.repository.CashRegisterRepository;
import br.com.lojacell.domain.service.CashRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cash-registers")
@RequiredArgsConstructor
public class CashRegisterController {

    private final CashRegisterRepository cashRegisterRepository;
    private final CashRegisterService cashRegisterService;

    @PostMapping("/open")
    public ResponseEntity<CashRegister> openCashRegister(@RequestBody OpenCashRegisterDTO dto) {
        CashRegister openedRegister = cashRegisterService.openCashRegister(dto);
        return new ResponseEntity<>(openedRegister, HttpStatus.CREATED);
    }

    @GetMapping("/current")
    public ResponseEntity<CashRegister> getCurrentCashRegister() {
        Optional<CashRegister> openRegister = cashRegisterRepository.findByStatus(CashRegisterStatus.OPEN);

        if (openRegister.isPresent()) {
            return ResponseEntity.ok(openRegister.get());
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/close")
    public ResponseEntity<Map<String, Object>> closeCurrentRegister(@RequestBody CloseRegisterDTO request) {
        Map<String, Object> result = cashRegisterService.closeCashRegister(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<CashRegisterHistoryDTO>> getHistory() {
        List<CashRegisterHistoryDTO> history = cashRegisterService.getHistory();
        return ResponseEntity.ok(history);
    }

    @PostMapping("/current/transactions")
    public ResponseEntity<?> addManualTransaction(@RequestBody br.com.lojacell.api.dto.ManualTransactionDTO request) {
        cashRegisterService.addManualTransaction(request);
        return ResponseEntity.ok().build();
    }
}
