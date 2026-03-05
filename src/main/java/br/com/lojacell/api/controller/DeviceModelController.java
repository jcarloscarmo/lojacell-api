package br.com.lojacell.api.controller;

import br.com.lojacell.domain.model.DeviceModel;
import br.com.lojacell.domain.repository.DeviceModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/models")
@RequiredArgsConstructor
@CrossOrigin("*") // Essencial para o React conseguir ler os dados sem ser bloqueado
public class DeviceModelController {

    private final DeviceModelRepository deviceModelRepository;

    // A rota exata que o Front-end está procurando!
    @GetMapping("/by-brand/{brandId}")
    public List<DeviceModel> listByBrand(@PathVariable Long brandId) {
        return deviceModelRepository.findByBrandId(brandId);
    }
}