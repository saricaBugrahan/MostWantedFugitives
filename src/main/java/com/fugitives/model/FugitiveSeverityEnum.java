package com.fugitives.model;


import lombok.Getter;

@Getter
public enum FugitiveSeverityEnum {

    RED("kirmizi","red"),
    BLUE("mavi","blue"),
    GREEN("yesil","green"),
    ORANGE("turuncu","orange"),
    GRAY("gri","gray");

    private final String color;
    private final String colorName;

    FugitiveSeverityEnum(String color, String colorName) {
        this.color = color;
        this.colorName = colorName;
    }
}
