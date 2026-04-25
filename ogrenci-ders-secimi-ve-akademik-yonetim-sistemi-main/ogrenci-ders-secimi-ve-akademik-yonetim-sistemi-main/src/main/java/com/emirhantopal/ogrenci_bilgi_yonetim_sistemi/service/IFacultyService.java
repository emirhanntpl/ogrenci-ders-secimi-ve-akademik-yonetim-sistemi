package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFaculty;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFacultyIU;

import java.util.List;

public interface IFacultyService {

    public DtoFaculty facultyAdd(DtoFacultyIU dtoFacultyIU);

    public DtoFaculty facultyUpdate(Long id , DtoFacultyIU dtoFacultyIU);

    public void facultyDelete(Long id);


    public DtoFaculty findByFacultyId(Long id);


    public List<DtoFaculty> getAllFaculty();




}
