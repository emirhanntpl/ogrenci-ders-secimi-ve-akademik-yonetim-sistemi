package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoExam;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoExamIU;

import java.util.List;

public interface IExamService {
    DtoExam addExam(DtoExamIU dtoExamIU);
    DtoExam updateExam(Long id, DtoExamIU dtoExamIU);
    void deleteExam(Long id);
    DtoExam getExamById(Long id);
    List<DtoExam> getAllExams();
    List<DtoExam> getExamsByCourseSection(Long courseSectionId);
}
