package br.com.lojacell.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "work_order_signatures")
public class WorkOrderSignature {

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
    private SignatureType type;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}