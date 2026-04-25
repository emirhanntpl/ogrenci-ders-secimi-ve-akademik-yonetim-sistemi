package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DtoSemester extends DtoBaseEntity  {

    private String term;

    Boolean isActive;





}
