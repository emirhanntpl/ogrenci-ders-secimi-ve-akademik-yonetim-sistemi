package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoCourseSection extends DtoBaseEntity {

    private DtoCourse course;
    
    private DtoTeacher teacher;
    
    private DtoClassroom classroom;
    
    private DtoSemester semester;

}