package br.com.lojacell.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OpenCashRegisterDTO {
    private Long userId;
    private BigDecimal openingBalance;
}
