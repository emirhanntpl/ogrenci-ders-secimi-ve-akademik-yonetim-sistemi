package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseSectionIU;
import jakarta.validation.Valid;

import java.util.List;

public interface ICourseSectionController {
    DtoCourseSection courseSectionAdd(@Valid DtoCourseSectionIU dtoCourseSectionIU);
    DtoCourseSection courseSectionUpdate(Long id, @Valid DtoCourseSectionIU dtoCourseSectionIU);
    void courseSectionDelete(Long id);
    List<DtoCourseSection> getAllCourseSections();
}