package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAssignment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAssignmentIU;

import java.util.List;

public interface IAssignmentService {
    DtoAssignment addAssignment(DtoAssignmentIU dtoAssignmentIU);
    DtoAssignment updateAssignment(Long id, DtoAssignmentIU dtoAssignmentIU);
    void deleteAssignment(Long id);
    DtoAssignment getAssignmentById(Long id);
    List<DtoAssignment> getAllAssignments();
    List<DtoAssignment> getAssignmentsByCourseSection(Long courseSectionId);
}
