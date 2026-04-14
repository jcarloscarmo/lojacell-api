package br.com.lojacell.api.controller;

import br.com.lojacell.domain.model.ItemType;
import br.com.lojacell.domain.model.Product;
import br.com.lojacell.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        normalizeServiceCost(product);
        return productRepository.save(product);
    }

    @GetMapping
    public List<Product> list() {
        return productRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    product.setId(id);
                    product.setCreatedAt(existingProduct.getCreatedAt());
                    normalizeServiceCost(product);
                    return ResponseEntity.ok(productRepository.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void normalizeServiceCost(Product product) {
        if (product.getItemType() == ItemType.SERVICE && product.getCostPrice() == null) {
            product.setCostPrice(BigDecimal.ZERO);
        }
    }
}
