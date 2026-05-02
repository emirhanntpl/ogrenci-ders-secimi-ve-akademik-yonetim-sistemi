package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoExam;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoExamIU;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IExamController {
    DtoExam addExam(@RequestBody DtoExamIU dtoExamIU);
    DtoExam updateExam(@PathVariable("id") Long id, @RequestBody DtoExamIU dtoExamIU);
    void deleteExam(@PathVariable("id") Long id);
    DtoExam getExamById(@PathVariable("id") Long id);
    List<DtoExam> getAllExams();
    List<DtoExam> getExamsByCourseSection(@PathVariable("courseSectionId") Long courseSectionId);
}