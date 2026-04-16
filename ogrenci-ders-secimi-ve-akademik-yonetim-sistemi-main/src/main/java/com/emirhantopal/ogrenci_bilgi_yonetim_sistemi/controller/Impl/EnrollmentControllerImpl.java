package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IEnrollmentController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoEnrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoEnrollmentIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IEnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
public class EnrollmentControllerImpl implements IEnrollmentController {

    @Autowired
    private IEnrollmentService enrollmentService;

    @PostMapping("/enroll")
    @Override
    public DtoEnrollment enrollStudent(@Valid @RequestBody DtoEnrollmentIU dtoEnrollmentIU) {
        return enrollmentService.enrollStudent(dtoEnrollmentIU);
    }

    @DeleteMapping("/drop/{id}")
    @Override
    public void dropCourse(@PathVariable("id") Long enrollmentId) {
        enrollmentService.dropCourse(enrollmentId);
    }

    @GetMapping("/all")
    @Override
    public List<DtoEnrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    @Override
    public DtoEnrollment findEnrollmentById(@PathVariable Long id) {
        return enrollmentService.findEnrollmentById(id);
    }
}