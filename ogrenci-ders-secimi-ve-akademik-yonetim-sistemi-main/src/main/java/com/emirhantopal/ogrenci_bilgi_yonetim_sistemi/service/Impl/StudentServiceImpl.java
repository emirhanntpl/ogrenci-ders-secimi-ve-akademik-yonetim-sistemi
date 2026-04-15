package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudentIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.StudentRepository;
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


    @Override
    public DtoStudent studentAdd(DtoStudentIU dtoStudentIU) {
        Student student= new Student();
        DtoStudent dtoStudent= new DtoStudent();
        student.setFirstName(dtoStudentIU.getFirstName());
        student.setLastName(dtoStudentIU.getLastName());
        student.setDepartment(dtoStudentIU.getDepartment());
        student.setStudentNumber(dtoStudentIU.getStudentNumber());
        student.setTelNumber(dtoStudentIU.getTelNumber());
        student.setEmail(dtoStudentIU.getEmail());
        Student savedStudent = studentRepository.save(student);
        BeanUtils.copyProperties(savedStudent, dtoStudent);

        return dtoStudent;
    }

    @Override
    public DtoStudent studentUpdate(Long id, DtoStudentIU dtoStudentIU) {

        Student student = studentRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));
        DtoStudent dtoStudent= new DtoStudent();
        student.setFirstName(dtoStudentIU.getFirstName());
        student.setLastName(dtoStudentIU.getLastName());
        student.setDepartment(dtoStudentIU.getDepartment());
        student.setStudentNumber(dtoStudentIU.getStudentNumber());
        student.setTelNumber(dtoStudentIU.getTelNumber());
        student.setEmail(dtoStudentIU.getEmail());
        Student savedStudent = studentRepository.save(student);
        BeanUtils.copyProperties(savedStudent, dtoStudent);
        return dtoStudent;


    }



    @Override
    public void studentDelete(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));
        studentRepository.delete(student);
        System.out.println("Öğrenci kaydı silindi. "+ id);
    }

    @Override
    public List<DtoStudent> getAllStudent() {
        List<Student> allStudent = studentRepository.findAll();
        if (allStudent.isEmpty()) throw new BaseException(MessageType.STUDENT_LIST_IS_EMPTY,HttpStatus.BAD_REQUEST);
        List<DtoStudent> dtoStudents = new ArrayList<>();
        for (Student student:allStudent){
            DtoStudent dtoStudent=new DtoStudent();
            dtoStudent.setFirstName(student.getFirstName());
            dtoStudent.setLastName(student.getLastName());
            dtoStudent.setDepartment(student.getDepartment());
            dtoStudent.setStudentNumber(student.getStudentNumber());
            dtoStudent.setTelNumber(student.getTelNumber());
            dtoStudent.setEmail(student.getEmail());
            dtoStudents.add(dtoStudent);

        }
          return  dtoStudents;

    }

    @Override
    public DtoStudent getStudentById(Long id) {
        Student studentId = studentRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));
        DtoStudent dtoStudent= new DtoStudent();
        BeanUtils.copyProperties(studentId,dtoStudent);
        return  dtoStudent  ;
    }
}
