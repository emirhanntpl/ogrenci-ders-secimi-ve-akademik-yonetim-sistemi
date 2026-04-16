package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IClassroomController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoClassroom;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoClassroomIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IClassroomService;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl.ClassroomService;
import jakarta.persistence.Access;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/classroom")
@RestController
public class ClassroomController implements IClassroomController {

    @Autowired
    private ClassroomService classroomService;


    @PostMapping("/add")
    @Override
    public DtoClassroom classroomAdd(@Valid @RequestBody DtoClassroomIU dtoClassroomIU) {
        return classroomService.classroomAdd(dtoClassroomIU);
    }

    @DeleteMapping("/delete")
    @Override
    public void classroomDelete(@Valid @RequestBody Long id) {
        classroomService.classroomDelete(id);

    }

    @PutMapping("/update")
    @Override
    public DtoClassroom classroomUpdate(@Valid @RequestBody Long id, DtoClassroomIU dtoClassroomIU) {
        return classroomService.classroomUpdate(id, dtoClassroomIU);
    }

    @GetMapping("/{id}")
    @Override
    public DtoClassroom findByClassroomId(@Valid @RequestBody Long id) {
        return classroomService.findByClassroomId(id);
    }

    @GetMapping("/all")
    @Override
    public List<DtoClassroom> getAllClassroom() {
        return classroomService.getAllClassroom();
    }
}
