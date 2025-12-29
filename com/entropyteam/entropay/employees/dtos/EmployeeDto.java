        public EmployeeDto(Employee employee, List<PaymentInformation> paymentInformationList, List<Children> childrenList,
                Assignment lastAssignment, Contract firstContract, Integer availableDays, Contract activeContract,
                LocalDate nearestPto, String timeSinceStart) {
            this(builder()
                    .id(employee.getId())
                    .internalId(employee.getInternalId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .gender(employee.getGender())
                    .personalEmail(employee.getPersonalEmail())
                    .phoneNumber(employee.getPhoneNumber())
                    .mobileNumber(employee.getMobileNumber())
                    .address(employee.getAddress())
                    .city(employee.getCity())
                    .state(employee.getState())
                    .zip(employee.getZip())
                    .countryId(employee.getCountry().getId())
                    .personalNumber(employee.getPersonalNumber())
                    .taxId(employee.getTaxId())
                    .emergencyContactFullName(employee.getEmergencyContactFullName())
                    .emergencyContactPhone(employee.getEmergencyContactPhone())
                    .profile(employee.getRoles().stream().map(BaseEntity::getId).collect(Collectors.toList()))
                    .notes(employee.getNotes())
                    .healthInsurance(employee.getHealthInsurance())
                    .paymentInformation(paymentInformationList.stream().map(PaymentInformationDto::new).toList())
                    .children(childrenList.stream().map(ChildrenDto::new).toList())
                    .labourEmail(employee.getLabourEmail())
                    .birthDate(employee.getBirthDate())
                    .createdAt(employee.getCreatedAt())
                    .modifiedAt(employee.getModifiedAt())
                    .deleted(employee.isDeleted())
                    .project(lastAssignment != null ? lastAssignment.getProject().getName() : "-")
                    .client(lastAssignment != null ? lastAssignment.getProject().getClient().getName() : "-")
                    .role(lastAssignment != null ? lastAssignment.getRole().getName() : "-")
                    .lastAssignmentId(lastAssignment != null ? lastAssignment.getId() : null)
                    .startDate(firstContract != null ? firstContract.getStartDate() : null)
                    .endDate(activeContract != null ? activeContract.getEndDate() : null)
                    .active(employee.isActive())
                    .availableDays(availableDays != null ? availableDays : 0)
                    .nearestPto(nearestPto)
                    .timeSinceStart(timeSinceStart)
                    .countryName(employee.getCountry().getName())
                    .build());
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private UUID id;
            private String internalId;
            private String firstName;
            private String lastName;
            private Gender gender;
            private String personalEmail;
            private String phoneNumber;
            private String mobileNumber;
            private String address;
            private String city;
            private String state;
            private String zip;
            private UUID countryId;
            private String personalNumber;
            private String taxId;
            private String emergencyContactFullName;
            private String emergencyContactPhone;
            private List<UUID> profile;
            private String notes;
            private String healthInsurance;
            private List<PaymentInformationDto> paymentInformation;
            private List<ChildrenDto> children;
            private String labourEmail;
            private LocalDate birthDate;
            private LocalDateTime createdAt;
            private LocalDateTime modifiedAt;
            private boolean deleted;
            private String project;
            private String client;
            private String role;
            private UUID lastAssignmentId;
            private LocalDate startDate;
            private LocalDate endDate;
            private boolean active;
            private Integer availableDays;
            private LocalDate nearestPto;
            private String timeSinceStart;
            private String countryName;

            public Builder id(UUID id) { this.id = id; return this; }
            public Builder internalId(String internalId) { this.internalId = internalId; return this; }
            public Builder firstName(String firstName) { this.firstName = firstName; return this; }
            public Builder lastName(String lastName) { this.lastName = lastName; return this; }
            public Builder gender(Gender gender) { this.gender = gender; return this; }
            public Builder personalEmail(String personalEmail) { this.personalEmail = personalEmail; return this; }
            public Builder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
            public Builder mobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; return this; }
            public Builder address(String address) { this.address = address; return this; }
            public Builder city(String city) { this.city = city; return this; }
            public Builder state(String state) { this.state = state; return this; }
            public Builder zip(String zip) { this.zip = zip; return this; }
            public Builder countryId(UUID countryId) { this.countryId = countryId; return this; }
            public Builder personalNumber(String personalNumber) { this.personalNumber = personalNumber; return this; }
            public Builder taxId(String taxId) { this.taxId = taxId; return this; }
            public Builder emergencyContactFullName(String emergencyContactFullName) { this.emergencyContactFullName = emergencyContactFullName; return this; }
            public Builder emergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; return this; }
            public Builder profile(List<UUID> profile) { this.profile = profile; return this; }
            public Builder notes(String notes) { this.notes = notes; return this; }
            public Builder healthInsurance(String healthInsurance) { this.healthInsurance = healthInsurance; return this; }
            public Builder paymentInformation(List<PaymentInformationDto> paymentInformation) { this.paymentInformation = paymentInformation; return this; }
            public Builder children(List<ChildrenDto> children) { this.children = children; return this; }
            public Builder labourEmail(String labourEmail) { this.labourEmail = labourEmail; return this; }
            public Builder birthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
            public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public Builder modifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; return this; }
            public Builder deleted(boolean deleted) { this.deleted = deleted; return this; }
            public Builder project(String project) { this.project = project; return this; }
            public Builder client(String client) { this.client = client; return this; }
            public Builder role(String role) { this.role = role; return this; }
            public Builder lastAssignmentId(UUID lastAssignmentId) { this.lastAssignmentId = lastAssignmentId; return this; }
            public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
            public Builder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
            public Builder active(boolean active) { this.active = active; return this; }
            public Builder availableDays(Integer availableDays) { this.availableDays = availableDays; return this; }
            public Builder nearestPto(LocalDate nearestPto) { this.nearestPto = nearestPto; return this; }
            public Builder timeSinceStart(String timeSinceStart) { this.timeSinceStart = timeSinceStart; return this; }
            public Builder countryName(String countryName) { this.countryName = countryName; return this; }

            public EmployeeDto build() {
                return new EmployeeDto(id, internalId, firstName, lastName, gender, personalEmail, phoneNumber,
                        mobileNumber, address, city, state, zip, countryId, personalNumber, taxId,
                        emergencyContactFullName, emergencyContactPhone, profile, notes, healthInsurance,
                        paymentInformation, children, labourEmail, birthDate, createdAt, modifiedAt, deleted,
                        project, client, role, lastAssignmentId, startDate, endDate, active, availableDays,
                        nearestPto, timeSinceStart, countryName);
            }
        }
    }
