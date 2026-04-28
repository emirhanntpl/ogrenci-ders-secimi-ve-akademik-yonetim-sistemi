package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoClassroom;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoClassroomIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Classroom;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.CourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.ClassroomRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.CourseSectionRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IClassroomService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassroomService implements IClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private CourseSectionRepository courseSectionRepository;


    @Override
    @Transactional
    public DtoClassroom classroomAdd(DtoClassroomIU dtoClassroomIU) {
        
        // UNIQUE KONTROLU: Aynı numaraya (Örn: B-101) sahip başka derslik var mı?
        if (classroomRepository.findByRoomNumber(dtoClassroomIU.getRoomNumber()).isPresent()) {
            throw new RuntimeException("Bu numaraya sahip bir derslik (" + dtoClassroomIU.getRoomNumber() + ") zaten mevcut!");
        }

        Classroom classroom=new Classroom();
        classroom.setCapacity(dtoClassroomIU.getCapacity());
        classroom.setRoomNumber(dtoClassroomIU.getRoomNumber());
        Classroom savedClassroom = classroomRepository.save(classroom);
        return convertToDto(savedClassroom);
    }

    @Override
    @Transactional
    public void classroomDelete(Long id) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.CLASSROOM_IS_EMPTY, HttpStatus.BAD_REQUEST));
        
        // Sınıfa bağlı CourseSection'ları kontrol et
        List<CourseSection> allSections = courseSectionRepository.findAll();
        for (CourseSection section : allSections) {
            if (section.getClassroom() != null && section.getClassroom().getId().equals(id)) {
                // Derslik silindiğinde şubelerdeki sınıf referansı null yapılır (Şubeler silinmez, sadece sınıfsız kalır)
                section.setClassroom(null);
                courseSectionRepository.save(section);
            }
        }

        classroomRepository.delete(classroom);
        System.out.println("Classroom silindi. " + id);
    }

    @Override
    @Transactional
    public DtoClassroom classroomUpdate(Long id, DtoClassroomIU dtoClassroomIU) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.CLASSROOM_IS_EMPTY, HttpStatus.BAD_REQUEST));
        
        // UNIQUE KONTROLU (Kendisi hariç)
        var existByRoom = classroomRepository.findByRoomNumber(dtoClassroomIU.getRoomNumber());
        if (existByRoom.isPresent() && !existByRoom.get().getId().equals(id)) {
             throw new RuntimeException("Bu numaraya sahip bir derslik zaten mevcut!");
        }

        classroom.setCapacity(dtoClassroomIU.getCapacity());
        classroom.setRoomNumber(dtoClassroomIU.getRoomNumber());
        Classroom savedClassroom = classroomRepository.save(classroom);
        return convertToDto(savedClassroom);
    }

    @Override
    public DtoClassroom findByClassroomId(Long id) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.CLASSROOM_IS_EMPTY, HttpStatus.BAD_REQUEST));
        return convertToDto(classroom);
    }

    @Override
    public List<DtoClassroom> getAllClassroom() {
        List<Classroom> allClassroom = classroomRepository.findAll();
        List<DtoClassroom> dtoClassrooms=new ArrayList<>();
        for (Classroom classroom:allClassroom){
            dtoClassrooms.add(convertToDto(classroom));
        }
        return dtoClassrooms;
    }

    private DtoClassroom convertToDto(Classroom classroom) {
        DtoClassroom dto = new DtoClassroom();
        BeanUtils.copyProperties(classroom, dto);
        dto.setId(classroom.getId());
        if (classroom.getCreatedDate() != null) {
            dto.setCreatedDate(classroom.getCreatedDate().toString());
        } else {
             dto.setCreatedDate(LocalDateTime.now().toString());
        }
        return dto;
    }
}
