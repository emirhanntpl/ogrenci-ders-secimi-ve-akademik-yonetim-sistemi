package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IFacultyController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFaculty;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFacultyIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl.FacultyServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/faculty")
@RestController
public class FacultyController implements IFacultyController {

    @Autowired
    private FacultyServiceImpl facultyService;


    @PostMapping("/add")
    @Override
    public DtoFaculty facultyAdd(@Valid @RequestBody DtoFacultyIU dtoFacultyIU) {
        return facultyService.facultyAdd(dtoFacultyIU);
    }

    @PutMapping("/update")
    @Override
    public DtoFaculty facultyUpdate(@Valid @RequestBody Long id, DtoFacultyIU dtoFacultyIU) {
        return facultyService.facultyUpdate(id, dtoFacultyIU);
    }

    @DeleteMapping("/delete")
    @Override
    public void facultyDelete(@Valid @RequestBody Long id) {
        facultyService.facultyDelete(id);

    }

    @GetMapping("/get")
    @Override
    public DtoFaculty findByFacultyId(@Valid @RequestBody Long id) {
        return facultyService.findByFacultyId(id);
    }

    @GetMapping("/getAll")
    @Override
    public List<DtoFaculty> getAllFaculty() {
        return facultyService.getAllFaculty();
    }
}
