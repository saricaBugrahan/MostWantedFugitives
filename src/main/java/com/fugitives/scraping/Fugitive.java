package com.fugitives.scraping;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
public class Fugitive {
    @NonNull
    private String name;
    private String surname;
    private String birthPlace;
    private String birthDate;
    @Nullable
    private String organization;
    private String color;
    @Nullable
    private String b64Image;

    public int getHashID(){
        return name.hashCode()+birthDate.hashCode();
    }
    public String toJson(){
        return "{" +
                "\"name\":\"" + name + "\"," +
                "\"surname\":\"" + surname + "\"," +
                "\"birthPlace\":\"" + birthPlace + "\"," +
                "\"birthDate\":\"" + birthDate + "\"," +
                "\"organization\":\"" + organization + "\"," +
                "\"color\":\"" + color + "\"," +
                "\"b64Image\":\"" + b64Image + "\"" +
                "}";
    }

}
