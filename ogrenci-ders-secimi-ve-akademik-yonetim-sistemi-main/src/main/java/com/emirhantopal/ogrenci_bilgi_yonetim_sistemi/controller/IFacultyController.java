package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFaculty;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFacultyIU;

import java.util.List;

public interface IFacultyController {

    public DtoFaculty facultyAdd(DtoFacultyIU dtoFacultyIU);

    public DtoFaculty facultyUpdate(Long id,DtoFacultyIU dtoFacultyIU);

    public void facultyDelete(Long id);

    public DtoFaculty findByFacultyId(Long id);

    public List<DtoFaculty> getAllFaculty();





}
