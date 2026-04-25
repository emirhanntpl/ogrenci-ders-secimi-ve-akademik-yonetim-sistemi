package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartmentIU;

import java.util.List;

public interface IDepartmentService {



    public DtoDepartment departmentAdd(DtoDepartmentIU dtoDepartmentIU);

    public DtoDepartment departmentUpdate(Long id, DtoDepartmentIU dtoDepartmentIU);

    public void departmentDelete(Long id);

    public List<DtoDepartment> getAllDepartment();

    public DtoDepartment findByDepartmentId(Long id);





}
