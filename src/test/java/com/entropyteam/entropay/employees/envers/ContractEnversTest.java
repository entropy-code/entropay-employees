package com.entropyteam.entropay.employees.envers;

import com.entropyteam.entropay.employees.repositories.ContractRepository;
import com.entropyteam.entropay.testcontainerInit.EnableTestcontainers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@SpringBootTest
@EnableTestcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
class ContractEnversTest {

    @Autowired
    private ContractRepository contractRepository;

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @WithMockUser(username = "admin", authorities = { "ROLE_ADMIN"})
    void testSaveContractAndCheckRevision() {
        var contract = contractRepository.findById(UUID.fromString("2b7002a2-b12b-430f-9f37-ff46a0683c4e"))
                .orElseThrow();
        contract.setHoursPerMonth(40);
        contract.setBenefits("Initial Benefits");
        contractRepository.save(contract);
        var revisions = contractRepository.findRevisions(contract.getId());
        assertEquals(1, revisions.getContent().size());
    }
}
