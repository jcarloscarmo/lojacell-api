package br.com.lojacell.api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class NewWorkOrderRequest {
    private String customerName;
    private String customerPhone;
    private String brandName;
    private String modelName;
    private String serialNumber;
    private String complaint;
    private String notes;
    private BigDecimal serviceTotal;
}