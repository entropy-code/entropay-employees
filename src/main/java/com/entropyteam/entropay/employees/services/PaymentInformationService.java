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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentInformationService extends BaseService<PaymentInformation, PaymentInformationDto, UUID> {

    private final PaymentInformationRepository paymentInformationRepository;

    @Autowired
    public PaymentInformationService(PaymentInformationRepository PaymentInformationRepository) {
        this.paymentInformationRepository = Objects.requireNonNull(PaymentInformationRepository);
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
        List<PaymentInformation> paymentsInformationListRepository = paymentInformationRepository.findAllByEmployeeIdAndDeletedIsFalse(id);
        List<PaymentInformation> paymentInformationRequest = paymentInformationDtos.stream().map(this::toEntity).toList();
        List<PaymentInformation> paymentInformationToDelete = new ArrayList<>();

        for(PaymentInformation paymentInformation: paymentsInformationListRepository){
            if(!paymentInformationRequest.contains(paymentInformation) && paymentInformationRepository.existsById(paymentInformation.getId())){
                paymentInformation.setDeleted(true);
                paymentInformationToDelete.add(paymentInformation);
            }
        }
        paymentInformationRepository.saveAll(paymentInformationToDelete);
        return new HashSet<>(paymentInformationRequest);
    }


}
