package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFaculty;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFacultyIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Faculty;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Department;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.FacultyRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.DepartmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IFacultyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacultyServiceImpl implements IFacultyService {

 @Autowired
 private FacultyRepository facultyRepository;

 @Autowired
 private DepartmentRepository departmentRepository;


    @Override
    @Transactional
    public DtoFaculty facultyAdd(DtoFacultyIU dtoFacultyIU) {
        Faculty faculty=new Faculty();
        faculty.setName(dtoFacultyIU.getName());
        faculty.setNumberOfPersonel(dtoFacultyIU.getNumberOfPersonel());
        faculty.setDean(dtoFacultyIU.getDean());
        faculty.setAvailable(dtoFacultyIU.getAvailable());
        faculty.setSecretary(dtoFacultyIU.getSecretary());
        faculty.setAddress(dtoFacultyIU.getAddress());
        faculty.setYear_of_establishment(dtoFacultyIU.getYear_of_establishment());
        
        Faculty savedFaculty = facultyRepository.save(faculty);
        // saveAndFlush kullanılabilir veya DTO'ya manuel atama yapılabilir
        return convertToDto(savedFaculty);
    }

    @Override
    @Transactional
    public DtoFaculty facultyUpdate(Long id,DtoFacultyIU dtoFacultyIU) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_FACULTY_ID, HttpStatus.BAD_REQUEST));
        faculty.setAddress(dtoFacultyIU.getAddress());
        faculty.setAvailable(dtoFacultyIU.getAvailable());
        faculty.setDean(dtoFacultyIU.getDean());
        faculty.setName(dtoFacultyIU.getName());
        faculty.setSecretary(dtoFacultyIU.getSecretary());
        faculty.setNumberOfPersonel(dtoFacultyIU.getNumberOfPersonel());
        faculty.setYear_of_establishment(dtoFacultyIU.getYear_of_establishment());
        Faculty savedFaculty = facultyRepository.save(faculty);
        return convertToDto(savedFaculty);
    }

    @Override
    @Transactional
    public void facultyDelete(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_FACULTY_ID, HttpStatus.BAD_REQUEST));
        
        if (faculty.getDepartments() != null) {
            for (Department department : faculty.getDepartments()) {
                department.setFaculty(null);
                departmentRepository.save(department);
            }
        }

        facultyRepository.delete(faculty);
    }

    @Override
    public DtoFaculty findByFacultyId(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_FACULTY_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(faculty);
    }

    @Override
    public List<DtoFaculty> getAllFaculty() {
        List<Faculty> allFaculty = facultyRepository.findAll();
        List<DtoFaculty> dtoFaculties=new ArrayList<>();
        for (Faculty faculty:allFaculty){
            dtoFaculties.add(convertToDto(faculty));
        }
        return dtoFaculties ;
    }

    private DtoFaculty convertToDto(Faculty faculty) {
        DtoFaculty dto = new DtoFaculty();
        BeanUtils.copyProperties(faculty, dto);
        dto.setId(faculty.getId());
        
        // Eğer createdDate null ise (henüz veritabanından dönmediyse) şimdiki zamanı set et
        if (faculty.getCreatedDate() != null) {
            dto.setCreatedDate(faculty.getCreatedDate().toString());
        } else {
            dto.setCreatedDate(LocalDateTime.now().toString());
        }
        return dto;
    }
}
