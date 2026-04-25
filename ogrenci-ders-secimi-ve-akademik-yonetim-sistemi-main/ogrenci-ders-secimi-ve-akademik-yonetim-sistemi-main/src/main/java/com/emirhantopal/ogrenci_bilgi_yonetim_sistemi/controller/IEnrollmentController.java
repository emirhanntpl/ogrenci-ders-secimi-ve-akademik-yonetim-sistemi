package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoEnrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoEnrollmentIU;
import jakarta.validation.Valid;

import java.util.List;

public interface IEnrollmentController {
    DtoEnrollment enrollStudent(@Valid DtoEnrollmentIU dtoEnrollmentIU);
    void dropCourse(Long enrollmentId);
    List<DtoEnrollment> getAllEnrollments();
    DtoEnrollment findEnrollmentById(Long id);
}