package com.entropyteam.entropay.common;

import java.util.ArrayList;

public class ReactAdminParams {

    private String filter;
    private String range;
    private String sort;
    private ArrayList<String> columns;

    public ReactAdminParams() {
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

    public ArrayList<String> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }
}
