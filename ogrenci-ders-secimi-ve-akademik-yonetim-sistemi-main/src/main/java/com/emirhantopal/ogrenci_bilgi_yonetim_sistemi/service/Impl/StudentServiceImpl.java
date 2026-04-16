package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudentIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoUser;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Department;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.User;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.DepartmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.StudentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.UserRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IStudentService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DtoStudent studentAdd(DtoStudentIU dtoIU) {
        Student student = new Student();

        student.setFirstName(dtoIU.getFirstName());
        student.setLastName(dtoIU.getLastName());
        student.setStudentNumber(dtoIU.getStudentNumber());
        student.setEmail(dtoIU.getEmail());
        student.setTelNumber(dtoIU.getTelNumber());

        mapEntitiesToStudent(dtoIU, student);

        Student savedStudent = studentRepository.save(student);
        return convertToDto(savedStudent);
    }

    @Override
    public DtoStudent studentUpdate(Long id, DtoStudentIU dtoIU) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));

        student.setFirstName(dtoIU.getFirstName());
        student.setLastName(dtoIU.getLastName());
        student.setStudentNumber(dtoIU.getStudentNumber());
        student.setEmail(dtoIU.getEmail());
        student.setTelNumber(dtoIU.getTelNumber());

        mapEntitiesToStudent(dtoIU, student);

        Student updatedStudent = studentRepository.save(student);
        return convertToDto(updatedStudent);
    }

    @Override
    public void studentDelete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));
        studentRepository.delete(student);
        System.out.println("Öğrenci kaydı silindi. "+ id);
    }

    @Override
    public List<DtoStudent> getAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
        if (allStudents.isEmpty()) {
            throw new BaseException(MessageType.STUDENT_LIST_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }

        List<DtoStudent> dtoList = new ArrayList<>();
        for (Student student : allStudents) {
            dtoList.add(convertToDto(student));
        }
        return dtoList;
    }

    @Override
    public DtoStudent findByStudentId(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(student);
    }

    // --- HELPER METHODS ---

    private void mapEntitiesToStudent(DtoStudentIU dtoIU, Student student) {
        if (dtoIU.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dtoIU.getDepartmentId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
            student.setDepartment(department);
        }

        if (dtoIU.getUserId() != null) {
            User user = userRepository.findById(dtoIU.getUserId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_USER_ID, HttpStatus.BAD_REQUEST));
            student.setUser(user);
        }
    }

    private DtoStudent convertToDto(Student student) {
        DtoStudent dto = new DtoStudent();
        BeanUtils.copyProperties(student, dto);

        if (student.getDepartment() != null) {
            DtoDepartment dtoDepartment = new DtoDepartment();
            BeanUtils.copyProperties(student.getDepartment(), dtoDepartment);
            dto.setDepartment(dtoDepartment);
        }

        if (student.getUser() != null) {
            DtoUser dtoUser = new DtoUser();
            BeanUtils.copyProperties(student.getUser(), dtoUser);
            dto.setUser(dtoUser);
        }

        return dto;
    }
}