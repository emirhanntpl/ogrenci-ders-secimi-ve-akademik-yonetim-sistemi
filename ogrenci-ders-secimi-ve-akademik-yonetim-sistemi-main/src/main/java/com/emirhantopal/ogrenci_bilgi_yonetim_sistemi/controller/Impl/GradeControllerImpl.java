package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IGradeController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGrade;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGradeIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/grade")
@RestController
@CrossOrigin(origins = "*")
public class GradeControllerImpl implements IGradeController {

    @Autowired
    private IGradeService gradeService;

    @PostMapping("/add")
    @Override
    public DtoGrade gradeAdd(@RequestBody DtoGradeIU dtoGradeIU) {
        return gradeService.gradeAdd(dtoGradeIU);
    }

    @PutMapping("/update/{id}")
    @Override
    public DtoGrade gradeUpdate(@PathVariable Long id, @RequestBody DtoGradeIU dtoGradeIU) {
        return gradeService.gradeUpdate(id, dtoGradeIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void gradeDelete(@PathVariable Long id) {
        gradeService.gradeDelete(id);
    }

    @GetMapping("/all")
    @Override
    public List<DtoGrade> getAllGrades() {
        return  gradeService.getAllGrades();
    }

    @GetMapping("/{id}")
    @Override
    public DtoGrade findByGradeId(@PathVariable Long id) {
        return gradeService.findByGradeId(id);
    }

    @GetMapping("/course-section/{courseSectionId}/student/{studentId}")
    @Override
    public List<DtoGrade> getGradesByCourseSectionAndStudent(@PathVariable Long courseSectionId, @PathVariable Long studentId) {
        return gradeService.getGradesByCourseSectionAndStudent(courseSectionId, studentId);
    }

    @PutMapping("/course-section/{courseSectionId}/student/{studentId}/update-grade")
    @Override
    public DtoGrade updateStudentGrade(
            @PathVariable Long courseSectionId,
            @PathVariable Long studentId,
            @RequestParam String examType,
            @RequestParam Double gradeValue) {
        return gradeService.updateStudentGrade(courseSectionId, studentId, examType, gradeValue);
    }
}
