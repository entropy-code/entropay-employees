package com.entropyteam.entropay.employees.payroll;

import java.math.BigDecimal;
import com.entropyteam.entropay.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.entropyteam.entropay.employees.models.Contract;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.Modality;

@Entity(name = "PayrollItem")
@Table(name = "payroll_item")
class PayrollItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_run_id", nullable = false)
    private PayrollRun payrollRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "payment_platform")
    private String paymentPlatform;

    @Enumerated(EnumType.STRING)
    @Column
    private Modality modality;

    @Column(name = "base_salary", precision = 14, scale = 2)
    private BigDecimal baseSalary = BigDecimal.ZERO;

    @Column(name = "proportional_salary", precision = 14, scale = 2)
    private BigDecimal proportionalSalary = BigDecimal.ZERO;

    @Column(name = "country_working_hours_in_month")
    private Integer countryWorkingHoursInMonth;

    @Column(name = "pto_hours_in_month", precision = 8, scale = 2)
    private BigDecimal ptoHoursInMonth = BigDecimal.ZERO;

    @Column(name = "unpaid_leave_hours_in_month", precision = 8, scale = 2)
    private BigDecimal unpaidLeaveHoursInMonth = BigDecimal.ZERO;

    @Column(name = "unpaid_leave_deduction", precision = 14, scale = 2)
    private BigDecimal unpaidLeaveDeduction = BigDecimal.ZERO;

    @Column(name = "billable_hours_in_month", precision = 8, scale = 2)
    private BigDecimal billableHoursInMonth = BigDecimal.ZERO;

    @Column(name = "overtime_hours", precision = 8, scale = 2)
    private BigDecimal overtimeHours = BigDecimal.ZERO;

    @Column(name = "overtime_amount", precision = 14, scale = 2)
    private BigDecimal overtimeAmount = BigDecimal.ZERO;

    @Column(name = "reimbursements_amount", precision = 14, scale = 2)
    private BigDecimal reimbursementsAmount = BigDecimal.ZERO;

    @Column(name = "hardware_clawback", precision = 14, scale = 2)
    private BigDecimal hardwareClawback = BigDecimal.ZERO;

    @Column(name = "vacation_cashout", precision = 14, scale = 2)
    private BigDecimal vacationCashout = BigDecimal.ZERO;

    @Column(name = "adjustment", precision = 14, scale = 2)
    private BigDecimal adjustment = BigDecimal.ZERO;

    @Column(name = "previous_balance", precision = 14, scale = 2)
    private BigDecimal previousBalance = BigDecimal.ZERO;

    @Column(length = 1000)
    private String notes;

    @Column(name = "total_amount", precision = 14, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "is_final_settlement")
    private boolean isFinalSettlement;

    @Column(name = "calculation_error", length = 2000)
    private String calculationError;

    public PayrollItem() {
    }

    public PayrollRun getPayrollRun() {
        return payrollRun;
    }

    public void setPayrollRun(PayrollRun payrollRun) {
        this.payrollRun = payrollRun;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPaymentPlatform() {
        return paymentPlatform;
    }

    public void setPaymentPlatform(String paymentPlatform) {
        this.paymentPlatform = paymentPlatform;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getProportionalSalary() {
        return proportionalSalary;
    }

    public void setProportionalSalary(BigDecimal proportionalSalary) {
        this.proportionalSalary = proportionalSalary;
    }

    public Integer getCountryWorkingHoursInMonth() {
        return countryWorkingHoursInMonth;
    }

    public void setCountryWorkingHoursInMonth(Integer countryWorkingHoursInMonth) {
        this.countryWorkingHoursInMonth = countryWorkingHoursInMonth;
    }

    public BigDecimal getPtoHoursInMonth() {
        return ptoHoursInMonth;
    }

    public void setPtoHoursInMonth(BigDecimal ptoHoursInMonth) {
        this.ptoHoursInMonth = ptoHoursInMonth;
    }

    public BigDecimal getUnpaidLeaveHoursInMonth() {
        return unpaidLeaveHoursInMonth;
    }

    public void setUnpaidLeaveHoursInMonth(BigDecimal unpaidLeaveHoursInMonth) {
        this.unpaidLeaveHoursInMonth = unpaidLeaveHoursInMonth;
    }

    public BigDecimal getUnpaidLeaveDeduction() {
        return unpaidLeaveDeduction;
    }

    public void setUnpaidLeaveDeduction(BigDecimal unpaidLeaveDeduction) {
        this.unpaidLeaveDeduction = unpaidLeaveDeduction;
    }

    public BigDecimal getBillableHoursInMonth() {
        return billableHoursInMonth;
    }

    public void setBillableHoursInMonth(BigDecimal billableHoursInMonth) {
        this.billableHoursInMonth = billableHoursInMonth;
    }

    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public BigDecimal getOvertimeAmount() {
        return overtimeAmount;
    }

    public void setOvertimeAmount(BigDecimal overtimeAmount) {
        this.overtimeAmount = overtimeAmount;
    }

    public BigDecimal getReimbursementsAmount() {
        return reimbursementsAmount;
    }

    public void setReimbursementsAmount(BigDecimal reimbursementsAmount) {
        this.reimbursementsAmount = reimbursementsAmount;
    }

    public BigDecimal getHardwareClawback() {
        return hardwareClawback;
    }

    public void setHardwareClawback(BigDecimal hardwareClawback) {
        this.hardwareClawback = hardwareClawback;
    }

    public BigDecimal getVacationCashout() {
        return vacationCashout;
    }

    public void setVacationCashout(BigDecimal vacationCashout) {
        this.vacationCashout = vacationCashout;
    }

    public BigDecimal getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(BigDecimal adjustment) {
        this.adjustment = adjustment;
    }

    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(BigDecimal previousBalance) {
        this.previousBalance = previousBalance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isFinalSettlement() {
        return isFinalSettlement;
    }

    public void setFinalSettlement(boolean finalSettlement) {
        isFinalSettlement = finalSettlement;
    }

    public String getCalculationError() {
        return calculationError;
    }

    public void setCalculationError(String calculationError) {
        this.calculationError = calculationError;
    }
}
