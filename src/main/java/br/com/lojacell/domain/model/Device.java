package br.com.lojacell.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "devices")
@Data
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // ANTES: private String model;
    // AGORA: Relacionamento com a nova entidade
    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private DeviceModel model;

    private String imei; // Opcional (IMEI/SN)
}