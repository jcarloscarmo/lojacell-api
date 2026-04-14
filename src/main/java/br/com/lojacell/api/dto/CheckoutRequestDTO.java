package br.com.lojacell.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CheckoutRequestDTO {
    private Long workOrderId;
    private String description;
    private BigDecimal discount = BigDecimal.ZERO;
    private List<PaymentItemDTO> payments;
}
