package br.com.lojacell.domain.service;

import br.com.lojacell.api.dto.WorkOrderRequestDTO;
import br.com.lojacell.domain.model.*;
import br.com.lojacell.domain.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final CustomerRepository customerRepository;
    private final BrandRepository brandRepository;
    private final DeviceModelRepository deviceModelRepository;
    private final DeviceRepository deviceRepository;

    @Transactional
    public WorkOrder createWorkOrder(@Valid WorkOrderRequestDTO request) {
        // ... (Seu código original de criação continua exatamente igual aqui)
        Customer customer = customerRepository.findByNameContainingIgnoreCase(request.getCustomerName())
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setName(request.getCustomerName());
                    newCustomer.setPhone(request.getCustomerPhone());
                    return customerRepository.save(newCustomer);
                });

        Brand brand = brandRepository.findByName(request.getBrandName())
                .orElseGet(() -> brandRepository.save(new Brand(request.getBrandName())));

        DeviceModel model = deviceModelRepository.findByNameAndBrand(request.getModelName(), brand)
                .orElseGet(() -> deviceModelRepository.save(new DeviceModel(request.getModelName(), brand)));

        Device device = new Device();
        device.setCustomer(customer);
        device.setModel(model);
        device.setImei(request.getSerialNumber());
        device = deviceRepository.save(device);

        WorkOrder wo = new WorkOrder();
        wo.setCustomer(customer);
        wo.setDevice(device);
        wo.setComplaint(request.getComplaint());
        wo.setNotes(request.getNotes());
        wo.setServiceTotal(BigDecimal.ZERO); // Nasce zerada
        wo.setStatus(OrderStatus.ABERTA);

        String numeroOS = "OS-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        wo.setWorkOrderNumber(numeroOS);

        return workOrderRepository.save(wo);
    }

    // NOVO: Método chamado ao editar/encerrar a OS no Modal
    @Transactional
    public WorkOrder updateWorkOrder(Long id, @Valid WorkOrderRequestDTO request) {
        WorkOrder wo = workOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada!"));


        // Verifica se a OS já está fechada no banco
        boolean isAlreadyClosed = (wo.getStatus() == OrderStatus.ENTREGUE);

        // Verifica se o usuário está tentando mudar o status para reabrir
        boolean isReopening = isAlreadyClosed && request.getStatus() != null && request.getStatus() != wo.getStatus();

        // REGRA 1: Se tá fechada e NÃO é uma reabertura, bloqueia tudo!
        if (isAlreadyClosed && !isReopening) {
            throw new IllegalStateException("Uma Ordem de Serviço encerrada não pode ser alterada!");
        }

        // REGRA 2: Se está reabrindo, exige a justificativa
        if (isReopening) {
            if (request.getJustification() == null || request.getJustification().trim().isEmpty()) {
                throw new IllegalArgumentException("É obrigatório informar uma justificativa para reabrir a OS.");
            }
            // Anota a justificativa no histórico da OS
            String historico = wo.getNotes() != null ? wo.getNotes() : "";
            wo.setNotes(historico + "\n\n[REABERTA] Motivo: " + request.getJustification());
            wo.setStatus(request.getStatus());

            // Retorna logo após reabrir, para obrigar o usuário a editar as peças num segundo momento
            return workOrderRepository.save(wo);
        }

        // Atualiza Status e Observações Internas
        if (request.getStatus() != null) wo.setStatus(request.getStatus());
        if (request.getNotes() != null) wo.setNotes(request.getNotes());

        // Limpa as listas atuais para o Hibernate substituir pelas novas que vieram do Front
        wo.getUsedParts().clear();
        wo.getProvidedServices().clear();

        BigDecimal novoTotal = BigDecimal.ZERO;

        // Processa as Peças
        if (request.getParts() != null) {
            for (WorkOrderRequestDTO.PartDTO p : request.getParts()) {
                UsedPart part = new UsedPart();
                part.setDescription(p.getDescription());
                part.setPrice(p.getPrice() != null ? p.getPrice() : BigDecimal.ZERO);
                part.setQuantity(p.getQuantity() != null ? p.getQuantity() : 1);

                wo.addUsedPart(part);

                // Soma no totalizador (Preço * Quantidade)
                BigDecimal itemTotal = part.getPrice().multiply(new BigDecimal(part.getQuantity()));
                novoTotal = novoTotal.add(itemTotal);
            }
        }

        // Processa os Serviços
        if (request.getServices() != null) {
            for (WorkOrderRequestDTO.ServiceDTO s : request.getServices()) {
                ProvidedService service = new ProvidedService();
                service.setDescription(s.getDescription());
                service.setPrice(s.getPrice() != null ? s.getPrice() : BigDecimal.ZERO);

                wo.addProvidedService(service);

                // Soma no totalizador
                novoTotal = novoTotal.add(service.getPrice());
            }
        }

        // Define o valor total recalculado à prova de falhas
        wo.setServiceTotal(novoTotal);

        return workOrderRepository.save(wo);
    }
}