package br.com.lojacell.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "work_orders")
public class WorkOrder {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_order_number")
    private String workOrderNumber;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.ABERTA;

    @NotBlank
    private String complaint;

    private String notes;

    @Column(name = "service_total")
    private BigDecimal serviceTotal = BigDecimal.ZERO;

    // NOVO: Lista de Peças vinculadas a esta OS
    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsedPart> usedParts = new ArrayList<>();

    // NOVO: Lista de Serviços vinculados a esta OS
    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProvidedService> providedServices = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "device_photo", columnDefinition = "LONGTEXT")
    private String devicePhoto;

    @Column(name = "customer_signature", columnDefinition = "LONGTEXT")
    private String customerSignature;

    // Métodos auxiliares para manter as tabelas sincronizadas
    public void addUsedPart(UsedPart part) {
        usedParts.add(part);
        part.setWorkOrder(this);
    }

    public void addProvidedService(ProvidedService service) {
        providedServices.add(service);
        service.setWorkOrder(this);
    }
}