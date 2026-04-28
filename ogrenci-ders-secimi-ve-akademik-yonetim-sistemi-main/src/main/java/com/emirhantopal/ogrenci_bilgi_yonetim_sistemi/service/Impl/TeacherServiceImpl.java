package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacherIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Department;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Teacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.CourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.DepartmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.TeacherRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.CourseSectionRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.ITeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements ITeacherService {
    
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseSectionRepository courseSectionRepository;
    
    @Override
    @Transactional
    public DtoTeacher teacherAdd(DtoTeacherIU dtoTeacherIU) {
        Teacher teacher = new Teacher();
        
        teacher.setFirstName(dtoTeacherIU.getFirstName());
        teacher.setLastName(dtoTeacherIU.getLastName());
        

        if (dtoTeacherIU.getDepartmentId() != null && dtoTeacherIU.getDepartmentId() > 0) {
            Department department = departmentRepository.findById(dtoTeacherIU.getDepartmentId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
            teacher.setDepartment(department);
        }
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToDto(savedTeacher);
    }

    @Override
    @Transactional
    public DtoTeacher teacherUpdate(Long id, DtoTeacherIU dtoTeacherIU) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST));
        teacher.setFirstName(dtoTeacherIU.getFirstName());
        teacher.setLastName(dtoTeacherIU.getLastName());

        if (dtoTeacherIU.getDepartmentId() != null && dtoTeacherIU.getDepartmentId() > 0) {
            Department department = departmentRepository.findById(dtoTeacherIU.getDepartmentId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
            teacher.setDepartment(department);
        } else {
            teacher.setDepartment(null);
        }
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToDto(savedTeacher);
    }

    @Override
    @Transactional
    public void teacherDelete(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST));
        
        // Öğretmene bağlı CourseSection'ları temizle
        List<CourseSection> allSections = courseSectionRepository.findAll();
        for (CourseSection section : allSections) {
            if (section.getTeacher() != null && section.getTeacher().getId().equals(id)) {
                section.setTeacher(null);
                courseSectionRepository.save(section);
            }
        }

        teacherRepository.delete(teacher);
        System.out.println("Öğretmen kaydı silindi. "+ id);
    }

    @Override
    public List<DtoTeacher> teacherAll() {
        List<Teacher> allTeacher = teacherRepository.findAll();
        List<DtoTeacher> dtoTeachers = new ArrayList<>();
        for (Teacher teacher :allTeacher){
            dtoTeachers.add(convertToDto(teacher));
        }
        return dtoTeachers;
    }

    @Override
    public DtoTeacher findByTeacherId(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(teacher);
    }

    private DtoTeacher convertToDto(Teacher teacher) {
        DtoTeacher dto = new DtoTeacher();
        BeanUtils.copyProperties(teacher, dto);
        
        // ID set etme işlemini garantiye al
        dto.setId(teacher.getId());
        
        if (teacher.getCreatedDate() != null) {
            dto.setCreatedDate(teacher.getCreatedDate().toString());
        }

        if (teacher.getDepartment() != null) {
            DtoDepartment dtoDepartment = new DtoDepartment();
            BeanUtils.copyProperties(teacher.getDepartment(), dtoDepartment);
            dtoDepartment.setId(teacher.getDepartment().getId());
            if (teacher.getDepartment().getCreatedDate() != null) {
                dtoDepartment.setCreatedDate(teacher.getDepartment().getCreatedDate().toString());
            }
            dto.setDepartment(dtoDepartment);
        }
        return dto;
    }
}
