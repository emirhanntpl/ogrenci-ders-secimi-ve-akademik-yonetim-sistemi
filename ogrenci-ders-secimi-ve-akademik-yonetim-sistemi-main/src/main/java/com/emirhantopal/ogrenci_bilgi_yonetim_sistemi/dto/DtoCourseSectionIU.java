package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoCourseSectionIU { // DtoBaseEntity kalıtımı kaldırıldı

    private Long courseId;
    
    private Long teacherId;
    
    private Long classroomId;
    
    private Long semesterId;
}
