package com.entropyteam.entropay.employees.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entropyteam.entropay.auth.AppRole;
import com.entropyteam.entropay.common.BaseRepository;
import com.entropyteam.entropay.common.BaseService;
import com.entropyteam.entropay.common.Filter;
import com.entropyteam.entropay.common.Range;
import com.entropyteam.entropay.common.Sort;
import com.entropyteam.entropay.employees.dtos.ConfigDto;
import com.entropyteam.entropay.employees.dtos.MenuItemDto;
import com.entropyteam.entropay.employees.dtos.PermissionDto;
import com.entropyteam.entropay.employees.models.Config;
import com.entropyteam.entropay.employees.repositories.ConfigRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConfigService extends BaseService<Config, ConfigDto, UUID> {

    ObjectMapper MAPPER = new ObjectMapper();
    private final ConfigRepository configRepository;


    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    @Transactional
    public List<ConfigDto> findAllActive(Filter filter, Sort sort, Range range) {
        AppRole userRole = getUserRole();
        return configRepository.findAllByDeletedIsFalseAndRole(userRole)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    protected BaseRepository<Config, UUID> getRepository() {
        return configRepository;
    }

    @Override
    protected ConfigDto toDTO(Config entity) {
        try {
            List<PermissionDto> permissions = MAPPER.readValue(entity.getPermissions(), new TypeReference<List<PermissionDto>>(){});
            List<MenuItemDto> menuItems = MAPPER.readValue(entity.getMenu(), new TypeReference<List<MenuItemDto>>(){});
            return new ConfigDto(entity, permissions, menuItems);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing config json");
        }
    }

    @Override
    protected Config toEntity(ConfigDto entity) {
        return null;
    }
}
