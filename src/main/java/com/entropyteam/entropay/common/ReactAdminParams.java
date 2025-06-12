package com.entropyteam.entropay.common;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Predicate;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ReactAdminParams {

    private String filter;
    private String range;
    private String sort;

    public ReactAdminParams() {
    }

    public ReactAdminParams(String filter, String range, String sort) {
        this.filter = filter;
        this.range = range;
        this.sort = sort;
    }

    @VisibleForTesting
    public static ReactAdminParams createTestInstance(Map<String, String> filter, int min, int max,
            Map<String, String> sort) {

        JsonObject filterJson = new JsonObject();
        filter.forEach(filterJson::addProperty);

        JsonArray rangeJson = new JsonArray();
        rangeJson.add(min);
        rangeJson.add(max);

        JsonArray sortJson = new JsonArray();
        sort.forEach((key, value) -> {
            sortJson.add(key);
            sortJson.add(value);
        });

        return new ReactAdminParams(filterJson.toString(), rangeJson.toString(), sortJson.toString());
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public <T> Comparator<T> getComparator(Class<T> clazz) {
        return ReactAdminMapper.getComparator(this, clazz);
    }

    public <T> Predicate<T> getFilter(Class<T> clazz) {
        return ReactAdminMapper.getFilter(this, clazz);
    }

    public Range<Integer> getRangeInterval() {
        return ReactAdminMapper.getRange(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("filter", filter)
                .append("range", range)
                .append("sort", sort)
                .toString();
    }
}
