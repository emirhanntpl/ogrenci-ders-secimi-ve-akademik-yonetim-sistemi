package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoClassroom;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoClassroomIU;

import java.util.List;

public interface IClassroomController {

    public DtoClassroom classroomAdd(DtoClassroomIU dtoClassroomIU);

    public void classroomDelete(Long id);

    public DtoClassroom classroomUpdate(Long id, DtoClassroomIU dtoClassroomIU);

    public DtoClassroom findByClassroomId(Long id);

    public List<DtoClassroom> getAllClassroom();


}
