package br.com.lojacell.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "cash_registers")
public class CashRegister {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime openingDate;

    private LocalDateTime closingDate;

    private BigDecimal openingBalance;

    private BigDecimal closingBalance;

    @Enumerated(EnumType.STRING)
    private CashRegisterStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User operator;

    @OneToMany(mappedBy = "cashRegister", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    @Column(name = "expected_cash")
    private BigDecimal expectedCash;

    @Column(name = "expected_pix")
    private BigDecimal expectedPix;

    @Column(name = "expected_credit")
    private BigDecimal expectedCredit;

    @Column(name = "expected_debit")
    private BigDecimal expectedDebit;

    @Column(name = "counted_cash")
    private BigDecimal countedCash;

    @Column(name = "counted_pix")
    private BigDecimal countedPix;

    @Column(name = "counted_credit")
    private BigDecimal countedCredit;

    @Column(name = "counted_debit")
    private BigDecimal countedDebit;

    @Column(name = "total_difference")
    private BigDecimal totalDifference;
}
