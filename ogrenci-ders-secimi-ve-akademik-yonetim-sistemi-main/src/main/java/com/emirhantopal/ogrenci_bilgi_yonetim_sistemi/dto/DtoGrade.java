package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoGrade extends DtoBaseEntity {

    private Double midTerm;
    private Double finalExam;
    private Double average;
    private String letterGrade; // Yeni eklendi
    private String passStatus;  // Yeni eklendi
}
