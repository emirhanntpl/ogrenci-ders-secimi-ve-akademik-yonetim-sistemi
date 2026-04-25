package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoEnrollment extends DtoBaseEntity {
    private DtoStudent student;
    private DtoCourseSection courseSection;
    private LocalDateTime enrollmentDate;
}