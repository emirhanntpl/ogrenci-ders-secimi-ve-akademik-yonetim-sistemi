package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoExam extends DtoBaseEntity {
    private String examName;
    private String examDate; // Frontend'den string olarak alıp parse edeceğiz
    private String classroom;
    private DtoCourseSection courseSection;
}
