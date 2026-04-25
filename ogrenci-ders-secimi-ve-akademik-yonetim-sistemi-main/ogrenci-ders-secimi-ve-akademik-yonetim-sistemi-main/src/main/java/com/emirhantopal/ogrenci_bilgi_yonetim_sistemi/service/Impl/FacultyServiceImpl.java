package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFaculty;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoFacultyIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Faculty;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.FacultyRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IFacultyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacultyServiceImpl implements IFacultyService {

 @Autowired
 private FacultyRepository facultyRepository;


    @Override
    public DtoFaculty facultyAdd(DtoFacultyIU dtoFacultyIU) {
        Faculty faculty=new Faculty();
        DtoFaculty dtoFaculty=new DtoFaculty();
        faculty.setName(dtoFacultyIU.getName());
        faculty.setNumberOfPersonel(dtoFacultyIU.getNumberOfPersonel());
        faculty.setDean(dtoFacultyIU.getDean());
        faculty.setAvailable(dtoFacultyIU.getAvailable());
        faculty.setSecretary(dtoFacultyIU.getSecretary());
        faculty.setAddress(dtoFacultyIU.getAddress());
        faculty.setYear_of_establishment(dtoFacultyIU.getYear_of_establishment());
        Faculty savedFaculty = facultyRepository.save(faculty);
        BeanUtils.copyProperties(savedFaculty, dtoFaculty);
        return dtoFaculty;
    }

    @Override
    public DtoFaculty facultyUpdate(Long id,DtoFacultyIU dtoFacultyIU) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_FACULTY_ID, HttpStatus.BAD_REQUEST));
        DtoFaculty dtoFaculty=new DtoFaculty();
        faculty.setAddress(dtoFacultyIU.getAddress());
        faculty.setAvailable(dtoFacultyIU.getAvailable());
        faculty.setDean(dtoFacultyIU.getDean());
        faculty.setName(dtoFacultyIU.getName());
        faculty.setSecretary(dtoFacultyIU.getSecretary());
        faculty.setNumberOfPersonel(dtoFacultyIU.getNumberOfPersonel());
        faculty.setYear_of_establishment(dtoFacultyIU.getYear_of_establishment());
        Faculty savedFaculty = facultyRepository.save(faculty);
        BeanUtils.copyProperties(savedFaculty, dtoFaculty);
        return dtoFaculty;
    }

    @Override
    public void facultyDelete(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_FACULTY_ID, HttpStatus.BAD_REQUEST));
        facultyRepository.delete(faculty);
        System.out.println("Fakülte kaydı silindi . "+ id);

    }

    @Override
    public DtoFaculty findByFacultyId(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_FACULTY_ID, HttpStatus.BAD_REQUEST));
        DtoFaculty dtoFaculty=new DtoFaculty();
        BeanUtils.copyProperties(faculty, dtoFaculty);
        return dtoFaculty;
    }

    @Override
    public List<DtoFaculty> getAllFaculty() {
        List<Faculty> allFaculty = facultyRepository.findAll();
        if (allFaculty.isEmpty()){
            throw new BaseException(MessageType.FACULTY_LIST_IS_EMPTY,HttpStatus.BAD_REQUEST);
        }
        List<DtoFaculty> dtoFaculties=new ArrayList<>();
        for (Faculty faculty:allFaculty){
            DtoFaculty faculty1=new DtoFaculty();
            faculty1.setDean(faculty.getDean());
            faculty1.setAvailable(faculty.getAvailable());
            faculty1.setName(faculty.getName());
            faculty1.setSecretary(faculty.getSecretary());
            faculty1.setNumberOfPersonel(faculty.getNumberOfPersonel());
            faculty1.setYear_of_establishment(faculty.getYear_of_establishment());
            faculty1.setAddress(faculty.getAddress());
            dtoFaculties.add(faculty1);
        }
        return dtoFaculties ;
    }
}
