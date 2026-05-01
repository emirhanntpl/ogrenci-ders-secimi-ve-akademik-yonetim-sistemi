package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacherIU;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ITeacherController {

    public DtoTeacher teacherAdd(DtoTeacherIU dtoTeacherIU);

    public DtoTeacher teacherUpdate(Long id,DtoTeacherIU dtoTeacherIU);

    public void teacherDelete(Long id);

    public List<DtoTeacher> teacherAll();

    public DtoTeacher findByTeacherId(Long id);

    List<DtoCourseSection> getTeacherCourseSections(Long teacherId);

    List<DtoStudent> getStudentsByCourseSection(Long courseSectionId);

    DtoTeacher findByUsername(@RequestParam String username);
}
