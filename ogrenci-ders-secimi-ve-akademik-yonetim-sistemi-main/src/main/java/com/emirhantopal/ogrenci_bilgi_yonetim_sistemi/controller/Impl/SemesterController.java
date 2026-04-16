package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.ISemesterController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoSemester;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoSemesterIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.ISemesterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/semester")
@RestController
public class SemesterController implements ISemesterController {

    @Autowired
    private ISemesterService semesterService;


    @PostMapping("/add")
    @Override
    public DtoSemester semesterAdd(@RequestBody @Valid DtoSemesterIU dtoSemesterIU) {
        return semesterService.semesterAdd(dtoSemesterIU);
    }


    @PutMapping("/update/{id}")
    @Override
    public DtoSemester semesterUpdate(@PathVariable Long id, @RequestBody @Valid DtoSemesterIU dtoSemesterIU) {
        return semesterService.semesterUpdate(id, dtoSemesterIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void semesterDelete(@PathVariable Long id) {
     semesterService.semesterDelete(id);
    }

    @GetMapping("/all")
    @Override
    public List<DtoSemester> getAllSemesters() {
        return semesterService.getAllSemesters();
    }

    @GetMapping("/{id}")
    @Override
    public DtoSemester findBySemesterId(@PathVariable Long id) {
        return semesterService.findBySemesterId(id);
    }
}
