package br.com.lojacell.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ManualTransactionDTO {
    private String type; // INCOME ou EXPENSE
    private BigDecimal amount;
    private String description;
    private String paymentMethod;
}