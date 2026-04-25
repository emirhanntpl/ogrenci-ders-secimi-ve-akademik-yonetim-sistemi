package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacherIU;

import java.util.List;

public interface ITeacherController {

    public DtoTeacher teacherAdd(DtoTeacherIU dtoTeacherIU);

    public DtoTeacher teacherUpdate(Long id,DtoTeacherIU dtoTeacherIU);

    public void teacherDelete(Long id);

    public List<DtoTeacher> teacherAll();

    public DtoTeacher findByTeacherId(Long id);




}
