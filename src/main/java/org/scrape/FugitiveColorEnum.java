package org.scrape;

import lombok.Getter;

@Getter
public enum FugitiveColorEnum {
    RED(1, "red"),
    BLUE(2, "blue"),
    GREEN(3, "green"),
    ORANGE(4, "orange"),
    GRAY(5, "gray");

    private final int color;
    private final String colorName;

    FugitiveColorEnum(int color, String colorName) {
        this.color = color;
        this.colorName = colorName;
    }
}