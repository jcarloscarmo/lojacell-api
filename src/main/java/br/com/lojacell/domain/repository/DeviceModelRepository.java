package br.com.lojacell.domain.repository;

import br.com.lojacell.domain.model.Brand;
import br.com.lojacell.domain.model.DeviceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Import necessário para o retorno múltiplo
import java.util.Optional;

@Repository
public interface DeviceModelRepository extends JpaRepository<DeviceModel, Long> {

    // NOVO: Essencial para o Front-end filtrar modelos por marca
    List<DeviceModel> findByBrandId(Long brandId);

    Optional<DeviceModel> findByNameAndBrand(String name, Brand brand);

    boolean existsByNameAndBrand(String name, Brand brand);
}