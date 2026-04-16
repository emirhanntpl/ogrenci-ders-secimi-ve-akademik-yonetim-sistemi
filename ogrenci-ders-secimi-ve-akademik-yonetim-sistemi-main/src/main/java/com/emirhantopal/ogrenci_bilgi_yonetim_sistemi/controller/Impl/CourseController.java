package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.ICourseController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl.CourseServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/course")
@RestController
public class CourseController implements ICourseController {


    @Autowired
    private CourseServiceImpl courseService;

    @PostMapping("/add")
    @Override
    public DtoCourse courseAdd(@Valid @RequestBody DtoCourseIU dtoCourseIU) {
        return courseService.courseAdd(dtoCourseIU);
    }

    @PutMapping("/update")
    @Override
    public DtoCourse courseUpdate(@Valid @RequestBody Long id, DtoCourseIU dtoCourseIU) {
        return courseService.courseUpdate(id, dtoCourseIU);
    }

    @DeleteMapping("/delete")
    @Override
    public void courseDelete(@Valid @RequestBody Long id) {
        courseService.courseDelete(id);
    }

    @GetMapping("/all")
    @Override
    public List<DtoCourse> courseAll() {
        return courseService.courseAll();
    }
}
