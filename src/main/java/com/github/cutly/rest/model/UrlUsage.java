package com.github.cutly.rest.model;

import java.util.Date;
import java.util.List;

public class UrlUsage {

    private long totalUsage;

    private List<String> accessDates;

    public UrlUsage(long usage, List<String> accessDates) {
        this.totalUsage = usage;
        this.accessDates = accessDates;
    }

    public long getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(long totalUsage) {
        this.totalUsage = totalUsage;
    }

    public List<String> getAccessDates() {
        return accessDates;
    }

    public void setAccessDates(List<String> accessDates) {
        this.accessDates = accessDates;
    }

    @Override
    public String toString() {
        return "UrlUsage{" +
                "totalUsage=" + totalUsage +
                ", accessDates=" + accessDates +
                '}';
    }
}
