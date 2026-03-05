package br.com.lojacell.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "payments")
public class Payment {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String notes;

    @Column(name = "paid_at")
    private LocalDateTime paidAt = LocalDateTime.now();
}