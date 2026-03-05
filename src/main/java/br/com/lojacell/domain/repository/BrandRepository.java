package br.com.lojacell.domain.repository;

import br.com.lojacell.domain.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Não esqueça desse import!
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    // NOVO: Essencial para o Autocomplete do Front-end
    List<Brand> findByNameContainingIgnoreCase(String name);

    Optional<Brand> findByNameIgnoreCase(String name);

    Optional<Brand> findByName(String name);
}