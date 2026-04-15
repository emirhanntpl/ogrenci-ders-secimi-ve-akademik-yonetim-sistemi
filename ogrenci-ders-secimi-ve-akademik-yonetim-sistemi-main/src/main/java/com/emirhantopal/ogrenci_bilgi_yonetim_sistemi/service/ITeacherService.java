package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacherIU;

import java.util.List;

public interface ITeacherService {

    public DtoTeacher teacherAdd(DtoTeacherIU dtoTeacherIU);

    public DtoTeacher teacherUpdate(Long id,DtoTeacherIU dtoTeacherIU);

    public void teacherDelete(Long id);

    public List<DtoTeacher> teacherAll();

    public DtoTeacher findByTeacherId(Long id);



}
