package br.com.lojacell.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CloseRegisterDTO {
    private BigDecimal countedCash;
    private BigDecimal countedPix;
    private BigDecimal countedCreditCard;
    private BigDecimal countedDebitCard;
}
