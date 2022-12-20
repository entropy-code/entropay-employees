package com.entropyteam.entropay.employees.services;

import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.employees.dtos.PaymentInformationDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.PaymentInformation;
import com.entropyteam.entropay.employees.repositories.PaymentInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class PaymentInformationService extends BaseService<PaymentInformation, PaymentInformationDto, UUID> {

    private final PaymentInformationRepository paymentInformationRepository;

    @Autowired
    public PaymentInformationService(PaymentInformationRepository paymentInformationRepository) {
        this.paymentInformationRepository = paymentInformationRepository;
    }

    @Override
    protected BaseRepository<PaymentInformation, UUID> getRepository() {
        return paymentInformationRepository;
    }

    @Transactional
    @Override
    protected PaymentInformationDto toDTO(PaymentInformation entity) {
        return new PaymentInformationDto(entity);
    }

    @Transactional
    @Override
    protected PaymentInformation toEntity(PaymentInformationDto entity) {
        return new PaymentInformation(entity);
    }

    @Transactional
    protected Set<PaymentInformation> create(List<PaymentInformationDto> paymentsInformation, Employee savedEntity) {
        Set<PaymentInformation> paymentInformation = paymentsInformation.stream().map(PaymentInformation::new).collect(Collectors.toSet());
        paymentInformation = paymentInformation.stream().peek( p -> p.setEmployee(savedEntity)).collect(Collectors.toSet());
        return new HashSet<>(paymentInformationRepository.saveAll(paymentInformation));
    }

    @Transactional
    protected Set<PaymentInformation> update(List<PaymentInformationDto> paymentInformationDtos, UUID id){
        List<PaymentInformation> paymentsInformationList = paymentInformationRepository.findAllByEmployeeIdAndDeletedIsFalse(id);
        List<PaymentInformation> paymentInformationRequest = new ArrayList<>(paymentInformationDtos.stream().map(this::toEntity).toList());

        for(PaymentInformation paymentInformation: paymentsInformationList){
            if(!paymentInformationRequest.contains(paymentInformation)){
                paymentInformation.setDeleted(true);
                paymentInformationRequest.add(paymentInformation);
            }
        }
        return new HashSet<>(paymentInformationRequest);
    }


}
