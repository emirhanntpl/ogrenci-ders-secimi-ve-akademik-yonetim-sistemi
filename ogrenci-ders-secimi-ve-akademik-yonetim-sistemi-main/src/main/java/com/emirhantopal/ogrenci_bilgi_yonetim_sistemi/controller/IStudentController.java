package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudentIU;

import java.util.List;

public interface IStudentController {

    public DtoStudent studentAdd(DtoStudentIU dtoStudentIU);

    public DtoStudent studentUpdate(Long id, DtoStudentIU dtoStudentIU);

    public void studentDelete(Long id);

    public List<DtoStudent> getAllStudent();




}
