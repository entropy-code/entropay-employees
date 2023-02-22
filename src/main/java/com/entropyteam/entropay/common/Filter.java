package com.entropyteam.entropay.common;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public class Filter {

    private Map<String, List<UUID>> getByIdsFilter;
    private Map<String, String> getByFieldsFilter;
    private Map<String, UUID> getByRelatedFieldsFilter;

    public Filter(Map<String, List<UUID>> getByIdsFilter, Map<String, String> getByFieldsFilter,
            Map<String, UUID> getByRelatedFieldsFilter) {
        this.getByIdsFilter = getByIdsFilter;
        this.getByFieldsFilter = getByFieldsFilter;
        this.getByRelatedFieldsFilter = getByRelatedFieldsFilter;
    }

    public Map<String, List<UUID>> getGetByIdsFilter() {
        return getByIdsFilter;
    }

    public void setGetByIdsFilter(Map<String, List<UUID>> getByIdsFilter) {
        this.getByIdsFilter = getByIdsFilter;
    }

    public Map<String, String> getGetByFieldsFilter() {
        return getByFieldsFilter;
    }

    public void setGetByFieldsFilter(Map<String, String> getByFieldsFilter) {
        this.getByFieldsFilter = getByFieldsFilter;
    }

    public Map<String, UUID> getGetByRelatedFieldsFilter() {
        return getByRelatedFieldsFilter;
    }

    public void setGetByRelatedFieldsFilter(Map<String, UUID> getByRelatedFieldsFilter) {
        this.getByRelatedFieldsFilter = getByRelatedFieldsFilter;
    }
}
