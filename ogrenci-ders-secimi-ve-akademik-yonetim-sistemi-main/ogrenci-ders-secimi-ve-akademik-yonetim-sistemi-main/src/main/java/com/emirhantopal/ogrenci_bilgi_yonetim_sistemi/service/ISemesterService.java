package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoSemester;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoSemesterIU;

import java.util.List;

public interface ISemesterService {


    public DtoSemester semesterAdd(DtoSemesterIU dtoSemesterIU);

    public DtoSemester semesterUpdate(Long id,DtoSemesterIU dtoSemesterIU);

    public void  semesterDelete(Long id);

    public List<DtoSemester> getAllSemesters();

    public DtoSemester findBySemesterId(Long id);










}
