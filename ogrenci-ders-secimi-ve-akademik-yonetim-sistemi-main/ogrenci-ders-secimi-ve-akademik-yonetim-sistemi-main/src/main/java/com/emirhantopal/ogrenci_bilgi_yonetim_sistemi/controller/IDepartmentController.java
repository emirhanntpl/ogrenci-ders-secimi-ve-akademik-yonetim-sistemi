package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartmentIU;

import java.util.List;

public interface IDepartmentController {

    public DtoDepartment departmentAdd(DtoDepartmentIU dtoDepartmentIU);

    public void departmentDelete(Long id);

    public DtoDepartment departmentUpdate(Long id, DtoDepartmentIU dtoDepartmentIU);

    public DtoDepartment findByDepartmentId(Long id);

    public List<DtoDepartment> getAllDepartment();










}
