package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoEnrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoEnrollmentIU;

import java.util.List;

public interface IEnrollmentService {
    DtoEnrollment enrollStudent(DtoEnrollmentIU dtoEnrollmentIU);
    void dropCourse(Long enrollmentId);
    List<DtoEnrollment> getAllEnrollments();
    DtoEnrollment findEnrollmentById(Long id);
}