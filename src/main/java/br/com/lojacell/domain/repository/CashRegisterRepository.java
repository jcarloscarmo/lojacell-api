package br.com.lojacell.domain.repository;

import br.com.lojacell.domain.model.CashRegister;
import br.com.lojacell.domain.model.CashRegisterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {
    Optional<CashRegister> findByStatus(CashRegisterStatus status);
    List<CashRegister> findByStatusOrderByClosingDateDesc(CashRegisterStatus status);
}
