package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.ITeacherController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacherIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.ITeacherService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/teacher")
@RestController
@CrossOrigin(origins = "*")
public class TeacherController implements ITeacherController {

    private final ITeacherService teacherService;

    public TeacherController(ITeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/add")
    @Override
    public DtoTeacher teacherAdd(@Valid @RequestBody DtoTeacherIU dtoTeacherIU) {
        return teacherService.teacherAdd(dtoTeacherIU);
    }

    @PutMapping("/update/{id}")
    @Override
    public DtoTeacher teacherUpdate(@PathVariable Long id, @Valid @RequestBody DtoTeacherIU dtoTeacherIU) {
        return teacherService.teacherUpdate(id, dtoTeacherIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void teacherDelete(@PathVariable Long id) {
        teacherService.teacherDelete(id);

    }

    @GetMapping("/all")
    @Override
    public List<DtoTeacher> teacherAll() {
        return teacherService.teacherAll();
    }

    @GetMapping("/{id}")
    @Override
    public DtoTeacher findByTeacherId(@PathVariable Long id) {
        return teacherService.findByTeacherId(id);
    }

    @GetMapping("/{teacherId}/course-sections")
    @Override
    public List<DtoCourseSection> getTeacherCourseSections(@PathVariable Long teacherId) {
        return teacherService.getTeacherCourseSections(teacherId);
    }

    @GetMapping("/course-section/{courseSectionId}/students")
    @Override
    public List<DtoStudent> getStudentsByCourseSection(@PathVariable Long courseSectionId) {
        return teacherService.getStudentsByCourseSection(courseSectionId);
    }

    @GetMapping("/by-username")
    @Override
    public DtoTeacher findByUsername(@RequestParam String username) {
        return teacherService.findByUsername(username);
    }
}