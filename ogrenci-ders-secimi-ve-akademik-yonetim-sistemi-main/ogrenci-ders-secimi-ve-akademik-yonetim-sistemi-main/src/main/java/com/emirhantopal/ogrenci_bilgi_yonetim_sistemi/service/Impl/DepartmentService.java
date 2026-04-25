package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartmentIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Department;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.DepartmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IDepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public DtoDepartment departmentAdd(DtoDepartmentIU dtoDepartmentIU) {
        Department department=new Department();
        DtoDepartment dtoDepartment=new DtoDepartment();
        department.setName(dtoDepartmentIU.getName());
        department.setHead_of_department(dtoDepartmentIU.getHead_of_department());
        department.setQuota(dtoDepartmentIU.getQuota());
        Department savedDepartment = departmentRepository.save(department);
        BeanUtils.copyProperties(savedDepartment, dtoDepartment);
        return  dtoDepartment;
    }

    @Override
    public DtoDepartment departmentUpdate(Long id, DtoDepartmentIU dtoDepartmentIU) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
        DtoDepartment dtoDepartment= new DtoDepartment();
        department.setHead_of_department(dtoDepartmentIU.getHead_of_department());
        department.setQuota(dtoDepartmentIU.getQuota());
        department.setName(dtoDepartmentIU.getName());
        Department updateDepartment = departmentRepository.save(department);
        BeanUtils.copyProperties(updateDepartment, dtoDepartment);

        return dtoDepartment;
    }

    @Override
    public void departmentDelete(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
        departmentRepository.delete(department);
        System.out.println("Departman kaydı silindi . "+ id);
    }

    @Override
    public List<DtoDepartment> getAllDepartment() {
        List<Department> allDepartment = departmentRepository.findAll();
        if (allDepartment.isEmpty()) throw new BaseException(MessageType.DEPARTMENT_LIST_IS_EMPTY,HttpStatus.BAD_REQUEST);
        List<DtoDepartment> dtoDepartments = new ArrayList<>();
        for (Department department:allDepartment){
            DtoDepartment dtoDepartment=new DtoDepartment();
            dtoDepartment.setHead_of_department(department.getHead_of_department());
            dtoDepartment.setQuota(department.getQuota());
            dtoDepartment.setName(department.getName());
            dtoDepartments.add(dtoDepartment);

        }
        return dtoDepartments;
    }

    @Override
    public DtoDepartment findByDepartmentId(Long id) {
        Department departmentId = departmentRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
         DtoDepartment dtoDepartment=new DtoDepartment();
         BeanUtils.copyProperties(departmentId, dtoDepartment);

        return  dtoDepartment;
    }
}
