package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.ICourseSectionController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoCourseSectionIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.ICourseSectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coursesection")
public class CourseSectionController implements ICourseSectionController {

    @Autowired
    private ICourseSectionService courseSectionService;

    @PostMapping("/add")
    @Override
    public DtoCourseSection courseSectionAdd(@Valid @RequestBody DtoCourseSectionIU dtoCourseSectionIU) {
        return courseSectionService.courseSectionAdd(dtoCourseSectionIU);
    }

    @PutMapping("/update/{id}")
    @Override
    public DtoCourseSection courseSectionUpdate(@PathVariable Long id, @Valid @RequestBody DtoCourseSectionIU dtoCourseSectionIU) {
        return courseSectionService.courseSectionUpdate(id, dtoCourseSectionIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void courseSectionDelete(@PathVariable Long id) {
        courseSectionService.courseSectionDelete(id);
    }

    @GetMapping("/all")
    @Override
    public List<DtoCourseSection> getAllCourseSections() {
        return courseSectionService.getAllCourseSections();
    }
}