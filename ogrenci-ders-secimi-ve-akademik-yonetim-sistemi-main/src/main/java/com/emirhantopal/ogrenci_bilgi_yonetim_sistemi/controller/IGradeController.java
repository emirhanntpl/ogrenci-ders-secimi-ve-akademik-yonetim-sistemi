package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGrade;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGradeIU;

import java.util.List;

public interface IGradeController {

    DtoGrade gradeAdd(DtoGradeIU dtoGradeIU);

    DtoGrade gradeUpdate(Long id, DtoGradeIU dtoGradeIU);

    void gradeDelete(Long id);

    List<DtoGrade> getAllGrades();

    DtoGrade findByGradeId(Long id);

    List<DtoGrade> getGradesByCourseSectionAndStudent(Long courseSectionId, Long studentId);

    DtoGrade updateStudentGrade(Long courseSectionId, Long studentId, String examType, Double gradeValue);
}
