package br.com.lojacell.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brands")
@Data // Isso cria Getters, Setters, Equals e HashCode automaticamente
@NoArgsConstructor // Cria o construtor vazio para o Hibernate
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public Brand(String name) {
        this.name = name;
    }
}