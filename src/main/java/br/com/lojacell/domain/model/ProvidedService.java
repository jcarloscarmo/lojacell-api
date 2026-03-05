package br.com.lojacell.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "provided_services")
public class ProvidedService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "work_order_id")
    @JsonIgnore // Evita loop infinito na hora de mandar o JSON pro Front
    private WorkOrder workOrder;
}