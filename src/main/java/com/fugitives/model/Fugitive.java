package com.fugitives.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
