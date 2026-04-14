package br.com.lojacell.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentItemDTO {
    private BigDecimal amount;

    @JsonAlias({"method", "paymentMethod", "payment_method", "type", "tipo"})
    private String paymentMethod;
}
