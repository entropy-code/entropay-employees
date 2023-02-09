package com.entropyteam.entropay.employees.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.PaymentSettlementDto;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.PaymentSettlement;
import com.entropyteam.entropay.employees.repositories.PaymentSettlementRepository;

@Service
public class PaymentSettlementService  extends BaseService<PaymentSettlement, PaymentSettlementDto, UUID> {

    private final PaymentSettlementRepository paymentSettlementRepository;

    @Autowired
    public PaymentSettlementService(PaymentSettlementRepository paymentSettlementRepository,
            ReactAdminMapper reactAdminMapper) {
        super(PaymentSettlement.class, reactAdminMapper);
        this.paymentSettlementRepository = paymentSettlementRepository;
    }

    @Override
    protected BaseRepository<PaymentSettlement, UUID> getRepository() {
        return paymentSettlementRepository;
    }

    @Transactional
    @Override
    protected PaymentSettlementDto toDTO(PaymentSettlement entity) {
        return new PaymentSettlementDto(entity);
    }

    @Transactional
    @Override
    protected PaymentSettlement toEntity(PaymentSettlementDto entity) {
        return new PaymentSettlement(entity);
    }

    @Transactional
    protected Set<PaymentSettlement> create(List<PaymentSettlementDto> paymentSettlementDtos, Contract savedEntity) {
        Set<PaymentSettlement> paymentSettlement = paymentSettlementDtos.stream().map(PaymentSettlement::new).collect(Collectors.toSet());
        paymentSettlement = paymentSettlement.stream().peek( p -> p.setContract(savedEntity)).collect(Collectors.toSet());
        return new HashSet<>(paymentSettlementRepository.saveAll(paymentSettlement));
    }

    @Transactional
    protected void update(List<PaymentSettlementDto> paymentSettlementDtos, Contract contract){
        List<PaymentSettlement> paymentsSettlementList = paymentSettlementRepository.findAllByContractIdAndDeletedIsFalse(contract.getId());
        List<PaymentSettlement> paymentSettlementRequest = paymentSettlementDtos.stream().map(this::toEntity).toList();
        List<PaymentSettlement> paymentSettlementToDelete = new ArrayList<>();

        for(PaymentSettlement paymentSettlement: paymentsSettlementList){
            if(!paymentSettlementRequest.contains(paymentSettlement)){
                paymentSettlement.setDeleted(true);
                paymentSettlementToDelete.add(paymentSettlement);
            }
        }
        paymentSettlementRequest = paymentSettlementRequest.stream().peek(p -> p.setContract(contract)).collect(Collectors.toList());
        paymentSettlementRepository.saveAll(paymentSettlementRequest);
        paymentSettlementRepository.saveAll(paymentSettlementToDelete);
    }

}
