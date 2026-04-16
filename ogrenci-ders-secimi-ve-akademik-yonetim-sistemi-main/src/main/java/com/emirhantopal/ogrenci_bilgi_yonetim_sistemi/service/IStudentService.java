package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudentIU;

import java.util.List;

public interface IStudentService {
    DtoStudent studentAdd(DtoStudentIU dtoStudentIU);
    DtoStudent studentUpdate(Long id, DtoStudentIU dtoStudentIU);
    void studentDelete(Long id);
    List<DtoStudent> getAllStudents();
    DtoStudent findByStudentId(Long id);
}