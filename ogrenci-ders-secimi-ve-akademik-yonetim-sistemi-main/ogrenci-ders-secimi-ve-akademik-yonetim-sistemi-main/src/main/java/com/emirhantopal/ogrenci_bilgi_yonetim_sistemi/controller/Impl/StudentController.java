package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IStudentController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudentIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IStudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/student")
public class StudentController implements IStudentController {

    @Autowired
    private IStudentService studentService;


    @PostMapping("/add")
    @Override
    public DtoStudent studentAdd(@Valid @RequestBody DtoStudentIU dtoStudentIU) {
        return studentService.studentAdd(dtoStudentIU);
    }

    @PutMapping("/update/{id}")
    @Override
    public DtoStudent studentUpdate(@Valid @RequestBody Long id, DtoStudentIU dtoStudentIU) {
        return studentService.studentUpdate(id, dtoStudentIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void studentDelete(@Valid @RequestBody Long id) {
        studentService.studentDelete(id);
    }

    @GetMapping("/getAll")
    @Override
    public List<DtoStudent> getAllStudent() {
        return studentService.getAllStudents();
    }

    @GetMapping("/getById/{id}")
    @Override
    public DtoStudent getStudentById(Long id) {
        return studentService.getAllStudents().get(Math.toIntExact(id));
    }
}
