package br.com.lojacell.domain.repository;

import br.com.lojacell.domain.model.WorkOrderSignature;
import br.com.lojacell.domain.model.SignatureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WorkOrderSignatureRepository extends JpaRepository<WorkOrderSignature, Long> {
    // Busca a assinatura de entrada ou entrega de uma OS específica
    Optional<WorkOrderSignature> findByWorkOrderIdAndType(Long workOrderId, SignatureType type);
}