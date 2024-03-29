package com.entropyteam.entropay.employees.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.ReactAdminMapper;
import com.entropyteam.entropay.employees.dtos.PaymentInformationDto;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.PaymentInformation;
import com.entropyteam.entropay.employees.repositories.PaymentInformationRepository;

@Service
public class PaymentInformationService extends BaseService<PaymentInformation, PaymentInformationDto, UUID> {

    private final PaymentInformationRepository paymentInformationRepository;

    @Autowired
    public PaymentInformationService(PaymentInformationRepository paymentInformationRepository,
            ReactAdminMapper reactAdminMapper) {
        super(PaymentInformation.class, reactAdminMapper);
        this.paymentInformationRepository = paymentInformationRepository;
    }

    @Override
    protected BaseRepository<PaymentInformation, UUID> getRepository() {
        return paymentInformationRepository;
    }

    @Override
    protected PaymentInformationDto toDTO(PaymentInformation entity) {
        return new PaymentInformationDto(entity);
    }

    @Override
    protected PaymentInformation toEntity(PaymentInformationDto entity) {
        return new PaymentInformation(entity);
    }

    public Set<PaymentInformation> createPaymentsInformation(Set<PaymentInformation> paymentInformation, Employee savedEntity) {
        paymentInformation = paymentInformation.stream().peek( p -> p.setEmployee(savedEntity)).collect(Collectors.toSet());
        return new HashSet<>(paymentInformationRepository.saveAll(paymentInformation));
    }

    public void updatePaymentsInformation(List<PaymentInformationDto> paymentInformationDtos, Employee employee){
        List<PaymentInformation> paymentsInformationList = paymentInformationRepository.findAllByEmployeeIdAndDeletedIsFalse(employee.getId());
        List<PaymentInformation> paymentInformationRequest = paymentInformationDtos.stream().map(this::toEntity).toList();
        List<PaymentInformation> paymentInformationToDelete = new ArrayList<>();

        for(PaymentInformation paymentInformation: paymentsInformationList){
            if(!paymentInformationRequest.contains(paymentInformation)){
                paymentInformation.setDeleted(true);
                paymentInformationToDelete.add(paymentInformation);
            }
        }
        paymentInformationRequest = paymentInformationRequest.stream().peek(p -> p.setEmployee(employee)).collect(Collectors.toList());
        paymentInformationRepository.saveAll(paymentInformationRequest);
        paymentInformationRepository.saveAll(paymentInformationToDelete);
    }


}
