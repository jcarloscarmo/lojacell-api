package br.com.lojacell.api.dto;

import br.com.lojacell.domain.model.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WorkOrderRequestDTO {
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String serialNumber;
    //private BigDecimal serviceTotal;
    private String brandName;
    private String modelName;
    private String complaint;
    private OrderStatus status;
    private String notes;
    private String justification;

    private String devicePhoto;
    private String customerSignature;

    // NOVO: Recebendo as listas do Front-end
    private List<PartDTO> parts;
    private List<ServiceDTO> services;

    // Classes internas para mapear os dados exatos que vêm do React
    @Data
    public static class PartDTO {
        private String description;
        private BigDecimal price;
        private Integer quantity;
    }

    @Data
    public static class ServiceDTO {
        private String description;
        private BigDecimal price;
    }
}