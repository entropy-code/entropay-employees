package com.entropyteam.entropay.common;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class Filter {

    private Map<String, List<UUID>> getByIdsFilter;
    private Map<String, Object> getByFieldsFilter;
    private Map<String, UUID> getByRelatedFieldsFilter;
    private Map<String, LocalDate> getByDateFieldsFilter;

    public Filter(Map<String, List<UUID>> getByIdsFilter, Map<String, Object> getByFieldsFilter,
            Map<String, UUID> getByRelatedFieldsFilter, Map<String, LocalDate> getByDateFieldsFilter) {
        this.getByIdsFilter = getByIdsFilter;
        this.getByFieldsFilter = getByFieldsFilter;
        this.getByRelatedFieldsFilter = getByRelatedFieldsFilter;
        this.getByDateFieldsFilter = getByDateFieldsFilter;
    }

    public Filter(Map<String, Object> getByFieldsFilter) {
        this.getByFieldsFilter = getByFieldsFilter;
    }

    public Map<String, List<UUID>> getGetByIdsFilter() {
        return getByIdsFilter;
    }

    public void setGetByIdsFilter(Map<String, List<UUID>> getByIdsFilter) {
        this.getByIdsFilter = getByIdsFilter;
    }

    public Map<String, Object> getGetByFieldsFilter() {
        return getByFieldsFilter;
    }

    public void setGetByFieldsFilter(Map<String, Object> getByFieldsFilter) {
        this.getByFieldsFilter = getByFieldsFilter;
    }

    public Map<String, UUID> getGetByRelatedFieldsFilter() {
        return getByRelatedFieldsFilter;
    }

    public void setGetByRelatedFieldsFilter(Map<String, UUID> getByRelatedFieldsFilter) {
        this.getByRelatedFieldsFilter = getByRelatedFieldsFilter;
    }

    public Map<String, LocalDate> getGetByDateFieldsFilter() {
        return getByDateFieldsFilter;
    }

    public void setGetByDateFieldsFilter(Map<String, LocalDate> getByDateFieldsFilter) {
        this.getByDateFieldsFilter = getByDateFieldsFilter;
    }
}
