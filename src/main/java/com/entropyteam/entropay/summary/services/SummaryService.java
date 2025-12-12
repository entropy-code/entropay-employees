package com.entropyteam.entropay.summary.services;

import com.entropyteam.entropay.common.*;
import com.entropyteam.entropay.employees.models.Employee;
import com.entropyteam.entropay.employees.models.EmployeeFeedback;
import com.entropyteam.entropay.employees.repositories.EmployeeFeedbackRepository;
import com.entropyteam.entropay.summary.dtos.EmployeeSummaryDto;
import com.entropyteam.entropay.summary.model.EmployeeSummary;
import com.entropyteam.entropay.summary.repository.EmployeeSummaryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class SummaryService extends BaseService<EmployeeSummary, EmployeeSummaryDto, UUID> {

    private final EmployeeSummaryRepository summaryRepository;
    private final EmployeeFeedbackRepository feedbackRepository;
    private final ChatClient chatClient;

    public SummaryService(EmployeeSummaryRepository summaryRepository,
                          EmployeeFeedbackRepository feedbackRepository,
                          ReactAdminMapper mapper,
                          ChatClient.Builder chatClientBuilder) {
        super(EmployeeSummary.class, mapper);
        this.summaryRepository = summaryRepository;
        this.feedbackRepository = feedbackRepository;
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    protected BaseRepository<EmployeeSummary, UUID> getRepository() {
        return summaryRepository;
    }

    @Override
    public EmployeeSummaryDto toDTO(EmployeeSummary entity) {
        return new EmployeeSummaryDto(entity);
    }

    @Override
    public EmployeeSummary toEntity(EmployeeSummaryDto dto) {
        return new EmployeeSummary(dto);
    }

   /* public List<EmployeeFeedback> getFeedbacksByEmployeeId(UUID employeeId) {
        return feedbackRepository.findByEmployee_IdAndDeletedFalse(employeeId);
    }*/

    public String generateSummary(String employeeName, List<String> feedbacks) {

        if (feedbacks == null || feedbacks.isEmpty()) {
            return "No hay feedback disponible para este empleado.";
        }

        /*String feedbackText = String.join("\n", feedbacks);*/
        String feedbackText = feedbacks.stream().distinct().collect(Collectors.joining("\n"));

        System.out.println("Feedbacks: " + feedbacks);

        String prompt = String.format(
                "Resume el siguiente feedback para el empleado %s en un párrafo claro y profesional:\n%s",
                employeeName, feedbackText
        );

        var response = chatClient.prompt(prompt).call();
       // return chatClient.prompt(prompt).call().content();
        System.out.println("Respuesta completa: " + response);
        System.out.println("Content: " + response.content());
        return response.content();
    }


 /****************************************************/

public String generateSummaryByEmployeeId(UUID employeeId, String customPrompt) {
    // 1. Buscar todos los feedbacks del empleado
    List<EmployeeFeedback> feedbacks = feedbackRepository.findAllByEmployee_IdAndDeletedFalse(employeeId);

    if (feedbacks.isEmpty()) {
        return "No hay feedback disponible para este empleado.";
    }
    System.out.println(feedbacks);
    // 2. Concatenar los textos de feedback
    String feedbackText = feedbacks.stream()
            .map(EmployeeFeedback::getText)
            .distinct()
            .collect(Collectors.joining("\n"));
    System.out.println(feedbackText);
    // 3. Obtener nombre del empleado (desde el primer feedback)
    String employeeName = feedbacks.stream()
            .findFirst().filter(f -> f.getEmployee() != null).map(f -> f.getEmployee().getFullName()).orElse("Empleado inexistente");

    // 4. Construir el prompt
    String prompt;
    if (customPrompt != null && !customPrompt.isBlank()) {
        prompt = String.format("%s\n\nFeedback del empleado %s:\n%s",
                customPrompt, employeeName, feedbackText);
    } else {
        prompt = String.format(
                "Resume el siguiente feedback para el empleado %s en un párrafo claro y profesional:\n%s",
                employeeName, feedbackText
        );
    }

    // 5. Llamar al ChatClient
    var response = chatClient.prompt(prompt).call();
    return response.content();
}







}





