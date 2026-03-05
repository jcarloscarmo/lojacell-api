package br.com.lojacell.api.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class DashboardSummaryDTO {
    private Map<String, Long> statusCount; // Ex: { "ABERTA": 5, "EM_DIAGNOSTICO": 2 }
    private BigDecimal totalReceivedToday; // Soma dos pagamentos do dia
    private Long openOrdersCount;          // Total de OS que não estão ENTREGUES ou CANCELADAS
}