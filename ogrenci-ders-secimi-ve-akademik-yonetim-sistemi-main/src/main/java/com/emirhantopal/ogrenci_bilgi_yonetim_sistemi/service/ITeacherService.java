package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacherIU;

import java.util.List;

public interface ITeacherService {

    DtoTeacher teacherAdd(DtoTeacherIU dtoTeacherIU);

    DtoTeacher teacherUpdate(Long id, DtoTeacherIU dtoTeacherIU);

    void teacherDelete(Long id);

    List<DtoTeacher> teacherAll();

    DtoTeacher findByTeacherId(Long id);

    List<DtoCourseSection> getTeacherCourseSections(Long teacherId);

    List<DtoStudent> getStudentsByCourseSection(Long courseSectionId);

    DtoTeacher findByUsername(String username);
}
