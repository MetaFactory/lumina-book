package com.metafactory.luminabook.service.dto;

public class SelectOptionDTO {

    private Object id;
    private String displayValue;

    public SelectOptionDTO() {}

    public SelectOptionDTO(Object id, String displayValue) {
        this.id = id;
        this.displayValue = displayValue;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }
}
