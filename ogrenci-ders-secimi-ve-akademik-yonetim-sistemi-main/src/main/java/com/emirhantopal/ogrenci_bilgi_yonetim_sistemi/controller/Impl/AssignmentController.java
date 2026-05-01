package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IAssignmentController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAssignment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAssignmentIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignment")
@CrossOrigin(origins = "*")
public class AssignmentController implements IAssignmentController {

    @Autowired
    private IAssignmentService assignmentService;

    @PostMapping("/add")
    @Override
    public DtoAssignment addAssignment(@RequestBody DtoAssignmentIU dtoAssignmentIU) {
        return assignmentService.addAssignment(dtoAssignmentIU);
    }

    @PutMapping("/update/{id}")
    @Override
    public DtoAssignment updateAssignment(@PathVariable Long id, @RequestBody DtoAssignmentIU dtoAssignmentIU) {
        return assignmentService.updateAssignment(id, dtoAssignmentIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
    }

    @GetMapping("/get/{id}")
    @Override
    public DtoAssignment getAssignmentById(@PathVariable Long id) {
        return assignmentService.getAssignmentById(id);
    }

    @GetMapping("/all")
    @Override
    public List<DtoAssignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    @GetMapping("/course-section/{courseSectionId}")
    @Override
    public List<DtoAssignment> getAssignmentsByCourseSection(@PathVariable Long courseSectionId) {
        return assignmentService.getAssignmentsByCourseSection(courseSectionId);
    }
}
