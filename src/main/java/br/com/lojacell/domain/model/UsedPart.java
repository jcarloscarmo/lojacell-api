package br.com.lojacell.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "used_parts")
public class UsedPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal price;
    private Integer quantity = 1;

    @ManyToOne
    @JoinColumn(name = "work_order_id")
    @JsonIgnore
    private WorkOrder workOrder;
}