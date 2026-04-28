package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartmentIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFaculty;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Department;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Faculty;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Teacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.DepartmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.FacultyRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.StudentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.TeacherRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IDepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Override
    @Transactional
    public DtoDepartment departmentAdd(DtoDepartmentIU dtoDepartmentIU) {
        Department department=new Department();
        department.setName(dtoDepartmentIU.getName());
        department.setCode(dtoDepartmentIU.getCode());
        department.setHead_of_department(dtoDepartmentIU.getHead_of_department());
        department.setQuota(dtoDepartmentIU.getQuota());

        if (dtoDepartmentIU.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(dtoDepartmentIU.getFacultyId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_FACULTY_ID, HttpStatus.BAD_REQUEST));
            department.setFaculty(faculty);
        }

        Department savedDepartment = departmentRepository.save(department);
        return convertToDto(savedDepartment);
    }

    @Override
    @Transactional
    public DtoDepartment departmentUpdate(Long id, DtoDepartmentIU dtoDepartmentIU) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
        department.setHead_of_department(dtoDepartmentIU.getHead_of_department());
        department.setQuota(dtoDepartmentIU.getQuota());
        department.setName(dtoDepartmentIU.getName());
        department.setCode(dtoDepartmentIU.getCode());

        if (dtoDepartmentIU.getFacultyId() != null) {
            Faculty faculty = facultyRepository.findById(dtoDepartmentIU.getFacultyId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_FACULTY_ID, HttpStatus.BAD_REQUEST));
            department.setFaculty(faculty);
        }

        Department updateDepartment = departmentRepository.save(department);
        return convertToDto(updateDepartment);
    }

    @Override
    @Transactional
    public void departmentDelete(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
        
        if (department.getStudents() != null) {
            for (Student student : department.getStudents()) {
                student.setDepartment(null);
                studentRepository.save(student);
            }
        }

        if (department.getTeachers() != null) {
            for (Teacher teacher : department.getTeachers()) {
                teacher.setDepartment(null);
                teacherRepository.save(teacher);
            }
        }

        departmentRepository.delete(department);
    }

    @Override
    public List<DtoDepartment> getAllDepartment() {
        List<Department> allDepartment = departmentRepository.findAll();
        List<DtoDepartment> dtoDepartments = new ArrayList<>();
        for (Department department:allDepartment){
            dtoDepartments.add(convertToDto(department));
        }
        return dtoDepartments;
    }

    @Override
    public DtoDepartment findByDepartmentId(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(department);
    }

    private DtoDepartment convertToDto(Department department) {
        DtoDepartment dto = new DtoDepartment();
        BeanUtils.copyProperties(department, dto);
        dto.setId(department.getId());
        dto.setCode(department.getCode());
        
        if (department.getCreatedDate() != null) {
            dto.setCreatedDate(department.getCreatedDate().toString());
        } else {
            dto.setCreatedDate(LocalDateTime.now().toString());
        }

        if (department.getFaculty() != null) {
            dto.setFaculty(convertToDto(department.getFaculty()));
        }
        
        return dto;
    }

    private DtoFaculty convertToDto(Faculty faculty) {
        DtoFaculty dto = new DtoFaculty();
        BeanUtils.copyProperties(faculty, dto);
        dto.setId(faculty.getId());
        if (faculty.getCreatedDate() != null) {
            dto.setCreatedDate(faculty.getCreatedDate().toString());
        }
        return dto;
    }
}
