package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAssignment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAssignmentIU;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IAssignmentController {
    DtoAssignment addAssignment(@RequestBody DtoAssignmentIU dtoAssignmentIU);
    DtoAssignment updateAssignment(@PathVariable("id") Long id, @RequestBody DtoAssignmentIU dtoAssignmentIU);
    void deleteAssignment(@PathVariable("id") Long id);
    DtoAssignment getAssignmentById(@PathVariable("id") Long id);
    List<DtoAssignment> getAllAssignments();
    List<DtoAssignment> getAssignmentsByCourseSection(@PathVariable("courseSectionId") Long courseSectionId);
}