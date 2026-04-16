package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IGradeController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGrade;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGradeIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl.GradeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RequestMapping("/grade")
@RestController
public class GradeControllerImpl implements IGradeController {

    @Autowired
    private GradeServiceImpl gradeService;

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
}
