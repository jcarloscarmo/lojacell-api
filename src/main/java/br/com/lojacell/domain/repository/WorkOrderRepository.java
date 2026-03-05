package br.com.lojacell.domain.repository;

import br.com.lojacell.domain.model.OrderStatus;
import br.com.lojacell.domain.model.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Optional<WorkOrder> findByWorkOrderNumber(String workOrderNumber);
    List<WorkOrder> findByStatus(OrderStatus status);
}