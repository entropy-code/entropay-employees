package com.entropyteam.entropay.common;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public class Filter {

    private Map<String, List<UUID>> getByIdsFilter;
    private Map<String, Object> getByFieldsFilter;
    private Map<String, UUID> getByRelatedFieldsFilter;
    private Map<String, String> getByDateFieldsFilter;

    public Filter(Map<String, List<UUID>> getByIdsFilter, Map<String, Object> getByFieldsFilter,
            Map<String, UUID> getByRelatedFieldsFilter, Map<String, String> getByDateFieldsFilter) {
        this.getByIdsFilter = getByIdsFilter;
        this.getByFieldsFilter = getByFieldsFilter;
        this.getByRelatedFieldsFilter = getByRelatedFieldsFilter;
        this.getByDateFieldsFilter = getByDateFieldsFilter;
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

    public Map<String, String> getGetByDateFieldsFilter() {
        return getByDateFieldsFilter;
    }

    public void setGetByDateFieldsFilter(Map<String, String> getByDateFieldsFilter) {
        this.getByDateFieldsFilter = getByDateFieldsFilter;
    }
}
