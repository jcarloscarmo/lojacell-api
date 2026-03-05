package br.com.lojacell.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "device_models")
@Data
@NoArgsConstructor
public class DeviceModel {

    @Id // O Hibernate precisa saber que este é o identificador
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    public DeviceModel(String name, Brand brand) {
        this.name = name;
        this.brand = brand;
    }
}