package br.com.lojacell.api.controller;

import br.com.lojacell.domain.model.Customer;
import br.com.lojacell.domain.repository.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor // Cria o construtor para o Spring injetar o Repository automaticamente
public class CustomerController {

    private final CustomerRepository customerRepository;

    // Listar todos os clientes ou buscar por nome
    @GetMapping
    public List<Customer> list(@RequestParam(required = false) String name) {
        if (name != null) {
            return customerRepository.findByNameContainingIgnoreCase(name);
        }
        return customerRepository.findAll();
    }

    // Buscar um cliente específico pelo ID
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> find(@PathVariable Long customerId) {
        return customerRepository.findById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cadastrar novo cliente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer add(@Valid @RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    // Atualizar cliente existente
    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> update(@PathVariable Long customerId, @Valid @RequestBody Customer customer) {
        if (!customerRepository.existsById(customerId)) {
            return ResponseEntity.notFound().build();
        }
        customer.setId(customerId);
        customer = customerRepository.save(customer);
        return ResponseEntity.ok(customer);
    }
}