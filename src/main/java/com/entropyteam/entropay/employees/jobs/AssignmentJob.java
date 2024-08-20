package com.entropyteam.entropay.employees.jobs;

import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.employees.repositories.AssignmentRepository;


@Component
public class AssignmentJob {

    private static final Logger LOGGER = LogManager.getLogger();
    private final AssignmentRepository assignmentRepository;

    @Autowired
    public AssignmentJob(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    //Job to execute every day at 9:05 AM
    @Scheduled(cron = "0 5 9 * * ?")
    @Transactional
    public void updateAssignmentsStatus() {
        LOGGER.info("Starting assignments update job");
        assignmentRepository.findAllByDeletedIsFalseAndActiveIsTrueAndEndDateLessThan(LocalDate.now())
                .forEach(assignment -> {
                    LOGGER.info("Ending assignment with id {} for employee {}", assignment.getId(),
                            assignment.getEmployee().getId());
                    assignment.setActive(false);
                    assignmentRepository.save(assignment);
                });
        assignmentRepository.findAllAssignmentsToActivateInDate(LocalDate.now())
                .forEach(assignment -> {
                    LOGGER.info("Starting assignment with id {} for employee {}", assignment.getId(),
                            assignment.getEmployee().getId());
                    assignment.setActive(true);
                    assignmentRepository.save(assignment);
                });
    }
}
