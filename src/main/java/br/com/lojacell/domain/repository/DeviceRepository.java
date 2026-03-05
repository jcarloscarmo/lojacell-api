package br.com.lojacell.domain.repository;

import br.com.lojacell.domain.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByCustomerId(Long customerId);
    List<Device> findByImei(String imei);
}