package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseSectionIU;

import java.util.List;

public interface ICourseSectionService {
    
    DtoCourseSection courseSectionAdd(DtoCourseSectionIU dtoCourseSectionIU);

    DtoCourseSection courseSectionUpdate(Long id, DtoCourseSectionIU dtoCourseSectionIU);

    void courseSectionDelete(Long id);

    List<DtoCourseSection> getAllCourseSections();

}