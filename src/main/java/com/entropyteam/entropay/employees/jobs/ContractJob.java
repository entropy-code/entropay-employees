package com.entropyteam.entropay.employees.jobs;

import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.employees.repositories.ContractRepository;


@Component
public class ContractJob {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ContractRepository contractRepository;

    @Autowired
    public ContractJob(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    //Job to execute every day at 9:00 AM
    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void updateContractsStatus() {
        LOGGER.info("Starting contracts update job");
        contractRepository.findAllByDeletedIsFalseAndActiveIsTrueAndEndDateLessThan(LocalDate.now())
                .forEach(contract -> {
                    LOGGER.info("Ending contract with id {} for employee {}", contract.getId(),
                            contract.getEmployee().getId());
                    contract.setActive(false);
                    contractRepository.save(contract);
                });
        contractRepository.findAllContractsToActivateInDate(LocalDate.now())
                .forEach(contract -> {
                    LOGGER.info("Starting contract with id {} for employee {}", contract.getId(),
                            contract.getEmployee().getId());
                    contract.setActive(true);
                    contractRepository.save(contract);
                });
    }
}
