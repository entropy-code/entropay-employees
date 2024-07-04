package com.entropyteam.entropay.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("filter", filter)
                .append("range", range)
                .append("sort", sort)
                .toString();
    }
}
