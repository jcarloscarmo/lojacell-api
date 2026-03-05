package br.com.lojacell.api.controller;

import br.com.lojacell.domain.model.Brand;
import br.com.lojacell.domain.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@CrossOrigin("*") // Libera para o seu Front-end acessar sem erro de CORS
public class BrandController {

    private final BrandRepository brandRepository;

    @GetMapping
    public List<Brand> listAll() {
        return brandRepository.findAll();
    }
}