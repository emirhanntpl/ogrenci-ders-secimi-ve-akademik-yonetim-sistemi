package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoClassroom;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoClassroomIU;

import java.util.List;

public interface IClassroomService {

    public DtoClassroom classroomAdd(DtoClassroomIU dtoClassroomIU);

    public void classroomDelete(Long id);

    public DtoClassroom classroomUpdate(Long id, DtoClassroomIU dtoClassroomIU);

    public DtoClassroom findByClassroomId(Long id);

    public List<DtoClassroom> getAllClassroom();


}
