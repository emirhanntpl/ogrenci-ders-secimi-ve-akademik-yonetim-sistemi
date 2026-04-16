package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoTeacherIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Department;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Teacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.DepartmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.TeacherRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.ITeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements ITeacherService {
    
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Override
    public DtoTeacher teacherAdd(DtoTeacherIU dtoTeacherIU) {
        Teacher teacher = new Teacher();
        DtoTeacher dtoTeacher = new DtoTeacher();
        
        teacher.setFirstName(dtoTeacherIU.getFirstName());
        teacher.setLastName(dtoTeacherIU.getLastName());
        

        if (dtoTeacherIU.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dtoTeacherIU.getDepartmentId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
            teacher.setDepartment(department);
        }
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        BeanUtils.copyProperties(savedTeacher, dtoTeacher);
        return dtoTeacher;
    }

    @Override
    public DtoTeacher teacherUpdate(Long id, DtoTeacherIU dtoTeacherIU) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST));
        DtoTeacher dtoTeacher = new DtoTeacher();
        teacher.setFirstName(dtoTeacherIU.getFirstName());
        teacher.setLastName(dtoTeacherIU.getLastName());

        if (dtoTeacherIU.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dtoTeacherIU.getDepartmentId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
            teacher.setDepartment(department);
        }
        Teacher savedTeacher = teacherRepository.save(teacher);
        BeanUtils.copyProperties(savedTeacher, dtoTeacher);
        return dtoTeacher;
    }

    @Override
    public void teacherDelete(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST));
        teacherRepository.delete(teacher);
        System.out.println("Öğretmen kaydı silindi. "+ id);
    }

    @Override
    public List<DtoTeacher> teacherAll() {
        List<Teacher> allTeacher = teacherRepository.findAll();
        if (allTeacher.isEmpty())
            throw new BaseException(MessageType.TEACHER_LIST_IS_EMPTY, HttpStatus.BAD_REQUEST);
        List<DtoTeacher> dtoTeachers = new ArrayList<>();
        for (Teacher teacher :allTeacher){
            DtoTeacher dtoTeacher=new DtoTeacher();
            dtoTeacher.setFirstName(teacher.getFirstName());
            dtoTeacher.setLastName(teacher.getLastName());
            if (teacher.getDepartment() != null){
                DtoDepartment dtoDepartment=new DtoDepartment();
                BeanUtils.copyProperties(teacher.getDepartment(), dtoDepartment);
                dtoTeacher.setDepartment(dtoDepartment);
            }
            dtoTeachers.add(dtoTeacher);
        }
        return dtoTeachers;
    }

    @Override
    public DtoTeacher findByTeacherId(Long id) {
        Optional<Teacher> byId = teacherRepository.findById(id);
        if (byId.isEmpty()){
            throw new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST);
        }

        Teacher teacher = byId.get();
        DtoTeacher dtoTeacher = new DtoTeacher();
        BeanUtils.copyProperties(teacher, dtoTeacher);
        
        if (teacher.getDepartment() != null) {
            DtoDepartment dtoDepartment = new DtoDepartment();
            BeanUtils.copyProperties(teacher.getDepartment(), dtoDepartment);
            dtoTeacher.setDepartment(dtoDepartment);
        }
        return dtoTeacher;
    }
}