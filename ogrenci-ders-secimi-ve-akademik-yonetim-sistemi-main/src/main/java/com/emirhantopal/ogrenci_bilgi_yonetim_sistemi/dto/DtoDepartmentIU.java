package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoDepartmentIU {

    private String name;
    private String code; // Yeni eklendi
    private String quota;
    private String head_of_department;
    private Long facultyId;

}
