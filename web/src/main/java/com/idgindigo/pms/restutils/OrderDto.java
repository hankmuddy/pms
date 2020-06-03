package com.idgindigo.pms.restutils;

/**
 * @author vomel
 * @since 29.10.13 15:39
 */
public class OrderDto {
    private String property;
    private String direction;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}