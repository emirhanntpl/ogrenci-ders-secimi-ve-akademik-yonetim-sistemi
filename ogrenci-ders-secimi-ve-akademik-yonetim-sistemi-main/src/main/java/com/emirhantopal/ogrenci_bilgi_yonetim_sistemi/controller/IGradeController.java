package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGrade;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGradeIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.GradeUpdateRequest; // Yeni eklendi
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam; // Artık kullanılmadığı için kaldırılabilir, ancak şimdilik bırakıyorum

import java.util.List;

public interface IGradeController {

    DtoGrade gradeAdd(@RequestBody DtoGradeIU dtoGradeIU);

    DtoGrade gradeUpdate(@PathVariable Long id, @RequestBody DtoGradeIU dtoGradeIU);

    void gradeDelete(Long id); // @PathVariable burada belirtilmez, implementasyonda belirtilir

    List<DtoGrade> getAllGrades();

    DtoGrade findByGradeId(@PathVariable Long id);

    List<DtoGrade> getGradesByCourseSectionAndStudent(@PathVariable Long courseSectionId, @PathVariable Long studentId);

    DtoGrade updateStudentGrade(
            @PathVariable Long courseSectionId,
            @PathVariable Long studentId,
            @RequestBody GradeUpdateRequest request); // İmza güncellendi
}
