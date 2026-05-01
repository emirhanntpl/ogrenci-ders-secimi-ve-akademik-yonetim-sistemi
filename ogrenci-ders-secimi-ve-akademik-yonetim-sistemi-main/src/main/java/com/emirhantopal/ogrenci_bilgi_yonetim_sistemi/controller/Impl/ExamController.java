package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IExamController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoExam;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoExamIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
@CrossOrigin(origins = "*")
public class ExamController implements IExamController {

    @Autowired
    private IExamService examService;

    @PostMapping("/add")
    @Override
    public DtoExam addExam(@RequestBody DtoExamIU dtoExamIU) {
        return examService.addExam(dtoExamIU);
    }

    @PutMapping("/update/{id}")
    @Override
    public DtoExam updateExam(@PathVariable Long id, @RequestBody DtoExamIU dtoExamIU) {
        return examService.updateExam(id, dtoExamIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
    }

    @GetMapping("/get/{id}")
    @Override
    public DtoExam getExamById(@PathVariable Long id) {
        return examService.getExamById(id);
    }

    @GetMapping("/all")
    @Override
    public List<DtoExam> getAllExams() {
        return examService.getAllExams();
    }

    @GetMapping("/course-section/{courseSectionId}")
    @Override
    public List<DtoExam> getExamsByCourseSection(@PathVariable Long courseSectionId) {
        return examService.getExamsByCourseSection(courseSectionId);
    }
}
