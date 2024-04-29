package org.scrape;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Fugitive {
    private String name;
    private String surname;
    private String birthPlace;
    private String birthDate;
    private String organization;
    private String color;
}
