package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoFacultyIU extends DtoBaseEntity {


    private  String name;

    private  String address;

    private  String numberOfPersonel;

    private String available;

    private  String year_of_establishment;

    private String dean;

    private String secretary;








}
