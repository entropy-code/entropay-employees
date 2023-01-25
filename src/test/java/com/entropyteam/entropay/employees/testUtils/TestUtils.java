package com.entropyteam.entropay.employees.testUtils;

import com.entropyteam.entropay.employees.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class TestUtils {
    public static Contract aContract() {
        Contract contract = new Contract();
        contract.setCompany(aCompany());
        contract.setEmployee(anEmployee());
        contract.setRole(aRole());
        contract.setSeniority(aSeniority());
        contract.setStartDate(LocalDate.of(2019, 5, 22));
        contract.setHoursPerMonth(48);
        contract.setVacations(15);
        contract.setActive(true);
        contract.setContractType(ContractType.CONTRACTOR);
        return contract;
    }

    public static Seniority aSeniority() {
        Seniority seniority = new Seniority();
        seniority.setName("Senior");
        return seniority;
    }

    public static Role aRole() {
        Role role = new Role();
        role.setName("Software Engineers");
        role.setEmployees(Set.of(anEmployee()));
        return role;
    }

    public static Employee anEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Satoshi");
        employee.setLastName("Nakamoto");
        employee.setPersonalEmail("satoshin@gmx.com");
        employee.setBirthDate(LocalDate.of(1975, 4, 5));
        employee.setCountry("Japan");
        employee.setTaxId("201234310");
        employee.setEmergencyContactFullName("Dorian Nakamoto");
        employee.setEmergencyContactPhone("+1503123513");
        return employee;
    }

    public static Company aCompany() {
        Company company = new Company();
        company.setName("Test Company");
        company.setAddress(anAddress());
        company.setTenant(aTenant());
        return company;
    }

    public static Tenant aTenant() {
        Tenant tenant = new Tenant();
        tenant.setName("The Great Wall");
        tenant.setDisplayName("TGW");
        return tenant;
    }

    public static Address anAddress() {
        Address address = new Address();
        address.setCountry("USA");
        address.setState("Massachusetts");
        address.setCity("Springfield");
        address.setAddressLine("Ever Green St.");
        address.setZipCode("01089");
        return address;
    }
}
