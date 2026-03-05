package br.com.lojacell.api.controller;

import br.com.lojacell.domain.model.Device;
import br.com.lojacell.domain.repository.DeviceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceRepository deviceRepository;

    // Listar aparelhos de um cliente específico
    @GetMapping("/customer/{customerId}")
    public List<Device> listByCustomer(@PathVariable Long customerId) {
        return deviceRepository.findByCustomerId(customerId);
    }

    // Buscar por IMEI (muito útil na recepção da loja)
    @GetMapping("/imei/{imei}")
    public List<Device> findByImei(@PathVariable String imei) {
        return deviceRepository.findByImei(imei);
    }

    // Cadastrar novo aparelho
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Device add(@Valid @RequestBody Device device) {
        return deviceRepository.save(device);
    }

    // Buscar aparelho por ID
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> find(@PathVariable Long deviceId) {
        return deviceRepository.findById(deviceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}