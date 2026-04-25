package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.ITeacherController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacherIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Teacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl.TeacherServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/teacher")
@RestController
public class TeacherController implements ITeacherController {

    @Autowired
    private TeacherServiceImpl teacherService;


    @PostMapping("/add")
    @Override
    public DtoTeacher teacherAdd(@Valid @RequestBody DtoTeacherIU dtoTeacherIU) {
        return teacherService.teacherAdd(dtoTeacherIU);
    }

    @PutMapping("/update/{id}")
    @Override
    public DtoTeacher teacherUpdate(@PathVariable Long id, @Valid @RequestBody DtoTeacherIU dtoTeacherIU) {
        return teacherService.teacherUpdate(id, dtoTeacherIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void teacherDelete(@PathVariable Long id) {
        teacherService.teacherDelete(id);

    }

    @GetMapping("/all")
    @Override
    public List<DtoTeacher> teacherAll() {
        return teacherService.teacherAll();
    }

    @GetMapping("/{id}")
    @Override
    public DtoTeacher findByTeacherId(@PathVariable Long id) {
        return teacherService.findByTeacherId(id);
    }
}
