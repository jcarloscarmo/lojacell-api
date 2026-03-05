package br.com.lojacell.domain.repository;

import br.com.lojacell.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Busca por parte do nome (ignore case) para facilitar a busca no balcão
    List<Customer> findByNameContainingIgnoreCase(String name);
}