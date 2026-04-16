package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IDepartmentController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartmentIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/department")
@RestController
public class DepartmentController implements IDepartmentController {


    @Autowired
    private DepartmentService departmentService;


    @PostMapping("/add")
    @Override
    public DtoDepartment departmentAdd(@Valid @RequestBody DtoDepartmentIU dtoDepartmentIU) {
        return departmentService.departmentAdd(dtoDepartmentIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void departmentDelete(@Valid @RequestBody Long id) {
        departmentService.departmentDelete(id);

    }

    @PutMapping("/update/{id}")
    @Override
    public DtoDepartment departmentUpdate(@Valid @RequestBody Long id, DtoDepartmentIU dtoDepartmentIU) {
        return departmentService.departmentUpdate(id, dtoDepartmentIU);
    }

    @GetMapping("/get/{id}")
    @Override
    public DtoDepartment findByDepartmentId(@Valid @RequestBody Long id) {
        return departmentService.findByDepartmentId(id);
    }

    @GetMapping("/getAll")
    @Override
    public List<DtoDepartment> getAllDepartment() {
        return departmentService.getAllDepartment();
    }
}
